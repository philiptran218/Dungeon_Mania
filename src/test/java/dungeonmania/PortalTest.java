package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import dungeonmania.MovingEntities.Player;
import dungeonmania.StaticEntities.Portal;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;


public class PortalTest {
    // Test helper: Checks if entity on a given position.
    public boolean isEntityOnTile(DungeonResponse response, Position pos, String id) {
        for (EntityResponse entity : response.getEntities()) {
            if (entity.getId() == id) {
                boolean x = (pos.getX() == entity.getPosition().getX());
                boolean y = (pos.getY() == entity.getPosition().getY());
                if (x && y) {
                    return true;
                }
            }
        }
        return false;
    }

    // Get the player:
    public String getPlayer(List<EntityResponse> eList) {
        for (EntityResponse e : eList) {
            if (e.getType().equals("player")) {
                return e.getId();
            }
        }
        return null;
    }

    // Test for saving portals of different colours
    @Test
    public void testColourPortals() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse tmp;
        // Create new game
        DungeonResponse r = controller.newGame("portal_test_big", "Peaceful");
        String id = getPlayer(r.getEntities());

        // Movements around the portal:
        controller.tick(null, Direction.DOWN);
        tmp = controller.tick(null, Direction.RIGHT);
        assertTrue(isEntityOnTile(tmp, new Position(6, 2), id));

        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);

        tmp = controller.tick(null, Direction.LEFT);
        assertTrue(isEntityOnTile(tmp, new Position(1, 4), id));

        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);

        tmp = controller.tick(null, Direction.RIGHT);
        assertTrue(isEntityOnTile(tmp, new Position(6, 6), id));

        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);

        tmp = controller.tick(null, Direction.LEFT);
        assertTrue(isEntityOnTile(tmp, new Position(1, 8), id));
    }

    @Test
    public void testColourConversion() {
        assertEquals(Portal.colourToId("not valud"), 0);
        assertEquals(Portal.getPortalColour(-1), null);
    }

}
