package dungeonmania.CollectableEntities;

import dungeonmania.MovingEntities.Player;
import dungeonmania.util.Position;

public class HealthPotion extends Potion implements AbilityBehaviour {

    /**
     * Constructor for HealthPotion
     * @param id
     * @param type
     * @param pos
     */
    public HealthPotion(String id, String type, Position pos) {
        super(id, type, pos);
    }

    /**
     * Activates the ability of the potion.
     * Called when the player wants to consume the potion.
     */
    public void ability(Player plyr) {
        setPlayer(plyr);
        getPlayer().setHealth(100);
    }
    
}