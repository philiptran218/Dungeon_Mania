package dungeonmania.CollectableEntities;

import dungeonmania.Entity;
import dungeonmania.MovingEntities.Player;
import dungeonmania.util.Position;

public abstract class CollectableEntity extends Entity {

    private Player player;
    /**
     * Constructor for CollectableEntity
     * @param id
     * @param type
     * @param pos
     */
    public CollectableEntity(String id, String type, Position pos) {
        super(id, type, pos);
    }

    /**
     * Abstract function.
     */
    abstract public void use();

    // ********************************************************************************************\\
    //                                    Getters and Setters                                      \\
    // ********************************************************************************************\\

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

}