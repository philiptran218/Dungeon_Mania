package dungeonmania.CollectableEntities;

import dungeonmania.util.Position;

public class Sceptre extends CombatItems {
    
    /**
     * Constructor for Sceptre
     * @param id
     * @param type
     * @param pos
     */
    public Sceptre(String id, String type, Position pos) {
        super(id, type, pos);
    }

    /**
     * Used to interact with enemies.
     */
    public void use() {
    }   
}