package dungeonmania.CollectableEntities;

import dungeonmania.util.Position;

public abstract class Potion extends CollectableEntity {

    /**
     * Constructor for Potion
     * @param id
     * @param type
     * @param pos
     */
    public Potion(String id, String type, Position pos) {
        super(id, type, pos);
    }
}