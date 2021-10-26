package dungeonmania;

import org.junit.jupiter.api.Test;

public class CollectableEntityTest {

    // Testing ideas for actual CollectableEntities:

    // Test 1: check that the entity can be picked up by the player.
    // Condition: should appear in the player's inventory
    @Test
    public void testCollectablePickup() {
        // Stub...

    }

    // Test 2: check that armour/oneRing is obtained from enemy after battle
    // Condition: should appear in the player's inventory
    @Test
    public void testBattlePickups() {
        // Stub...
    }

    // Test 3: check that entity is successfully removed after being used
    // Condition: should not appear in player's inventory
    @Test
    public void testUsedCollectable() {
        // Stub...
    }

    // Test 4: test that each entity performs their intended functions
    // Condition: treasure/wood/arrow/key - for crafting
    //            key - opening correct door
    //            potions - provides special effects for the player
    //            bomb - explodes when placed on a switch
    //            sword - provides additional atk damage for player
    //            armour - halves damage received from enemy
    //            theOneRing - revives the player to full hp upon death
    @Test
    public void testCollectableFunctionality() {
        // Stub...
    }

    // SOME ASSUMPTIONS FOR COLLECTABLEENTITIES:
    // 1. Player can collect an unlimited number of entities (with exception of key)
    // 2. Maybe limit the number of armour/sword the player could have...
    // 3. TheOneRing is used immediately once the player dies (they are not given a choice).


    // SOME NOTES FOR COLLECTABLEENTITIES:
    // - armour and theOneRing is added to inventory after defeating an enemy with it
    // - key disappears completely after being used (does not get melted/cannot be used to craft)
    
    

    // Some testing ideas for BuildableEntities (included in CollectableEntities):

    // Test 1: check that the entity can be built if the user has sufficient materials
    // Condition: check that the entity has been successfully added to inventory
    // Condition: check that materials have been removed from inventory
    @Test
    public void testSuccessfulBuild() {
        // Stub...
    }

    // Test 2: check that entity is not built if user has insufficent materials
    // Condition: check that entity has not been added to inventory
    // Condition: check that materials are still in inventory
    @Test
    public void testUnsuccessfulBuild() {
        // Stub...
    }

    // Test 3: check that entities are performing correct functions in combat
    // Condition: Bow - attacks twice   Shield - reduces damage received
    @Test
    public void testBuildableFunctionality() {
        // Stub...
    }

    // Test 4: check that entity durability is being worn out by combat
    // Condition: Bow/shield durability decreases if used, removed once it reaches 0.
    @Test
    public void testDurability() {
        // Stub...
    }

    // Test 5 (assumption): check that a second bow/shield replaces the current bow/shield.
    // Condition: new bow/arrow should have full durability and only have 1 in inventory
    @Test 
    public void testBuildableLimit() {
        // Stub...
    }

    // SOME ASSUMPTIONS TO POSSIBLY ADD:
    // 1. Player can only hold 1 bow and 1 shield at a time. Creating another bow/shield discards
    // the currently existing bow/shield. (Prevents unrealistic combat)
    // 2. Shield/bow is used automatically in combat. Player is not given an option to use it
    // or not.

    // IDEAS FOR COMBAT:
    // Player has a method which returns the amount of damage they deal
    // Player has a method which takes in the amount of damage they receive and calculates
    // remaining health.
    // Enemies will have similar methods to calculate dealing/receiving battle damage.

    // Player vs enemy scenario:
    // enemy.recDamage(player.getDamage(), player.getHealth()); --> takes in player's attack and
    // current health to calculate enemy's current health after battle.
    // Similarly, player.recDamage(enemy.getDamage(), enemy.getHealth());
    // recDamage() also takes into account shield/armour to reduce damage received.

}
