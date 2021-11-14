package dungeonmania.StaticEntities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public class FloorSwitch extends StaticEntity implements LogicEntity{

    private String logic = "none";
    /**
     * Constructor for FloorSwitch
     * @param id
     * @param type
     * @param pos
     */
    public FloorSwitch(String id, String type, Position pos) {
        super(id, type, pos);
    }

    public FloorSwitch(String id, String type, Position pos, String logic) {
        this(id, type, pos);
        this.logic = logic;
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
    public boolean isOn (Map<Position, List<Entity>> map, List<String> visitedIDs) {
        if (logic.equals("none")) {
            return isUnderBoulder(map);
        } else {
            List<Entity> inputs = new ArrayList<Entity>();
            List<Position> adjacentPositions = super.getPos().getCardinallyAdjacentPositions();
            if (!(visitedIDs.contains(super.getId()))) {
                visitedIDs.add(super.getId());
            }
            for (Position position : adjacentPositions) {
                List<Entity> entities = map.get(position.asLayer(0));
                if (LogicEntityUtility.isLogicCarrier(entities)) {
                    String entityID = entities.get(0).getId();
                    if (!(visitedIDs.contains(entityID))) {
                        visitedIDs.add(entityID);
                        inputs.add(entities.get(0));
                    }
                }
            }
            List<Boolean> inputValues = new ArrayList<Boolean>();
            for (Entity entity : inputs) {
                inputValues.add(((LogicEntity) entity).isOn(map, visitedIDs));
            }
            inputValues.add(isUnderBoulder(map));
            return LogicEntityUtility.applyLogic(logic, inputValues);
        }
    }
}
