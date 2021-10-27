package dungeonmania.CollectableEntities;

import dungeonmania.util.Position;

public abstract class CombatItems extends CollectableEntity {

    /**
     * Constructor for CombatItems
     * @param id
     * @param type
     * @param pos
     */
    public CombatItems(String id, String type, Position pos) {
        super(id, type, pos);
    }
}