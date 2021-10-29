package dungeonmania.MovingEntities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dungeonmania.util.Position;
import dungeonmania.util.Direction;
import dungeonmania.Entity;
import dungeonmania.Inventory;
import dungeonmania.CollectableEntities.CollectableEntity;
import dungeonmania.StaticEntities.ZombieToastSpawner;
import dungeonmania.exceptions.InvalidActionException;


public class Player extends MovingEntity implements MovingEntitySubject {
    private List<MovingEntityObserver> listObservers = new ArrayList<MovingEntityObserver>();
    private Inventory inventory = new Inventory(this);
    private List<Mercenary> bribedMercenaries = new ArrayList<Mercenary>();

    public Player(String id, String type, Position pos){
        super(id, type, pos, 10, 10);
    }

    public void move(Map<Position, List<Entity>> map) {

    }
    public void move(Map<Position, List<Entity>> map, Direction direction) {
        Position newPos = super.getPos().translateBy(direction);        
        if (canPass(map, newPos)) {
            moveInDir(map, direction);
        } else {
            // Do nothing
        }
        pickUp(map);
        notifyObservers();
    }

    public boolean canPass(Map<Position, List<Entity>> map, Position pos) {
        return map.get(new Position(pos.getX(), pos.getY(), 1)).isEmpty();
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
            if (entity instanceof CollectableEntity) {
                this.inventory.put(entity);
                collectables.remove(entity);
            }
        }
    }

    /**
     * The player bribes the mercenary. The bribe will be successful if the player
     * has enough gold and is within 2 cardinal tiles from the mercenary
     * @param map 
     * @param mercenary mercenary that the player will try to bribe
     */
    public void bribeMercenary(Map<Position, List<Entity>> map, Mercenary mercenary) throws InvalidActionException{
        Position pos = super.getPos();
        Position mercenaryPos = mercenary.getPos();

        if (inventory.getItem("treasure") != null) {
            // Player has some treasures
            if (inventory.getNoItemType("treasure") <= mercenary.getPrice()) {
                // Player doesnt have enough gold
                throw new InvalidActionException("Player doesn't have enough gold");
            } 
            // Remove gold;
            for (int i = 1; i < mercenary.getPrice(); i++) {
                inventory.useItem("treasure");
            }
        } else {
            throw new InvalidActionException("Player doesn't any enough gold");
        }

        Position difference = Position.calculatePositionBetween(pos, mercenaryPos);
        if (Math.abs(difference.getX()) + Math.abs(difference.getY()) > 2) {
            // player more than 2 cardinal tiles from mercenary
            throw new InvalidActionException("Mercenary too far away");
        } else {
            mercenary.bribe();
            bribedMercenaries.add(mercenary);
        }
    }

    public void attackZombieSpawner(Map<Position, List<Entity>> map, ZombieToastSpawner spawner) {
        Position pos = super.getPos();
        Position spawnerPos = spawner.getPos();

        Position difference = Position.calculatePositionBetween(pos, spawnerPos);
        if (Math.abs(difference.getX()) + Math.abs(difference.getY()) > 2) {
            // player more than 2 cardinal tiles from mercenary
            throw new InvalidActionException("Mercenary too far away");
        } else {
            
        }     
    }


    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Get the player inventory in the form: List<CollectableEntity>
     * @return List of entity in the player's inventory.
     */
    public List<CollectableEntity> getInventoryList() {
        return inventory.getInventory();
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

    ////////////////////////////////////////////////////////////////////////////
    // Observer Pattern
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

}
