package dungeonmania.StaticEntities;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public abstract class StaticEntity extends Entity {

    public StaticEntity(String id, String type, Position pos) {
        super(id, type, pos);
    }

}
