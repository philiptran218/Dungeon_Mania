package dungeonmania.CollectableEntities;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public abstract class CollectableEntity extends Entity {

    /**
     * Constructor for CollectableEntity
     * @param id
     * @param type
     * @param pos
     */
    public CollectableEntity(String id, String type, Position pos) {
        super(id, type, pos);
    }
}