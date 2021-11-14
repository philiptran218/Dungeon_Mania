package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class EnemySpawnTest {

    // Tests if mercenary spawns after several ticks
    @Test
    public void testMercenarySpawn() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("enemy_spawn", "peaceful");
        DungeonResponse info;
        
        // Tick 30 times to reach merc spawn (1st spawn in an assassin)
        for (int i = 0; i < 29; i++) {
            info = newDungeon.tick(null, Direction.RIGHT);
        }
        // 30th tick will spawn the mercenary
        info = newDungeon.tick(null, Direction.RIGHT);
        assertTrue(info.getEntities().stream().anyMatch(itm -> itm.getType().equals("mercenary")));

        // Tick 15 more times to spawn another mercenary
        for (int i = 0; i < 14; i++) {
            info = newDungeon.tick(null, Direction.RIGHT);
        }
        info = newDungeon.tick(null, Direction.RIGHT);
        // Should only be 1 merc, since previous merc is killed by player
        assertTrue(info.getEntities().stream().anyMatch(itm -> itm.getType().equals("mercenary")));
    }

    // Tests if assassin will spawn on map (has 20% chance)
    @Test
    public void testAssassinSpawn() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("enemy_spawn", "peaceful");
        DungeonResponse info;
        
        // The seed for assassin spawns should ensure that it is the first enemy spawned
        for (int i = 0; i < 14; i++) {
            info = newDungeon.tick(null, Direction.RIGHT);
        }
        // 15th tick will spawn the assassin
        info = newDungeon.tick(null, Direction.RIGHT);
        assertTrue(info.getEntities().stream().anyMatch(itm -> itm.getType().equals("assassin")));
    }

    // Tests that hydra should not spawn on peaceful
    @Test
    public void testNoHydraSpawnPeaceful() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("enemy_spawn", "peaceful");
        DungeonResponse info;
        // Tick 49 times to reach hydra spawn
        for (int i = 0; i < 49; i++) {
            info = newDungeon.tick(null, Direction.RIGHT);
            assertFalse(info.getEntities().stream().anyMatch(itm -> itm.getType().equals("hydra")));
        }
        info = newDungeon.tick(null, Direction.RIGHT);
        // Hydra should not appear
        assertFalse(info.getEntities().stream().anyMatch(itm -> itm.getType().equals("hydra")));
    }

    // Tests that hydra should not spawn on standard
    @Test
    public void testNoHydraSpawnStandard() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("enemy_spawn", "standard");
        DungeonResponse info;
        // Tick 49 times to reach hydra spawn
        for (int i = 0; i < 49; i++) {
            info = newDungeon.tick(null, Direction.RIGHT);
            assertFalse(info.getEntities().stream().anyMatch(itm -> itm.getType().equals("hydra")));
        }
        info = newDungeon.tick(null, Direction.RIGHT);
        // Hydra should not appear
        assertFalse(info.getEntities().stream().anyMatch(itm -> itm.getType().equals("hydra")));
    }

    // Tests hydra spawning after 50 ticks on hard
    @Test
    public void testHydraSpawnHard() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("enemy_spawn", "hard");
        DungeonResponse info;
        // Tick 49 times to reach hydra spawn
        for (int i = 0; i < 49; i++) {
            info = newDungeon.tick(null, Direction.RIGHT);
            assertFalse(info.getEntities().stream().anyMatch(itm -> itm.getType().equals("hydra")));
        }
        info = newDungeon.tick(null, Direction.RIGHT);
        // Hydra should now appear
        assertTrue(info.getEntities().stream().anyMatch(itm -> itm.getType().equals("hydra")));
    }
}