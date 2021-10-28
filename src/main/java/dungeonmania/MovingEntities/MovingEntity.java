package dungeonmania.MovingEntities;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public abstract class MovingEntity extends Entity {
    private int health;
    private int attackDamage;
    //private Position location;

    /**
     * 
     * @param health 
     * @param attackDamage
     * @param location
     */
    public MovingEntity(String id, String type, Position pos, int health, int attackDamage){
        super(id, type, pos);
        this.health = health;
        this.attackDamage = attackDamage;
        //this.location = location;
    }

    /**
     * When the MovingEntity enemy fights with the player
     */
    public void fight() {

    }

    public void moveUp() {

    }
    public void moveRight() {

    }
    public void moveDown() {

    }
    public void moveLeft() {

    }

    /** 
     * The MovingEntity will move around the map based on its type
     */
    public abstract void move();

    /**
     * 
     * @return
     */
    //public abstract void canPass();




    ////////////////////////////////////////////////////////////////////////////
    // Getters and Setters
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    // public Position getLocation() {
    //     return location;
    // }

    // public void setLocation(Position location) {
    //     this.location = location;
    // }


}