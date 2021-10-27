package dungeonmania.StaticEntities;

import dungeonmania.util.Position;

public class Exit extends StaticEntity{
    public Exit(String id, String type, Position pos) {
        super(id, type, pos);
        super.setCanStandOn(true);
        super.setType("Exit");
    }
}
