package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class LogicEntityTest {
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

    // Gets the entity on a position:
    public EntityResponse getEntity(Position pos, DungeonResponse response, String type) {
        for (EntityResponse entity : response.getEntities()) {
            if (entity.getPosition().equals(pos) && entity.getPosition().getLayer() == pos.getLayer()
                && entity.getType().equals(type)) {
                return entity;
            }
        }
        return null;
    }

    @Test
    public void testNoLogicLightBulb() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        controller.newGame("light_bulb", "peaceful");
        // Move player to the right, pushing boulder onto a plate
        DungeonResponse response = controller.tick(null, Direction.RIGHT);
        assertTrue(getEntity(new Position(3, 5, 0), response, "light_bulb_on") != null);
    }

    @Test
    public void testOrLogicLightBulb() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        controller.newGame("light_bulb", "peaceful");
        // Move player to the right, pushing boulder onto a plate
        DungeonResponse response = controller.tick(null, Direction.RIGHT);
        assertTrue(getEntity(new Position(1, 5, 0), response, "light_bulb_on") != null);
    }

    @Test
    public void testAndLogicLightBulb() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        controller.newGame("light_bulb", "peaceful");
        // Move player to the right, pushing boulder onto a plate
        DungeonResponse response = controller.tick(null, Direction.RIGHT);
        assertTrue(getEntity(new Position(5, 5, 0), response, "light_bulb_on") != null);
        assertTrue(getEntity(new Position(2, 5, 0), response, "light_bulb_off") != null);
    }

    @Test
    public void testXorLogicLightBulb() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        controller.newGame("light_bulb", "peaceful");
        // Move player to the right, pushing boulder onto a plate
        DungeonResponse response = controller.tick(null, Direction.RIGHT);
        assertTrue(getEntity(new Position(7, 5, 0), response, "light_bulb_off") != null);
    }

    @Test
    public void testNotLogicLightBulb() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        controller.newGame("light_bulb", "peaceful");
        DungeonResponse response = controller.tick(null, Direction.LEFT);
        assertTrue(getEntity(new Position(9, 5, 0), response, "light_bulb_on") != null);
        assertTrue(getEntity(new Position(9, 7, 0), response, "light_bulb_on") != null);
        controller.tick(null, Direction.RIGHT);
        // Move player to the right, pushing boulder onto a plate
        response = controller.tick(null, Direction.RIGHT);
        assertTrue(getEntity(new Position(9, 5, 0), response, "light_bulb_off") != null);
        assertTrue(getEntity(new Position(9, 7, 0), response, "light_bulb_on") != null);
    }

    @Test
    public void testNoLogicSwitch() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        controller.newGame("floorSwitchChain", "peaceful");
        DungeonResponse response = controller.tick(null, Direction.RIGHT);
        assertTrue(getEntity(new Position(5, 2, 0), response, "light_bulb_off") != null);
    }

    @Test
    public void testOrLogicSwitch() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        controller.newGame("floorSwitchChain", "peaceful");
        DungeonResponse response = controller.tick(null, Direction.RIGHT);
        assertTrue(getEntity(new Position(5, 0, 0), response, "light_bulb_on") != null);
    }

    @Test
    public void testAndLogicSwitch() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        controller.newGame("floorSwitchChain", "peaceful");
        DungeonResponse response = controller.tick(null, Direction.RIGHT);
        assertTrue(getEntity(new Position(5, 4, 0), response, "light_bulb_off") != null);
    }

    @Test
    public void testXorLogicSwitch() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        controller.newGame("floorSwitchChain", "peaceful");
        DungeonResponse response = controller.tick(null, Direction.RIGHT);
        assertTrue(getEntity(new Position(5, 6, 0), response, "light_bulb_on") != null);
    }

    @Test
    public void testNotLogicSwitch() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        controller.newGame("floorSwitchChain", "peaceful");
        DungeonResponse response = controller.tick(null, Direction.LEFT);
        assertTrue(getEntity(new Position(5, 10, 0), response, "light_bulb_on") != null);
        response = controller.tick(null, Direction.RIGHT);
        assertTrue(getEntity(new Position(5, 10, 0), response, "light_bulb_off") != null);
    }

    @Test
    public void testWireLoop() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        controller.newGame("wireloop", "peaceful");
        DungeonResponse response = controller.tick(null, Direction.RIGHT);
        assertTrue(getEntity(new Position(5, 1, 0), response, "bomb") == null);
    }

    @Test
    public void testNoLogicSwitchDoor() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        controller.newGame("switchDoors", "peaceful");
        DungeonResponse response = controller.tick(null, Direction.RIGHT);
        assertTrue(getEntity(new Position(4, 2, 0), response, "switch_door_unlocked") != null);
    }

    @Test
    public void testOrLogicSwitchDoor() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        controller.newGame("switchDoors", "peaceful");
        DungeonResponse response = controller.tick(null, Direction.RIGHT);
        assertTrue(getEntity(new Position(4, 0, 0), response, "switch_door_unlocked") != null);
    }

    @Test
    public void testAndLogicSwitchDoor() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        controller.newGame("switchDoors", "peaceful");
        DungeonResponse response = controller.tick(null, Direction.RIGHT);
        assertTrue(getEntity(new Position(4, 4, 1), response, "switch_door") != null);
        assertTrue(getEntity(new Position(4, 4, 0), response, "switch_door_unlocked") == null);
    }

    @Test
    public void testXorLogicSwitchDoor() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        controller.newGame("switchDoors", "peaceful");
        DungeonResponse response = controller.tick(null, Direction.RIGHT);
        assertTrue(getEntity(new Position(4, 6, 0), response, "switch_door_unlocked") != null);
    }

    @Test
    public void testNotLogicSwitchDoor() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        controller.newGame("switchDoors", "peaceful");
        DungeonResponse response = controller.tick(null, Direction.LEFT);
        assertTrue(getEntity(new Position(4, 10, 0), response, "switch_door_unlocked") != null);
        response = controller.tick(null, Direction.RIGHT);
        assertTrue(getEntity(new Position(4, 10, 1), response, "switch_door") != null);
    }

    @Test
    public void testNoLogicBomb() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        controller.newGame("logicBomb", "peaceful");
        DungeonResponse response = controller.tick(null, Direction.RIGHT);
        assertTrue(getEntity(new Position(4, 2, 2), response, "bomb") != null);
    }

    @Test
    public void testOrLogicBomb() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        controller.newGame("logicBomb", "peaceful");
        DungeonResponse response = controller.tick(null, Direction.RIGHT);
        assertTrue(getEntity(new Position(4, 0, 2), response, "bomb") == null);
    }

    @Test
    public void testAndLogicBomb() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        controller.newGame("logicBomb", "peaceful");
        DungeonResponse response = controller.tick(null, Direction.RIGHT);
        assertTrue(getEntity(new Position(4, 4, 2), response, "bomb") != null);
    }

    @Test
    public void testXorLogicBomb() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        controller.newGame("logicBomb", "peaceful");
        DungeonResponse response = controller.tick(null, Direction.RIGHT);
        assertTrue(getEntity(new Position(4, 6, 2), response, "bomb") == null);
    }
}
