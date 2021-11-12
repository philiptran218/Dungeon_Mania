package dungeonmania.CollectableEntities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.StaticEntities.LogicGate;
import dungeonmania.StaticEntities.LogicGateUtility;
import dungeonmania.util.Position;

public class Bomb extends CombatItems implements LogicGate {
    private static final int EXPLOSION_RADIUS = 2;
    private String logic = "none";

    public Bomb(String id, String type, Position pos, String logic) {
        this(id, type, pos);
        this.logic = logic;
    }
    public Bomb(String id, String type, Position pos) {
        super(id, type, pos);
    }

    /**
     * Places the bomb onto the map, and removes it from inventory.
     */
    public void use() {
        getPlayer().getInventoryList().remove(this);
    }

    public void detonate(Map<Position, List<Entity>> map) {
        for(Position tempPos : map.keySet()) {
            if (Math.sqrt(Position.distance(getPos(), tempPos)) < EXPLOSION_RADIUS) {
                // Within explosion radius
                List<Entity> entities = map.get(tempPos);
                Entity player = getPlayer(entities);
                if (player != null) {
                    entities.clear();
                    entities.add(player);
                } else {
                    entities.clear();
                }
            }
        }
    }

    public Entity getPlayer(List<Entity> entities) {
        if (entities.size() == 0) {
            return null;
        }
        for (Entity entity: entities) {
            if (entity.getType().equals("player")) {
                return entity;
            }
        }
        return null;
    }

    @Override
    public boolean isOn(Map<Position, List<Entity>> map, List<String> visitedIDs) {
        List<Entity> inputs = new ArrayList<Entity>();
        List<Position> adjacentPositions = super.getPos().getCardinallyAdjacentPositions();
        for (Position position : adjacentPositions) {
            List<Entity> entities = map.get(position.asLayer(0));
            if (entities.size() > 0 && (entities.get(0) instanceof LogicGate)) {
                String entityID = entities.get(0).getId();
                if (!visitedIDs.contains(entityID)) {
                    inputs.add(entities.get(0));
                }
            }
        }
        List<Boolean> inputValues = new ArrayList<Boolean>();
        for (Entity entity : inputs) {
            inputValues.add(((LogicGate) entity).isOn(map, visitedIDs));
        }
        return LogicGateUtility.applyLogic(logic, inputValues);
        // Position pos = super.getPos();
        // List<Entity> entities = map.get(getPos().asLayer(0));
        // if ( entities.size() > 0 && entities.get(0).isType("switch")) {
        //     // Boulder is on a switch
        //     List<Position> adjacentPos = pos.getCardinallyAdjacentPositions();
        //     for (Position tempPos: adjacentPos) {
        //         // Checks if a bomb needs to be exploded
        //         List<Entity> collectablEntities = map.get(tempPos.asLayer(2));
        //         if (collectablEntities.size() > 0 && collectablEntities.get(0).isType("bomb")) {
        //             // contains bomb
        //             Bomb bomb = (Bomb) collectablEntities.get(0);
        //             bomb.detonate(map);
        //         }
        //     }
        // }
        // return false;
    }

}