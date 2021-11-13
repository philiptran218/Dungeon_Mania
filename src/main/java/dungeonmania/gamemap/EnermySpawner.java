package dungeonmania.gamemap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import dungeonmania.AnimationUtility;
import dungeonmania.Entity;
import dungeonmania.MovingEntities.*;
import dungeonmania.response.models.AnimationQueue;
import dungeonmania.util.Position;

public class EnermySpawner {
    // Seed counter used for spider
    int seed;
    int period = 1;
    int mercAssSeed = 9999;

    // ******************************
    GameMap map;

    /**
     * Spawner constructor
     * @param map
     */
    public EnermySpawner(GameMap map) {
        this.map = map;
    }

    /**
     * Spawn all respective mobs.
     */
    public void spawnMob(List<AnimationQueue> animations) {
        spawnSpider(animations);
        spawnMercenary(animations);
        if (map.getBattle().getDifficulty().equals("hard")) {
            spawnHydra(animations);
        }
        period++;
    }

    /**
     * Spawns a spider on the map with a one in ten chance (with
     * restrictions).
     */
    public void spawnSpider(List<AnimationQueue> animations) {
        int spiders = 0;
        for (MovingEntity e : map.getMovingEntityList()) {
            if (e.isType("spider")) { spiders++; }
        }
        // Square too small:
        if(map.getWidth() < 2 || map.getHeight() < 2) { return; }
        // Check conditions
        Random random = new Random(seed);
        if (random.nextInt(10) == 5 && spiders < 5) {
            // Random x and y positions
            int xPos = new Random(seed + 37).nextInt(map.getWidth() - 2) + 1;
            int yPos = new Random(seed + 68).nextInt(map.getHeight() - 2) + 1;
            // Create the spider:
            Position newSpider = new Position(xPos, yPos, 3);
            Position checkAbove = new Position(xPos, yPos - 1, 3);
            Spider spider = new Spider("spider" + System.currentTimeMillis(), "spider", newSpider);
            // Check if current and above positions of the spiders are boulders:
            if (spider.canPass(map.getMap(), newSpider) && spider.canPass(map.getMap(), checkAbove)) {
                map.getMap().get(newSpider).add(spider);
                map.getPlayer().registerObserver(spider);
            }
            AnimationUtility.setMovingEntityHealthBar(animations, spider);
        }
        seed += 124;
    }

    /**
     * Periodically spawns a mecenary at the entry location.
     */
    public void spawnMercenary(List<AnimationQueue> animations) {
        // Check conditions to spawn mercenary/assassin
        if (period != 0 && period % 15 == 0) {
            Random random = new Random(mercAssSeed);
            // 20% chance of spawning an assassin instead
            if (random.nextInt(10) >= 8) {
                addAssassin(animations);
            } else {
                addMercenary(animations);
            }
            mercAssSeed += 50;
        }
    }

    /**
     * Adds the assassin to the map.
     * @param animations
     */
    public void addAssassin(List<AnimationQueue> animations) {
        Assassin newAssin = new Assassin("assin" + System.currentTimeMillis(), "assassin", map.getEntryPos());
        if (newAssin.canPass(map.getMap(), map.getEntryPos())) {
            map.getMap().get(map.getEntryPos()).add(newAssin);
            AnimationUtility.setMovingEntityHealthBar(animations, newAssin);
            map.getPlayer().registerObserver(newAssin);
        } else {
            period--;
        }
    }

    /**
     * Adds the mercenary to the map.
     * @param animations
     */
    public void addMercenary(List<AnimationQueue> animations) {
        Mercenary newMerc = new Mercenary("merc" + System.currentTimeMillis(), "mercenary", map.getEntryPos());
        if (newMerc.canPass(map.getMap(), map.getEntryPos())) {
            map.getMap().get(map.getEntryPos()).add(newMerc);
            AnimationUtility.setMovingEntityHealthBar(animations, newMerc);
            map.getPlayer().registerObserver(newMerc);
        } else {
            period--;
        }
    }

    public void spawnHydra(List<AnimationQueue> animations) {
        // Check conditions to spawn hydra
        if (period != 0 && period % 50 == 0) {
            Position spawnPos = randomEmptyPos();
            // If there are no empty positions, do not spawn hydra
            if (spawnPos == null) {
                return;
            }
            Hydra newHydra = new Hydra("hydra" + System.currentTimeMillis(), "hydra", spawnPos);
            map.getMap().get(spawnPos).add(newHydra);
            AnimationUtility.setMovingEntityHealthBar(animations, newHydra);
            map.getPlayer().registerObserver(newHydra);
        }
    }

    public Position randomEmptyPos() {
        Map<Position, List<Entity>> posMap = map.getMap();
        List<Position> emptyPos = new ArrayList<>();
        for (Position pos : posMap.keySet()) {
            if (posMap.get(pos.asLayer(1)).isEmpty() && !map.getPlayer().getPos().asLayer(0).equals(pos.asLayer(1))) {
                emptyPos.add(pos);
            }
        }
        if (emptyPos.isEmpty()) {
            return null;
        } else {
            // Return a random empty position on the map
            Random random = new Random();
            return emptyPos.get(random.nextInt(emptyPos.size()));
        }
    }
}
