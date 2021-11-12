package dungeonmania.gamemap;

import java.util.List;
import java.util.Random;

import dungeonmania.AnimationUtility;
import dungeonmania.MovingEntities.*;
import dungeonmania.response.models.AnimationQueue;
import dungeonmania.util.Position;

public class EnermySpawner {
    // Seed counter used for spider
    int seed;
    int period;

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
        Mercenary newMerc = new Mercenary("merc" + System.currentTimeMillis(), "mercenary", map.getEntryPos());
        // Check conditions to spawn mercenary
        if (period != 0 && period % 15 == 0) {
            if (newMerc.canPass(map.getMap(), map.getEntryPos())) {
                map.getMap().get(map.getEntryPos()).add(newMerc);
                AnimationUtility.setMovingEntityHealthBar(animations, newMerc);
                map.getPlayer().registerObserver(newMerc);
            } else {
                period--;
            }
        }
    }
}
