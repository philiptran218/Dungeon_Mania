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
    // TODO: change this test to add new collectables
    @Test
    public void testCollectablePickup() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("player_pickup_item", "peaceful");
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

        inv = newDungeon.tick(null, Direction.RIGHT).getInventory();
        assertTrue(inv.stream().anyMatch(itm -> itm.getType().equals("sun_stone")));

        inv = newDungeon.tick(null, Direction.RIGHT).getInventory();
        assertTrue(inv.stream().anyMatch(itm -> itm.getType().equals("anduril")));
    }

    // Test that the collected item can be used via tick() in DungeonManiaController
    @Test
    public void testUsedCollectable() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("player_pickup_item", "peaceful");

        DungeonResponse response = newDungeon.tick(null, Direction.RIGHT);

        String healthId = getEntityId(new Position(2, 2, 2), response);
        String invincibleId = getEntityId(new Position(1, 2, 2), response);
        String invisibleId = getEntityId(new Position(1, 3, 2), response);
        String bombId = getEntityId(new Position(2, 3, 2), response);

        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.DOWN);
        newDungeon.tick(null, Direction.LEFT);
        newDungeon.tick(null, Direction.LEFT);
        newDungeon.tick(null, Direction.LEFT);
        newDungeon.tick(null, Direction.DOWN);
        response = newDungeon.tick(null, Direction.RIGHT);

        response = newDungeon.tick(healthId, null);
        assertFalse(response.getInventory().stream().anyMatch(itm -> itm.getType().equals("health_potion")));
        response = newDungeon.tick(invincibleId, null);
        assertFalse(response.getInventory().stream().anyMatch(itm -> itm.getType().equals("invincibility_potion")));
        response = newDungeon.tick(invisibleId, null);
        assertFalse(response.getInventory().stream().anyMatch(itm -> itm.getType().equals("invisibility_potion")));
        response = newDungeon.tick(bombId, null);
        assertFalse(response.getInventory().stream().anyMatch(itm -> itm.getType().equals("bomb")));
    }


    // Tests using an item of an unaccepted type
    @Test
    public void testUnusableItem() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        DungeonResponse response = newDungeon.newGame("simpleTreasure", "peaceful");
        String treasure = getEntityId(new Position(4, 1, 2), response);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        assertThrows(IllegalArgumentException.class, () -> newDungeon.tick(treasure, null));
    }
        
    // Tests for BuildableEntities (included in CollectableEntities):

    // Tests if a bow can be built successfully
    @Test
    public void testSuccessfulBuildBow() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("build_bow", "peaceful");
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
    public void testSuccessfulBuildShieldTreasure() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("build_shield", "peaceful");
        List<ItemResponse> inv;
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.DOWN);
        newDungeon.tick(null, Direction.LEFT);
        inv = newDungeon.build("shield").getInventory();
        assertTrue(inv.stream().anyMatch(itm -> itm.getType().equals("shield")));
    }

    @Test
    public void testSuccessfulBuildShieldKey() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("build_shield_key", "peaceful");
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
        newDungeon.newGame("build_bow", "peaceful");
        newDungeon.tick(null, Direction.RIGHT);
        assertThrows(InvalidActionException.class, () -> newDungeon.build("bow"));
    }

    // Tests if InvalidActionException is raised for shield (insufficient materials)
    @Test
    public void testUnsuccessfulBuildShield() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("build_shield", "peaceful");
        newDungeon.tick(null, Direction.RIGHT);
        assertThrows(InvalidActionException.class, () -> newDungeon.build("shield"));
    }

    // Tests if IllegalArgumentException is raised if input is not shield or bow
    @Test
    public void testInvalidBuild() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("build_bow", "peaceful");
        newDungeon.tick(null, Direction.RIGHT);
        assertThrows(IllegalArgumentException.class, () -> newDungeon.build("invalid"));
    }

    // Tests building a sceptre with wood and treasure
    @Test
    public void testSuccessfulBuildSceptreWoodTreasure() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("build_sceptre_wood_treasure", "Peaceful");
        // Test building with no materials first
        assertThrows(InvalidActionException.class, () -> newDungeon.build("sceptre"));

        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        List<ItemResponse> inv = newDungeon.build("sceptre").getInventory();
        assertTrue(inv.stream().anyMatch(itm -> itm.getType().equals("sceptre")));
    }

    // Tests building a sceptre with arrow and key
    @Test
    public void testSuccessfulBuildSceptreArrowKey() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("build_sceptre_arrow_key", "Peaceful");
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        List<ItemResponse> inv = newDungeon.build("sceptre").getInventory();
        assertTrue(inv.stream().anyMatch(itm -> itm.getType().equals("sceptre")));
    }

    // Tests successfully building a midnight armour (no zombies)
    @Test
    public void testSuccessfulBuildMidnightArmour() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("build_midnight_armour", "Peaceful");
        // Test building with no materials first
        assertThrows(InvalidActionException.class, () -> newDungeon.build("midnight_armour"));

        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        List<ItemResponse> inv = newDungeon.build("midnight_armour").getInventory();
        assertTrue(inv.stream().anyMatch(itm -> itm.getType().equals("midnight_armour")));
    }

    // Tests building midnight armour while zombie is present
    @Test
    public void testBuildMidnightArmourZombie() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        newDungeon.newGame("build_midnight_armour_zombie", "Peaceful");
        newDungeon.tick(null, Direction.RIGHT);
        newDungeon.tick(null, Direction.RIGHT);
        assertThrows(InvalidActionException.class, () -> newDungeon.build("midnight_armour"));
        List<ItemResponse> inv = newDungeon.tick(null, Direction.RIGHT).getInventory();
        // Midnight armour shouldn't be in inventory
        assertFalse(inv.stream().anyMatch(itm -> itm.getType().equals("midnight_armour")));
    }

    // Test for invincible potion
    @Test
    public void testInviniciblePotionZombie() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        DungeonResponse response = newDungeon.newGame("invincibleWithZombie", "standard");
        String zombieId = getEntityId(new Position(3, 1, 3), response);
        String potionId = getEntityId(new Position(7, 1, 2), response);
        newDungeon.tick(null, Direction.LEFT);
        newDungeon.tick(potionId,null);
        newDungeon.tick(null, Direction.LEFT);
        response = newDungeon.tick(null, Direction.LEFT);
        assertTrue(isEntityOnTile(response, new Position(1, 1), zombieId));
    }

    @Test
    public void testInviniciblePotionMerc() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        DungeonResponse response = newDungeon.newGame("invincibleWithMerc", "standard");
        String mercId = getEntityId(new Position(3, 1, 3), response);
        String potionId = getEntityId(new Position(7, 1, 2), response);
        newDungeon.tick(null, Direction.LEFT);
        newDungeon.tick(potionId,null);
        newDungeon.tick(null, Direction.LEFT);
        response = newDungeon.tick(null, Direction.LEFT);
        assertTrue(isEntityOnTile(response, new Position(1, 1), mercId));
    }

    @Test
    public void testInviniciblePotionSpider() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        DungeonResponse response = newDungeon.newGame("invincibleWithSpider", "standard");
        String spiderId = getEntityId(new Position(3, 1, 3), response);
        String potionId = getEntityId(new Position(7, 1, 2), response);
        newDungeon.tick(null, Direction.LEFT);
        newDungeon.tick(potionId, null);
        newDungeon.tick(null, Direction.LEFT);
        response = newDungeon.tick(null, Direction.LEFT);
        assertTrue(isEntityOnTile(response, new Position(2, 0), spiderId));
    }
    @Test
    public void testUseTreasure() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        DungeonResponse response = newDungeon.newGame("mercenary", "peaceful");
        String treasure = getEntityId(new Position(2, 1, 2), response);
        newDungeon.tick(null, Direction.RIGHT);
        assertThrows(IllegalArgumentException.class, () -> newDungeon.tick(treasure, null));
    }
    @Test
    public void testItemNotInInventory() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        DungeonResponse response = newDungeon.newGame("advanced", "peaceful");
        String invinc = getEntityId(new Position(11, 10, 2), response);
        assertThrows(InvalidActionException.class, () -> newDungeon.tick(invinc, null));
    }
    @Test
    public void testEntityExists() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        DungeonResponse response = newDungeon.newGame("advanced", "peaceful");
        String entity = getEntityId(new Position(2, 2, 2), response);
        assertThrows(IllegalArgumentException.class, () -> newDungeon.interact(entity));
    }
    @Test
    public void testEntityNotInteractable() {
        DungeonManiaController newDungeon = new DungeonManiaController();
        DungeonResponse response = newDungeon.newGame("advanced", "peaceful");
        String entity = getEntityId(new Position(0, 2, 1), response);
        assertThrows(IllegalArgumentException.class, () -> newDungeon.interact(entity));
    }
}
