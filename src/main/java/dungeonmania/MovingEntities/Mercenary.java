package dungeonmania.MovingEntities;

import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;
import java.util.concurrent.ThreadLocalRandom;

import dungeonmania.Entity;
import dungeonmania.util.Position;
import dungeonmania.CollectableEntities.Armour;


public class Mercenary extends MovingEntity {
    private MercenaryState enemyState;
    private MercenaryState allyState;
    private MercenaryState state;
    private Position previousPlayerPos;
    private Armour armour;
    private int price = 1;
    private int battleRadius = 3;
    private int bribedTicks = -1;

    /**
     * Constructor for mercenary.
     * @param id
     * @param type
     * @param pos
     */
    public Mercenary(String id, String type, Position pos) {
        super(id, type, pos, 15, 2);
        this.enemyState = new MercenaryEnemyState(this);
        this.allyState = new MercenaryAllyState(this);
        this.state = enemyState;
        this.armour = generateArmour();
    }

    // ********************************************************************************************\\
    //                                         Functions                                           \\
    // ********************************************************************************************\\

    /**
     * Randomly generates amour.
     * @return New object armour, else null.
     */
    public Armour generateArmour() {
        int num = ThreadLocalRandom.current().nextInt(0,10);
        // num = 0,1,2,3,4,5,6,7,8,9

        // 30% chance that zombie spawns with armour
        if (num >= 7) {
            return new Armour("" + System.currentTimeMillis(), "armour", null);
        }
        return null;
    }

    /**
     * Automatic movement of mecernary around the map tracking 
     * the player.
     */
    public void move(Map<Position, List<Entity>> map){
        // Entity list to add all entities with respect to player.
        List<Entity> entities = map.get(super.getPlayerPos()).stream()
                                                             .filter(e -> e.getType().equals("player"))
                                                             .collect(Collectors.toList());
        Player player = (Player) entities.get(0);
        
        // Movement based on potion consumption
        if (player.getInvisDuration() > 0) {
            return;
        }
        else if (player.getInvincDuration() > 0 && !player.getBattle().getDifficulty().equals("hard")) {
            state.moveAway(map);
        }
        else {
            state.move(map);
        }
    }

    /**
     * Checks if the mecenary can pass a certain position.
     */
    public boolean canPass(Map<Position, List<Entity>> map, Position pos) {
        return map.get(new Position(pos.getX(), pos.getY(), 1)).isEmpty();    
    }

    /**
     * Changing the state of the mecenary to ally.
     */
    public void bribe() {
        this.state = allyState;
    }

    /**
     * Changes the state of the mercenary back to enemy.
     * Used when the sceptre effect wears off.
     */
    public void resetMindControl() {
        this.state = enemyState;
    }

    /**
     * Checks the state of the ally.
     * @return true if the mercenary is an ally (has been bribed)
     */
    public boolean isAlly() {
        return state.equals(allyState);
    }

    /**
     * Checks if the mercenary owns armour
     * @return True is mecenary has armour, false otherwise.
     */
    public boolean hasArmour() {
        return this.armour != null;
    }

    // ********************************************************************************************\\
    //                                   Getter and setters:                                       \\
    // ********************************************************************************************\\

    public void setArmour(Armour armour) {
        this.armour = armour;
    }

    public Armour getArmour() {
        Armour armour = this.armour;
        return armour;
    }

    public int getBattleRadius() {
        return battleRadius;
    }

    public Position getPreviousPlayerPos() {
        return previousPlayerPos;
    }

    public void setPreviousPlayerPos(Position previousPlayerPos) {
        this.previousPlayerPos = previousPlayerPos;
    }

    public int getPrice() {
        return price;
    }

    public int getBribedTicks() {
        return bribedTicks;
    }

    public void setBribedTicks(int tick) {
        this.bribedTicks = tick;
    }
}

