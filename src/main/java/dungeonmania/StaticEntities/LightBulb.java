package dungeonmania.StaticEntities;

import dungeonmania.util.Position;

public class LightBulb extends StaticEntity {

    private String logic;

    public LightBulb(String id, String type, Position pos, String logic) {
        this(id, type, pos);
        this.logic = logic;
    }

    public LightBulb(String id, String type, Position pos) {
        super(id, type, pos);
        this.logic = "none";
    }

}
