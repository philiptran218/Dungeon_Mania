package dungeonmania.StaticEntities;

import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public class FloorSwitch extends StaticEntity implements LogicGate{

    private String logic;
    /**
     * Constructor for FloorSwitch
     * @param id
     * @param type
     * @param pos
     */
    public FloorSwitch(String id, String type, Position pos) {
        super(id, type, pos);
        super.setType("switch");
        this.logic = "none";
    }
    /**
     * Checks if there is a boulder on a floor switch
     * @param map
     * @return
     */
    public boolean isUnderBoulder(Map<Position, List<Entity>> map) {
        Position pos = super.getPos();
        List<Entity> entities = map.get(new Position(pos.getX(), pos.getY(), 1));
        if (entities.isEmpty()) {
            return false;
        }
        return (entities.get(0) instanceof Boulder);
    }
    @Override
    public boolean isOn (Map<Position, List<Entity>> map) {
        if (logic.equals("none")) {
            return isUnderBoulder(map);
        }
        return false;
    }
}
