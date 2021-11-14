package dungeonmania.StaticEntities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public class FloorSwitch extends StaticEntity implements LogicGate{

    private String logic = "none";
    /**
     * Constructor for FloorSwitch
     * @param id
     * @param type
     * @param pos
     */
    public FloorSwitch(String id, String type, Position pos) {
        super(id, type, pos);
        super.setType("switch");
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
                if (entities != null && entities.size() > 0 && ((entities.get(0) instanceof Wire) || (entities.get(0) instanceof FloorSwitch))) {
                    String entityID = entities.get(0).getId();
                    if (!visitedIDs.contains(entityID)) {
                        visitedIDs.add(entityID);
                        inputs.add(entities.get(0));
                    }
                }
                visitedIDs.clear();
                visitedIDs.add(super.getId());
            }
            List<Boolean> inputValues = new ArrayList<Boolean>();
            for (Entity entity : inputs) {
                inputValues.add(((LogicGate) entity).isOn(map, visitedIDs));
            }
            inputValues.add(isUnderBoulder(map));
            return LogicGateUtility.applyLogic(logic, inputValues);
        }
    }
}
