package dungeonmania.StaticEntities;

import dungeonmania.util.Position;

public class Boulder extends StaticEntity {
    /**
     * Constructor for Boulder
     * @param id
     * @param type
     * @param pos
     */
    public Boulder(String id, String type, Position pos) {
        super(id, type, pos);
        super.setCanStandOn(false);
    }
}
