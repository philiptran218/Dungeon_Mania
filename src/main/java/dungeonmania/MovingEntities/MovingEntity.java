package dungeonmania.MovingEntities;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import dungeonmania.util.Position;
import dungeonmania.util.Direction;
import dungeonmania.Entity;


public abstract class MovingEntity extends Entity{
    private double health;
    private double attackDamage;
    private Position playerLocation;
    private String type;

    /**
     * 
     * @param health 
     * @param attackDamage
     * @param location
     */
    public MovingEntity(String id, String type, Position pos, double health, double attackDamage){
        super(id, type, pos);
        this.health = health;
        this.attackDamage = attackDamage;
    }

    /**
     * @param entities A list of Entity at the position that the MovingEntity is checking it can move to
     * @return true if the MovingEntity can pass through each Entity in entities, false otherwise
     */
    public abstract boolean canPass(Map<Position, List<Entity>> map, Position pos);

    public void moveInDir(Map<Position, List<Entity>> map, Direction direction) {
        map.get(super.getPos()).remove(this);
        
        Position newPos = super.getPos().translateBy(direction);
        super.setPos(newPos);
        map.get(newPos).add(this);
    }
    public void moveToPos(Map<Position, List<Entity>> map, Position newPos) {
        map.get(super.getPos()).remove(this);

        super.setPos(newPos);
        map.get(newPos).add(this);
    }


    ////////////////////////////////////////////////////////////////////////////
    // Getters and Setters
    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(double attackDamage) {
        this.attackDamage = attackDamage;
    }

    public Position getPlayerLocation() {
        return playerLocation;
    }

    public void setPlayerLocation(Position playerLocation) {
        this.playerLocation = playerLocation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}