package dungeonmania.StaticEntities;

import dungeonmania.util.Position;

public class FloorSwitch extends StaticEntity {

    public FloorSwitch(String id, String type, Position pos) {
        super(id, type, pos);
        super.setCanStandOn(true);
        super.setType("switch");
    }
}
