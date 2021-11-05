package dungeonmania.MovingEntities;

import dungeonmania.Entity;
import java.util.Map;

import org.json.JSONObject;

import java.util.List;

import dungeonmania.util.Position;
import dungeonmania.util.Direction;


public abstract class MovingEntity extends Entity implements MovingEntityObserver {
    private double health;
    private double attackDamage;
    private Position playerPos;

    // ********************************************************************************************\\
    //                                         Functions                                           \\
    // ********************************************************************************************\\

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

    /**
     * The default move of the MovingEntity in a tic
     * @param map
     */
    public abstract void move(Map<Position, List<Entity>> map);

    /**
     * Move the MovingEntity in a direction
     * @param map
     * @param direction
     */
    public void moveInDir(Map<Position, List<Entity>> map, Direction direction) {
        map.get(super.getPos()).remove(this);
        
        Position newPos = super.getPos().translateBy(direction);
        super.setPos(newPos);
        map.get(newPos).add(this);
    }

    /**
     * Moves the MovingEntity to a given Position on the map
     * @param map
     * @param newPos
     */
    public void moveToPos(Map<Position, List<Entity>> map, Position newPos) {
        map.get(super.getPos()).remove(this);

        super.setPos(newPos);
        map.get(newPos).add(this);
    }

    /**
     * Abstract function, do nothing.
     * @return ...
     */
    public abstract boolean hasArmour();

    /**
     * Checks if the entity is moving to a position with a boulder.
     * @param map
     * @param pos
     * @return True if that position has a boulder, false otherwise.
     */
    public boolean isPassingBoulder(Map<Position, List<Entity>> map, Position pos) {
        List<Entity> entities = map.get(new Position(pos.getX(), pos.getY(), 1));
        if (entities.size() == 0) {
            return false;
        } else {
            Entity entity = entities.get(0);
            return entity.getType().equals("boulder");
        }

    }

    /**
     * Updating the player position.
     */
    @Override
    public void update(MovingEntitySubject obj) {
        this.setPlayerPos(((Player) obj).getPos());
    }

    @Override
    public JSONObject toJSONObject() {
        return super.toJSONObject();
    }

    // ********************************************************************************************\\
    //                                   Getter and setters:                                       \\
    // ********************************************************************************************\\

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

    public Position getPlayerPos() {
        return playerPos;
    }

    public void setPlayerPos(Position playerPos) {
        this.playerPos = playerPos;
    }

}