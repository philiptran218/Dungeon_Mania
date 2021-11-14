package dungeonmania.StaticEntities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public class SwitchDoor extends StaticEntity implements LogicEntity{

    private String logic = "or";

    public SwitchDoor(String id, String type, Position pos, String logic) {
        this(id, type, pos);
        this.logic = logic;
    }

    public SwitchDoor(String id, String type, Position pos) {
        super(id, type, pos);
    }

    @Override
    public boolean isOn(Map<Position, List<Entity>> map, List<String> visitedIDs) {
        List<Boolean> inputValues = new ArrayList<Boolean>();
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
                    inputValues.add(((LogicEntity) entities.get(0)).isOn(map, visitedIDs));
                }
            }
            visitedIDs.clear();
            visitedIDs.add(super.getId());
        }
        Position doorLayer = super.getPos().asLayer(1);
        
        if (LogicEntityUtility.applyLogic(logic, inputValues)) {
            if (this.getType().equals("switch_door")) {
                this.setType("switch_door_unlocked");
                this.setPos(doorLayer.asLayer(0));
                map.get(super.getPos().asLayer(0)).add(this);
                map.get(doorLayer).remove(this);
            }
        } else {
            if (this.getType().equals("switch_door_unlocked")) {
                this.setType("switch_door");
                this.setPos(doorLayer.asLayer(1));
                map.get(super.getPos().asLayer(1)).add(this);
                map.get(super.getPos().asLayer(0)).remove(this);
            }
        }
        return false;
    }
}
