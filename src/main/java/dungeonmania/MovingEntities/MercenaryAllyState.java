package dungeonmania.MovingEntities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import dungeonmania.util.Position;
import dungeonmania.util.Direction;
import dungeonmania.Entity;

public class MercenaryAllyState implements MercenaryState{
    private Mercenary mercenary;

    public MercenaryAllyState(Mercenary mercenary) {
        this.mercenary = mercenary;
    }

    @Override
    public void move(Map<Position, List<Entity>> map) {
        Position playerPos = mercenary.getPlayerPos();
        Position pos = mercenary.getPos();
        
        List<Position> adjacentPos = pos.getAdjacentPositions();

        List<Position> cardinallyAdjacentPos = adjacentPos.stream().filter(e -> Position.isCardinallyAdjacent(pos, e)).collect(Collectors.toList());
        cardinallyAdjacentPos.add(pos);

        int distance = Integer.MAX_VALUE;
        Position newPos = pos;

        for (Position tempPos: cardinallyAdjacentPos) {
            if (Position.distance(playerPos, tempPos) < distance && Position.distance(playerPos, tempPos) != 0 && mercenary.canPass(map, tempPos)) {
                newPos = tempPos;
                distance = Position.distance(playerPos, tempPos);
            }
        }

        Position previousPlayerPos = mercenary.getPreviousPlayerPos();

        if (Position.distance(pos, previousPlayerPos) > 1) {
            mercenary.moveToPos(map, new Position(newPos.getX(), newPos.getY(), 3));
        } else if (Position.distance(previousPlayerPos, playerPos) != 0) {
            mercenary.moveToPos(map, previousPlayerPos);
        }

        mercenary.setPreviousPlayerPos(playerPos);
    }
    
}
