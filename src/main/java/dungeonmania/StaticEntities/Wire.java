package dungeonmania.StaticEntities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public class Wire extends StaticEntity implements LogicGate{

    private static final String LOGIC = "or";

    public Wire(String id, String type, Position pos) {
        super(id, type, pos);
    }

    @Override
    public boolean isOn(Map<Position, List<Entity>> map, List<String> visitedIDs) {
        List<Entity> inputs = new ArrayList<Entity>();
        List<Position> adjacentPositions = super.getPos().getCardinallyAdjacentPositions();
        for (Position position : adjacentPositions) {
            List<Entity> entities = map.get(position.asLayer(0));
            if (entities != null && entities.size() > 0 && ((entities.get(0) instanceof Wire) || (entities.get(0) instanceof FloorSwitch))) {
                String entityID = entities.get(0).getId();
                if (!(visitedIDs.contains(entityID))) {
                    visitedIDs.add(entityID);
                    inputs.add(entities.get(0));
                }
            }
        }
        List<Boolean> inputValues = new ArrayList<Boolean>();
        for (Entity entity : inputs) {
            inputValues.add(((LogicGate) entity).isOn(map, visitedIDs));
        }
        return LogicGateUtility.applyLogic(LOGIC, inputValues);
    }
}
