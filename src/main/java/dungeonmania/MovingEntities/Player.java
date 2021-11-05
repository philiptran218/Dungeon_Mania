package dungeonmania.MovingEntities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.util.Position;
import dungeonmania.util.Direction;

import dungeonmania.Entity;
import dungeonmania.Inventory;
import dungeonmania.Battles.Battle;

import dungeonmania.CollectableEntities.*;
import dungeonmania.StaticEntities.*;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.ItemResponse;


public class Player extends MovingEntity implements MovingEntitySubject {
    private List<MovingEntityObserver> listObservers = new ArrayList<MovingEntityObserver>();
    private List<Mercenary> bribedMercenaries = new ArrayList<Mercenary>();
    private Inventory inventory = new Inventory(this);
    private Battle battle;
    private int invisDuration = 0;
    private int invincDuration = 0;
    private List<String> useableItems = Arrays.asList("bomb", "health_potion", "invincibility_potion", "invisibility_potion", null);

    /**
     * Constructor for the player.
     * @param id
     * @param type
     * @param pos
     * @param battle
     */
    public Player(String id, String type, Position pos){
        super(id, type, pos, 20, 2);
    }

    // ********************************************************************************************\\
    //                                         Functions                                           \\
    // ********************************************************************************************\\

    // Polymorphism
    public void move(Map<Position, List<Entity>> map) {}

    /**
     * Given a direction to move in, the player moves in that direction. Checks 
     * if the player can move on that position.
     * @param map
     * @param direction
     */
    public void move(Map<Position, List<Entity>> map, Direction direction) {
        Position newPos = super.getPos().translateBy(direction);    
        Position doorLayer = newPos.asLayer(1);

        if (canPass(map, newPos)) {
            moveInDir(map, direction);
        } else if (canPush(map, newPos, direction)) {   
            // Player can move, but pushes a boulder
            Boulder boulder = (Boulder) map.get(newPos.asLayer(1)).get(0);
            boulder.push(map, direction);
            moveInDir(map, direction);
        } else if (!map.get(doorLayer).isEmpty() && map.get(doorLayer).get(0).getType().equals("door")) {
            Entity e = map.get(doorLayer).get(0);
            // Check if the door and key matches:
            int keyId = ((Door) e).getKeyId();
            if (inventory.getKey(keyId) != null) {
                e.setType("door_unlocked");
                e.setPos(doorLayer.asLayer(-1));
                inventory.getKey(keyId).use();
                map.get(newPos.asLayer(-1)).add(e);
                // Remove the door on current layer and
                map.get(doorLayer).remove(e);
                moveInDir(map, direction);
            }
        } else if (canTeleport(map, newPos, direction)) {
            Portal portal = (Portal) map.get(newPos.asLayer(4)).get(0);
            portal.teleport(map, this, direction);
        }

        pickUp(map);
        notifyObservers();
    }

    /**
     * Checks if the player can step onto a give position.
     * @return True is the player can go onto the new position, false otherwise.
     */
    public boolean canPass(Map<Position, List<Entity>> map, Position pos) {
        List<Entity> entities = map.get(pos.asLayer(4));
        if (entities.size() == 1) {
            System.out.println(entities.get(0).getType().equals("door_unlocked"));
            return entities.get(0).getType().equals("door_unlocked");
        } else {
            return map.get(pos.asLayer(1)).isEmpty();
        }
    }

    /**
     * Checks if the player can push the entity infront.
     * @param map
     * @param pos
     * @param direction
     * @return True if can push, false otherwise.
     */
    public boolean canPush(Map<Position, List<Entity>> map, Position pos, Direction direction) {
        if (super.isPassingBoulder(map, pos)) {
            // Has boulder
            Boulder boulder = (Boulder) map.get(pos.asLayer(1)).get(0);
            return boulder.canBePushed(map, direction);
        }
        return false;
    }

    /**
     * Checks if the player is able to walk through the teleportor.
     */
    public boolean canTeleport(Map<Position, List<Entity>> map, Position pos, Direction direction) {
        List<Entity> entities = map.get(pos.asLayer(4));
        if (entities.size() == 0) {
            // Player not on portal
            return false;
        }
        Entity e = entities.get(0);
        if (e instanceof Portal) {
            // Is a portal, check if exiting position of exit portal can be exited from by the player
            Position exitPos = ((Portal) e).getTeleportPos(map, direction);
            return this.canPass(map, exitPos.asLayer(3));
        }
        return false;
    }

    /**
     * Player picks up all collectable items (CollectableEntity) on their current 
     * position and adds it to their inventory
     * @param map
     */
    public void pickUp(Map<Position, List<Entity>> map) {
        Position pos = super.getPos();
        List<Entity> collectables = map.get(new Position(pos.getX(), pos.getY(), 2));
        if (!collectables.isEmpty()) {
            Entity entity = collectables.get(0);
            if (entity.getType().equals("key") && inventory.getNoItemType("key") > 0) {
                // Entity is a key and player is already holding a key
                // Dont pick it up
            } else {
                // Pickup the item
                this.inventory.put(entity, this);
                collectables.remove(entity);

            }
        }
    }

    /**
     * Player uses items
     * @param map
     * @param itemUsed
     * @throws IllegalArgumentException
     */
    public void useItem(Map<Position, List<Entity>> map, String itemUsed) throws IllegalArgumentException, InvalidActionException {
        Entity item = inventory.getItemById(itemUsed);
        if (item == null) {
            // Player does not have the item
            throw new InvalidActionException("Player does not have the item.");
        }
        if (!useableItems.contains(item.getType())) {
            // Cannot use the item
            throw new IllegalArgumentException("Cannot use item.");
        }

        if (item.getType().equals("bomb")) {
            // Item is a bomb
            Bomb bomb = (Bomb) this.inventory.getItemById(itemUsed);
            map.get(super.getPos().asLayer(2)).add(bomb);
            bomb.setPos(super.getPos().asLayer(2));
        }
        this.inventory.getItemById(itemUsed).use();
    }

    /**
     * The player bribes the mercenary. The bribe will be successful if the player
     * has enough gold and is within 2 cardinal tiles from the mercenary
     * @param map 
     * @param mercenary mercenary that the player will try to bribe
     */
    public void bribeMercenary(Map<Position, List<Entity>> map, Mercenary mercenary) throws InvalidActionException{
        if (mercenary.isAlly()) {
            // mercenary is an ally, dont bribe him;
            return;
        }
        Position pos = super.getPos();
        Position mercenaryPos = mercenary.getPos();

        Position difference = Position.calculatePositionBetween(pos, mercenaryPos);
        if (Math.abs(difference.getX()) + Math.abs(difference.getY()) > 2) {
            // player more than 2 cardinal tiles from mercenary
            throw new InvalidActionException("Mercenary too far away");
        }

        if (inventory.getNoItemType("treasure") < mercenary.getPrice()) {
            // Player doesnt have enough gold
            throw new InvalidActionException("Player doesn't have enough gold");
        }


        // Remove gold;
        for (int i = 0; i < mercenary.getPrice(); i++) {
            if (inventory.getItem("treasure").getType().equals("treasure")) {
                ((Treasure) inventory.getItem("treasure")).use();
            }
        }
        // Successfully bribe mercenary
        mercenary.bribe();
        bribedMercenaries.add(mercenary);
    
    }

    /**
     * Player attacks a designated spawner on the map and the function 
     * checks if the player can destroy the spawner or not.
     * checks 
     * @param map
     * @param spawner
     */
    public void attackZombieSpawner(Map<Position, List<Entity>> map, ZombieToastSpawner spawner) {
        Position pos = super.getPos();
        Position spawnerPos = spawner.getPos();

        Position difference = Position.calculatePositionBetween(pos, spawnerPos);
        if (Math.abs(difference.getX()) + Math.abs(difference.getY()) > 1) {
            // player is not cardinally adjacent to spawner
            throw new InvalidActionException("player not cardinally adjacent to spawner");
        }
        
        if (inventory.getItem("sword") != null) {
            spawner.destroy(map);
            inventory.getItem("sword").use();
        } else if (inventory.getItem("bow") != null) {
            spawner.destroy(map);
            inventory.getItem("bow").use();
        } else {
            throw new InvalidActionException("player does not have a weapon");
        }
    }

    /**
     * Get the player inventory in the form: List<CollectableEntity>
     * @return List of entity in the player's inventory.
     */
    public List<CollectableEntity> getInventoryList() {
        return inventory.getInventory();
    }
    
    /**
     * Converts the player's inventory into a list of item response.
     * @return List<ItemResponse> List of ItemResponse.
     */
    public List<ItemResponse> getInventoryResponse() {
        List<ItemResponse> itemResponse = new ArrayList<>();
        // Loop through the player and adds his items to the lists
        for (CollectableEntity c : inventory.getInventory()) {
            itemResponse.add(new ItemResponse(c.getId(), c.getType()));
        }
        return itemResponse;
    }

    /**
     * Given an item name, check if the player has the 
     * item in inventory or not.
     * @param item (Collectable Entity)
     * @return True if player has item, and false otherwise.
     */
    public boolean hasItem(String item) {
        return !(inventory.getItem(item) == null);
    }

    /**
     * Given an item name, checks in the player inventory, and if exisits,
     * return the item as a collectable entity.
     * @param item (String)
     * @return The item (Collectable Entity)
     */
    public CollectableEntity getItem(String item) {
        return inventory.getItem(item);
    }

    /**
     * Looks through the inventory of the defeated enermy, and collects armour 
     * if the body has it.
     * @param deadBody
     */
    public void lootBody(MovingEntity deadBody) {
        if (deadBody.hasArmour()) {
            // Take armour from the dead body
            if (deadBody instanceof ZombieToast) {
                this.inventory.put(((ZombieToast)deadBody).getArmour(), this);
            } else if (deadBody instanceof Mercenary) {
                this.inventory.put(((Mercenary)deadBody).getArmour(), this);
            }
        }
    }
    
    /**
     * Helper function not for player.
     */
    public boolean hasArmour() {
        return false;
    }

    /**
     * Ticks the time that a potion has been in use for.
     */
    public void tickPotions() {
        if (invincDuration > 0) {
            invincDuration--;
        }
        if (invisDuration > 0) {
            invisDuration--;
        }
        // Set battle state depending on active potions
        if (invisDuration > 0) {
            battle.setBattleState(battle.getInvisibleState());
        }
        else if (invincDuration > 0 && !battle.getDifficulty().equals("Hard")) {
            battle.setBattleState(battle.getInvincibleState());
        }
        else {
            battle.setInitialState();
        }
    }

    /**
     * Returns a list of bribed meercenaries.
     * @return List<Mercenary> List of bribed mecernaries.
     */
    public List<Mercenary> getBribedMercenaries() {
        return bribedMercenaries;
    }

    // ********************************************************************************************\\
    //                                     Observer Pattern                                        \\
    // ********************************************************************************************\\

    @Override
    public void registerObserver(MovingEntityObserver o) {
        if(! listObservers.contains(o)) {
            listObservers.add(o);
        }
    }

    @Override
    public void removeObserver(MovingEntityObserver o) {
        listObservers.remove(o);        
    }
    @Override
    public void notifyObservers() {
        for (MovingEntityObserver obs: listObservers) {
            obs.update(this);
        }        
    }

    // ********************************************************************************************\\
    //                                   Getter and setters:                                       \\
    // ********************************************************************************************\\

    public void setInvisDuration(int time) {
        invisDuration = time;
    }
    public int getInvisDuration() {
        return invisDuration;
    }
    public void setInvincDuration(int time) {
        invincDuration = time;
    }
    public int getInvincDuration() {
        return invincDuration;
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
    }
    
    public Battle getBattle() {
        return battle;
    }

    public Inventory getInventory() {
        return inventory;
    }

}
