package dungeonmania.StaticEntities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public class LightBulb extends StaticEntity implements LogicEntity {

    private String logic = "or";

    public LightBulb(String id, String type, Position pos, String logic) {
        this(id, type, pos);
        this.logic = logic;
    }

    public LightBulb(String id, String type, Position pos) {
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
        Position position = super.getPos();
        if (LogicEntityUtility.applyLogic(logic, inputValues)) {
            map.get(position.asLayer(0)).remove(this);
            this.setType("light_bulb_on");
            map.get(position.asLayer(0)).add(this);
        } else {
            map.get(position.asLayer(0)).remove(this);
            this.setType("light_bulb_off");
            map.get(position.asLayer(0)).add(this);
        }
        return false;
    }



}
