package dungeonmania.MovingEntities;

import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import dungeonmania.util.Position;
import dungeonmania.util.Direction;
import dungeonmania.Entity;


public class Mercenary extends MovingEntity {
    private MercenaryState enemyState;
    private MercenaryState allyState;
    private MercenaryState state;
    private int price = 1;

    private int battleRadius;
    public Mercenary(String id, String type, Position pos) {
        super(id, type, pos, 5, 5);
        this.enemyState = new MercenaryEnemyState(this);
        this.allyState = new MercenaryAllyState(this);
        this.state = enemyState;
    }

    public boolean generateArmour() {
        int num = ThreadLocalRandom.current().nextInt(0,10);
        // num = 0,1,2,3,4,5,6,7,8,9

        // 30% chance that zombie spawns with armour
        return num >= 7;
    }

    public void move(Map<Position, List<Entity>> map){
        state.move(map);
    }

    public boolean canPass(Map<Position, List<Entity>> map, Position pos) {
        return map.get(new Position(pos.getX(), pos.getY(), 1)).isEmpty();
    }

    public void bribe() {
        this.state = allyState;
    }
    public void moveAway(Map<Position, List<Entity>> map) {
        Position playerPos = this.getPlayerLocation();
        Position pos = super.getPos();
        
        List<Position> adjacentPos = pos.getAdjacentPositions();

        List<Position> cardinallyAdjacentPos = adjacentPos.stream().filter(e -> Position.isCardinallyAdjacent(pos, e)).collect(Collectors.toList());
        cardinallyAdjacentPos.add(pos);

        int distance = Integer.MIN_VALUE;
        Position newPos = pos;

        for (Position tempPos: cardinallyAdjacentPos) {
            if (Position.distance(playerPos, tempPos) > distance && this.canPass(map, tempPos)) {
                newPos = tempPos;
                distance = Position.distance(playerPos, tempPos);
            }
        }

        this.moveToPos(map, new Position(newPos.getX(), newPos.getY(), 3));
    }

    public int getPrice() {
        return price;
    }
}

