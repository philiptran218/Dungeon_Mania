package dungeonmania.CollectableEntities;

import dungeonmania.util.Position;

public abstract class Utility extends CollectableEntity {

    /**
     * Constructor for Utility
     * @param id
     * @param type
     * @param pos
     */
    public Utility(String id, String type, Position pos) {
        super(id, type, pos);
    }

}