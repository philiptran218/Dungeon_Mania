package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class BattleTest {
    // Test helper: Checks if entity on a given position.
    public boolean isEntityOnTile(DungeonResponse response, Position pos, String id) {
        for (EntityResponse entity : response.getEntities()) {
            if (entity.getId() == id) {
                return entity.getPosition().equals(pos);
            }
        }
        return false;
    }
    // Gets the id of entity on a position:
    public String getEntityId(Position pos, DungeonResponse response) {
        for (EntityResponse entity : response.getEntities()) {
            if (entity.getPosition().equals(pos) && entity.getPosition().getLayer() == pos.getLayer()) {
                return entity.getId();
            }
        }
        return null;
    }


    // Tests for Battles:

    // Testing no weapon combat against a spider
    @Test
    public void basicBattle() {
        


    }

    // Tests using buildable weapons to kill a mercenary
    @Test
    public void testBuildableWeaponsMercenary() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("buildable_battle", "Peaceful");

        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.build("bow");
        newDungeon.build("shield");
        newDungeon.tick(null, Direction.RIGHT);
        // This movement should fight the mercenary and win
        DungeonResponse temp = newDungeon.tick(null, Direction.RIGHT);
        assertFalse(temp.getEntities().stream().anyMatch(e -> e.getType().equals("mercenary")));
    }





}