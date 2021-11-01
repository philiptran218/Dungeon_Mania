package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.*;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class CollectableEntityTest {
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
    // Gets the id of entity on a position:
    public String getEntityId(Position pos, DungeonResponse response, String type) {
        for (EntityResponse entity : response.getEntities()) {
            if (entity.getPosition().equals(pos) && entity.getPosition().getLayer() == pos.getLayer()
                && entity.getType().equals(type)) {
                return entity.getId();
            }
        }
        return null;
    }

    // Tests for CollectableEntities:

    // Test that the entity can be picked up by the player.
    @Test
    public void testCollectablePickup() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("player_pickup_item", "Peaceful");
        List<ItemResponse> inv;
        
        inv = newDungeon.tick(null, Direction.RIGHT).getInventory();
        assertTrue(inv.stream().anyMatch(itm -> itm.getType().equals("wood")));

        inv = newDungeon.tick(null, Direction.RIGHT).getInventory();
        assertTrue(inv.stream().anyMatch(itm -> itm.getType().equals("arrow")));

        inv = newDungeon.tick(null, Direction.RIGHT).getInventory();
        assertTrue(inv.stream().anyMatch(itm -> itm.getType().equals("treasure")));

        inv = newDungeon.tick(null, Direction.DOWN).getInventory();
        assertTrue(inv.stream().anyMatch(itm -> itm.getType().equals("key")));

        inv = newDungeon.tick(null, Direction.LEFT).getInventory();
        assertTrue(inv.stream().anyMatch(itm -> itm.getType().equals("sword")));

        inv = newDungeon.tick(null, Direction.LEFT).getInventory();
        assertTrue(inv.stream().anyMatch(itm -> itm.getType().equals("health_potion")));

        inv = newDungeon.tick(null, Direction.LEFT).getInventory();
        assertTrue(inv.stream().anyMatch(itm -> itm.getType().equals("invincibility_potion")));

        inv = newDungeon.tick(null, Direction.DOWN).getInventory();
        assertTrue(inv.stream().anyMatch(itm -> itm.getType().equals("invisibility_potion")));

        inv = newDungeon.tick(null, Direction.RIGHT).getInventory();
        assertTrue(inv.stream().anyMatch(itm -> itm.getType().equals("bomb")));
    }

    // Test that the collected item can be used via tick() in DungeonManiaController
    @Test
    public void testUsedCollectable() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("player_pickup_item", "Peaceful");

        DungeonResponse temp = newDungeon.tick(null, Direction.RIGHT);

        String healthId = getEntityId(new Position(2, 2, 2), temp);
        String invincibleId = getEntityId(new Position(1, 2, 2), temp);
        String invisibleId = getEntityId(new Position(1, 3, 2), temp);
        String bombId = getEntityId(new Position(2, 3, 2), temp);

        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.DOWN);
        newDungeon.tick(null, Direction.LEFT);
        newDungeon.tick(null, Direction.LEFT);
        newDungeon.tick(null, Direction.LEFT);
        newDungeon.tick(null, Direction.DOWN);
        temp = newDungeon.tick(null, Direction.RIGHT);

        temp = newDungeon.tick(healthId, null);
        assertFalse(temp.getInventory().stream().anyMatch(itm -> itm.getType().equals("health_potion")));
        temp = newDungeon.tick(invincibleId, null);
        assertFalse(temp.getInventory().stream().anyMatch(itm -> itm.getType().equals("invincibility_potion")));
        temp = newDungeon.tick(invisibleId, null);
        assertFalse(temp.getInventory().stream().anyMatch(itm -> itm.getType().equals("invisibility_potion")));
        temp = newDungeon.tick(bombId, null);
        assertFalse(temp.getInventory().stream().anyMatch(itm -> itm.getType().equals("bomb")));
    }
        
    // Tests for BuildableEntities (included in CollectableEntities):

    // Tests if a bow can be built successfully
    @Test
    public void testSuccessfulBuildBow() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("build_bow", "Peaceful");
        List<ItemResponse> inv;
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.DOWN);
        newDungeon.tick(null, Direction.LEFT);
        inv = newDungeon.build("bow").getInventory();
        assertTrue(inv.stream().anyMatch(itm -> itm.getType().equals("bow")));
    }

    // Tests if a shield can be built successfully 
    @Test
    public void testSuccessfulBuildShield() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("build_shield", "Peaceful");
        List<ItemResponse> inv;
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.DOWN);
        newDungeon.tick(null, Direction.LEFT);
        inv = newDungeon.build("shield").getInventory();
        assertTrue(inv.stream().anyMatch(itm -> itm.getType().equals("shield")));
    }

    // Tests if InvalidActionException is raised for bow (insufficient materials)
    @Test
    public void testUnsuccessfulBuildBow() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("build_bow", "Peaceful");
        newDungeon.tick(null, Direction.RIGHT);
        assertThrows(InvalidActionException.class, () -> newDungeon.build("bow"));
    }

    // Tests if InvalidActionException is raised for shield (insufficient materials)
    @Test
    public void testUnsuccessfulBuildShield() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("build_shield", "Peaceful");
        newDungeon.tick(null, Direction.RIGHT);
        assertThrows(InvalidActionException.class, () -> newDungeon.build("shield"));
    }

    // Tests if IllegalArgumentException is raised if input is not shield or bow
    @Test
    public void testInvalidBuild() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("build_bow", "Peaceful");
        newDungeon.tick(null, Direction.RIGHT);
        assertThrows(IllegalArgumentException.class, () -> newDungeon.build("invalid"));
    }

}
