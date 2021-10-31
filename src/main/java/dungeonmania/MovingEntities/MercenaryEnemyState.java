package dungeonmania.MovingEntities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import dungeonmania.util.Position;
import dungeonmania.util.Direction;
import dungeonmania.Entity;

public class MercenaryEnemyState implements MercenaryState{
    private Mercenary mercenary;

    public MercenaryEnemyState(Mercenary mercenary) {
        this.mercenary = mercenary;
    }

    @Override
    public void move(Map<Position, List<Entity>> map) {
        Position playerPos = mercenary.getPlayerPos();
        Position pos = mercenary.getPos();

        double distance = Math.sqrt(Position.distance(playerPos, pos));

        if (distance <= mercenary.getBattleRadius()) {
            // Check if player will fight
            if (map.get(playerPos).size() > 1) {
                // Player will fight with an enemy, move twice
                moveDefault(map);
            }

        }

        moveDefault(map);
    }
    
    public void moveDefault(Map<Position, List<Entity>> map) {
        Position playerPos = mercenary.getPlayerPos();
        Position pos = mercenary.getPos();
        
        List<Position> adjacentPos = pos.getAdjacentPositions();

        List<Position> cardinallyAdjacentPos = adjacentPos.stream().filter(e -> Position.isCardinallyAdjacent(pos, e)).collect(Collectors.toList());
        cardinallyAdjacentPos.add(pos);

        int distance = Integer.MAX_VALUE;
        Position newPos = pos;

        for (Position tempPos: cardinallyAdjacentPos) {
            if (Position.distance(playerPos, tempPos) < distance && mercenary.canPass(map, tempPos)) {
                newPos = tempPos;
                distance = Position.distance(playerPos, tempPos);
            }
        }

        mercenary.moveToPos(map, new Position(newPos.getX(), newPos.getY(), 3));
        mercenary.setPreviousPlayerPos(playerPos);
    }

    @Override
    public void moveAway(Map<Position, List<Entity>> map) {
        Position playerPos = mercenary.getPlayerPos();
        Position pos = mercenary.getPos();
        
        List<Position> adjacentPos = pos.getAdjacentPositions();

        List<Position> cardinallyAdjacentPos = adjacentPos.stream().filter(e -> Position.isCardinallyAdjacent(pos, e)).collect(Collectors.toList());
        cardinallyAdjacentPos.add(pos);

        int distance = Integer.MIN_VALUE;
        Position newPos = pos;

        for (Position tempPos: cardinallyAdjacentPos) {
            if (Position.distance(playerPos, tempPos) > distance && mercenary.canPass(map, tempPos)) {
                newPos = tempPos;
                distance = Position.distance(playerPos, tempPos);
            }
        }

        mercenary.moveToPos(map, new Position(newPos.getX(), newPos.getY(), 3));
    }
}
