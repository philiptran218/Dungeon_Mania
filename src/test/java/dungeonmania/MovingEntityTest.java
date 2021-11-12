package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class MovingEntityTest {
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

    // ************************************************************ \\ 
    //                Test for Player Basic Movements
    // ************************************************************ \\ 

    // Test basic player movements
    @Test
    public void testBasicPlayerMovements() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse response;
        // Create new game
        DungeonResponse createNew = controller.newGame("basic_player_move", "peaceful");
        // Get id of player:
        String playerId = getEntityId(new Position(1, 1, 3), createNew);

        // Moving the player:
        response = controller.tick(null, Direction.RIGHT);
        assertTrue(isEntityOnTile(response, new Position(2, 1), playerId));

        response = controller.tick(null, Direction.RIGHT);
        assertTrue(isEntityOnTile(response, new Position(3, 1), playerId));
        
        response = controller.tick(null, Direction.DOWN);
        assertTrue(isEntityOnTile(response, new Position(3, 2), playerId));

        response = controller.tick(null, Direction.DOWN);
        assertTrue(isEntityOnTile(response, new Position(3, 3), playerId));

        response = controller.tick(null, Direction.LEFT);
        assertTrue(isEntityOnTile(response, new Position(2, 3), playerId));

        response = controller.tick(null, Direction.LEFT);
        assertTrue(isEntityOnTile(response, new Position(1, 3), playerId));

        response = controller.tick(null, Direction.UP);
        assertTrue(isEntityOnTile(response, new Position(1, 2), playerId));

        // Back to where I started:
        response = controller.tick(null, Direction.UP);
        assertTrue(isEntityOnTile(response, new Position(1, 1), playerId));
    }

    // Test stationary movement:
    @Test
    public void testStationaryPlayerMovement() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse response;
        // Create new game
        DungeonResponse createNew = controller.newGame("basic_player_move", "peaceful");
        // Get id of player:
        String playerId = getEntityId(new Position(1, 1, 3), createNew);

        // Stationary movement:
        response = controller.tick(null, Direction.NONE);

        // Should be at the same position:
        assertTrue(isEntityOnTile(response, new Position(1, 1), playerId));
    }

    // Test player moving into a wall:
    @Test
    public void testPlayerMovingIntoWall() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse response = controller.newGame("player_wall_interaction", "peaceful");
        // Get id of player:
        String playerId = getEntityId(new Position(1, 1, 3), response);

        // Moving the player into walls should remain stationary:
        response = controller.tick(null, Direction.UP);
        assertTrue(isEntityOnTile(response, new Position(1, 1), playerId));

        response = controller.tick(null, Direction.RIGHT);
        assertTrue(isEntityOnTile(response, new Position(1, 1), playerId));

        response = controller.tick(null, Direction.DOWN);
        assertTrue(isEntityOnTile(response, new Position(1, 1), playerId));

        response = controller.tick(null, Direction.LEFT);
        assertTrue(isEntityOnTile(response, new Position(1, 1), playerId));
    }

    // ************************************************** \\ 
    //          Test for Player Exit Interaction
    // ************************************************** \\ 


    // Test for player boulder interaction:
    @Test
    public void testPlayerInteractionOneBoulder() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse response;
        // Create new game
        DungeonResponse createNew = controller.newGame("play_boulder_interaction", "peaceful");
        // Get id of player:
        String playerId = getEntityId(new Position(1, 1, 3), createNew);

        // Move player down one:
        response = controller.tick(null, Direction.DOWN);
        assertTrue(isEntityOnTile(response, new Position(1, 2), playerId));

        // Push the first boulder:
        response = controller.tick(null, Direction.DOWN);
        assertTrue(isEntityOnTile(response, new Position(1, 3), playerId));

        // Atresponsets to push the second boulder, but since player is not strong enough
        // will remian stationary at the same position.
        response = controller.tick(null, Direction.DOWN);
        assertTrue(isEntityOnTile(response, new Position(1, 3), playerId));
    }

    // ************************************************************ \\ 
    //               Test for Spider Basic Movements
    // ************************************************************ \\ 
    @Test
    public void testNormalSpiderMovement () {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse response = controller.newGame("spider", "peaceful");
        // Get id of player:
        String spiderId = getEntityId(new Position(3, 2, 3), response);
        assertTrue(isEntityOnTile(response, new Position(3, 2), spiderId));
        response = controller.tick(null, Direction.UP);
        assertTrue(isEntityOnTile(response, new Position(3, 1), spiderId));
        response = controller.tick(null, Direction.UP);
        assertTrue(isEntityOnTile(response, new Position(4, 1), spiderId));
        response = controller.tick(null, Direction.UP);
        assertTrue(isEntityOnTile(response, new Position(4, 2), spiderId));
        response = controller.tick(null, Direction.UP);
        assertTrue(isEntityOnTile(response, new Position(4, 3), spiderId));
        response = controller.tick(null, Direction.UP);
        assertTrue(isEntityOnTile(response, new Position(3, 3), spiderId));
        response = controller.tick(null, Direction.UP);
        assertTrue(isEntityOnTile(response, new Position(2, 3), spiderId));
        response = controller.tick(null, Direction.UP);
        assertTrue(isEntityOnTile(response, new Position(2, 2), spiderId));
        response = controller.tick(null, Direction.UP);
        assertTrue(isEntityOnTile(response, new Position(2, 1), spiderId));
        response = controller.tick(null, Direction.UP);
        assertTrue(isEntityOnTile(response, new Position(3, 1), spiderId));
    }

    @Test
    public void testBoulderSpiderMovement () {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse response = controller.newGame("spiderBoulder", "peaceful");
        // Get id of player:
        String spiderId = getEntityId(new Position(3, 2, 3), response);
        assertTrue(isEntityOnTile(response, new Position(3, 2), spiderId));
        response = controller.tick(null, Direction.UP);
        assertTrue(isEntityOnTile(response, new Position(3, 1), spiderId));
        response = controller.tick(null, Direction.UP);
        assertTrue(isEntityOnTile(response, new Position(2, 1), spiderId));
        response = controller.tick(null, Direction.UP);
        assertTrue(isEntityOnTile(response, new Position(2, 2), spiderId));
        response = controller.tick(null, Direction.UP);
        assertTrue(isEntityOnTile(response, new Position(2, 3), spiderId));
        response = controller.tick(null, Direction.UP);
        assertTrue(isEntityOnTile(response, new Position(3, 3), spiderId));
        response = controller.tick(null, Direction.UP);
        assertTrue(isEntityOnTile(response, new Position(4, 3), spiderId));
        response = controller.tick(null, Direction.UP);
        assertTrue(isEntityOnTile(response, new Position(4, 2), spiderId));
        response = controller.tick(null, Direction.UP);
        assertTrue(isEntityOnTile(response, new Position(4, 3), spiderId));
    }

    // ************************************************************ \\ 
    //               Test for Zombie Basic Movements
    // ************************************************************ \\ 
    
    @Test
    public void testNormalZombieMovement () {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse response = controller.newGame("zombieMovement", "peaceful");
        // Get id of player:
        String zombieId = getEntityId(new Position(2, 2, 3), response);
        response = controller.tick(null, Direction.UP);
        assertTrue(isEntityOnTile(response, new Position(2, 1), zombieId) 
                                    || isEntityOnTile(response, new Position(1, 2), zombieId)
                                    || isEntityOnTile(response, new Position(2, 2), zombieId));  
    }

    @Test
    public void testBiggerZombieMovement () {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse response = controller.newGame("zombieMovementBigger", "standard");
        // Get id of player:
        String zombieId = getEntityId(new Position(2, 2, 3), response);
        response = controller.tick(null, Direction.UP);
        assertTrue(isEntityOnTile(response, new Position(2, 1), zombieId) 
                                    || isEntityOnTile(response, new Position(1, 2), zombieId)
                                    || isEntityOnTile(response, new Position(2, 2), zombieId)); 
    }
    // ************************************************************ \\ 
    //               Test for Mercenary Basic Movements
    // ************************************************************ \\ 

    /**
     * Test bribing a mercenary with a sun stone
     */
    @Test
    public void testBribeMercenaryWithSunStone() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse response = controller.newGame("simpleMercWithSunStone", "Standard");
        String MercId = getEntityId(new Position(4, 1, 3), response);
        assertTrue(":enemies".equals(response.getGoals()));
        controller.tick(null, Direction.RIGHT);
        response = controller.interact(MercId);
        assertTrue("".equals(response.getGoals()));
        // Check that sun stone has been retained
        assertTrue(response.getInventory().stream().anyMatch(e -> e.getType().equals("sun_stone")));
    }

    @Test
    public void testMercenaryTooFarAwayToBribe() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse response = controller.newGame("mercenary", "standard");
        String MercId = getEntityId(new Position(6, 1, 3), response);
        response = controller.tick(null, Direction.RIGHT);
        assertThrows(InvalidActionException.class, () -> controller.interact(MercId));
    }

    @Test
    public void testnoTreasureToBribeMercenary() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse response = controller.newGame("mercenary", "standard");
        String MercId = getEntityId(new Position(6, 1, 3), response);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.RIGHT);
        response = controller.tick(null, Direction.RIGHT);
        assertThrows(InvalidActionException.class, () -> controller.interact(MercId));
    }

    @Test
    public void testBribedMercenaryMovement() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse response = controller.newGame("allyMercMovement", "standard");
        // Get id of player:
        String mercId = getEntityId(new Position(4, 1, 3), response);
        controller.tick(null, Direction.RIGHT);
        controller.interact(mercId);
        response = controller.tick(null, Direction.LEFT);
        assertTrue(isEntityOnTile(response, new Position(2, 1), mercId));  
    }

    @Test
    public void testBribeMercenarySceptre() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse response = controller.newGame("bribeMercSceptre", "standard");
        String mercId = getEntityId(new Position(10, 1, 3), response);
        // Move to collect the items
        for (int i = 0; i < 4; i++) {
            controller.tick(null, Direction.RIGHT);
        }
        // Build the sceptre
        controller.build("sceptre");
        // Once mercenary is bribed with sceptre, player should not be able to kill him for 10 ticks
        controller.interact(mercId);
        for (int i = 0; i < 5; i++) {
            controller.tick(null, Direction.RIGHT);
            controller.tick(null, Direction.LEFT);
            assertTrue(response.getEntities().stream().anyMatch(e -> e.getType().equals("mercenary")));
        }
        // This tick should battle mercenary, since effect has worn off
        response = controller.tick(null, Direction.RIGHT);
        // Mercenary should have died
        assertFalse(response.getEntities().stream().anyMatch(e -> e.getType().equals("mercenary")));
    }

    @Test
    public void testAssassinTooFarAwayToBribe() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse response = controller.newGame("bribeAssassin", "standard");
        String assinId = getEntityId(new Position(8, 1, 3), response);
        // Collect items to bribe
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        // Try to bribe assassin 3 tiles away
        assertThrows(InvalidActionException.class, () -> controller.interact(assinId));
    }

    @Test
    public void testBribeAssassinNoTreasure() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse response = controller.newGame("bribeAssassinNoTreasure", "standard");
        String assinId = getEntityId(new Position(4, 1, 3), response);
        controller.tick(null, Direction.RIGHT);
        
        // Try to bribe assassin with only one ring (no treasure)
        assertThrows(InvalidActionException.class, () -> controller.interact(assinId));
    }

    @Test
    public void testBribeAssassinNoRing() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse response = controller.newGame("bribeAssassinNoRing", "standard");
        String assinId = getEntityId(new Position(4, 1, 3), response);
        controller.tick(null, Direction.RIGHT);
        
        // Try to bribe assassin with only treasure (no ring)
        assertThrows(InvalidActionException.class, () -> controller.interact(assinId));
    }

    @Test
    public void testBribeAssassinWithRingTreasure() {
        // Create controller
        DungeonManiaController controller = new DungeonManiaController();
        // Create new game
        DungeonResponse response = controller.newGame("bribeAssassin", "standard");
        String assinId = getEntityId(new Position(8, 1, 3), response);
        // Collect items to bribe
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        assertTrue(":enemies".equals(response.getGoals()));
        // Successfully bribe assassin
        response = controller.interact(assinId);
        // Check goals for enemies (should be empty)
        assertTrue("".equals(response.getGoals()));
        // Try to bribe again (should do nothing)
        controller.interact(assinId);
    }

    @Test
    public void testBribeAssassinWithRingSunStone() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse response = controller.newGame("bribeAssassinSunStone", "standard");
        String assinId = getEntityId(new Position(6, 1, 3), response);
        // Collect items to bribe
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        assertTrue(":enemies".equals(response.getGoals()));
        // Successfully bribe assassin
        response = controller.interact(assinId);
        // Check goals for enemies (should be empty)
        assertTrue("".equals(response.getGoals()));
        // Sun stone should be retained
        assertTrue(response.getInventory().stream().anyMatch(e -> e.getType().equals("sun_stone")));
    }

    @Test
    public void testBribeAssassinSceptre() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse response = controller.newGame("bribeAssassinSceptre", "standard");
        String assinId = getEntityId(new Position(6, 1, 3), response);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
    
        // Once assassin is bribed with sceptre, player should not be able to kill him for 10 ticks
        controller.interact(assinId);
        for (int i = 0; i < 5; i++) {
            controller.tick(null, Direction.RIGHT);
            controller.tick(null, Direction.LEFT);
            assertTrue(response.getEntities().stream().anyMatch(e -> e.getType().equals("assassin")));
        }
        // This tick should battle assassin, since effect has worn off
        response = controller.tick(null, Direction.RIGHT);
        // Assassin should have died
        assertFalse(response.getEntities().stream().anyMatch(e -> e.getType().equals("assassin")));
    }
}
