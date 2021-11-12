package dungeonmania.MovingEntities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.AnimationUtility;
import dungeonmania.Entity;
import dungeonmania.StaticEntities.SwampTile;
import dungeonmania.response.models.AnimationQueue;

public class MercenaryEnemyState implements MercenaryState{
    private Mercenary mercenary;

    /**
     * Constructor for mercenary enermy state.
     * @param mercenary
     */
    public MercenaryEnemyState(Mercenary mercenary) {
        this.mercenary = mercenary;
    }

    // ********************************************************************************************\\
    //                                         Functions                                           \\
    // ********************************************************************************************\\

    /**
     * Mercenary moves - follows the player
     * Moves twice as fast if the player is in the battle radius
     * @param map
     */
    @Override
    public void move(Map<Position, List<Entity>> map, List<AnimationQueue> animations) {
        Position playerPos = mercenary.getPlayerPos();
        Position pos = mercenary.getPos();

        double distance = Math.sqrt(Position.distance(playerPos, pos));

        if (distance <= mercenary.getBattleRadius()) {
            // Check if player will fight
            if (map.get(playerPos).size() > 1) {
                // Player will fight with an enemy, move twice
                moveDefault(map, animations);
            }
        }
        moveDefault(map, animations);
    }
    
    /**
     * Mercenary moves - follows the player
     * @param map
     */
    public void moveDefault(Map<Position, List<Entity>> map, List<AnimationQueue> animations) {
        Position playerPos = mercenary.getPlayerPos();
        Position pos = mercenary.getPos();

        Position newPos = dijkstra(map, pos);
        if (newPos == null) {
            // Player is unreachable, use a method other than dijkstra to calculate move
            newPos = moveCloser(map, animations);
        }

        Direction direction = Position.getTranslationDirection(pos, newPos);
        AnimationUtility.translateMovingEntity(animations, false, mercenary, direction);

        mercenary.moveToPos(map, newPos.asLayer(3));
        mercenary.setPreviousPlayerPos(playerPos);


    }

    /**
     * Uses the dijkstra algorithmn to find the next move that an entity should use
     * to get to the player
     * @param map
     * @param pos the starting position of the entity
     * @return The position that the entity should move to. Is null if the path to
     * the player is unreachable
     */
    public Position dijkstra(Map<Position, List<Entity>> map, Position pos) {
        pos = pos.asLayer(0);
        Map<Position, Double> dist = new HashMap<Position, Double>();
        Map<Position, Position> prev  = new HashMap<Position,Position>();

        for (Position mapPos: map.keySet()) {
            Position newPos = mapPos.asLayer(0);
            dist.put(newPos, Double.POSITIVE_INFINITY);
            prev.put(newPos, null);
        }

        // Set the starting position with distance 0
        dist.put(pos, (double) 0);
        
        Queue<Node> queue = new PriorityQueue<Node>(new Node());
        queue.add(new Node(pos, 0.0));
        
        while (queue.size() > 0) {
            Position u = queue.poll().getPos();
            for (Position v: u.getCardinallyAdjacentPositions()) {
                if (!dist.containsKey(v)) {
                    // v is outside of the map, don't do anything
                    continue;
                }

                double cost = travelCost(map, u, v);
                if (dist.get(u) + cost <  dist.get(v)) {
                    // This is a shorter path
                    dist.put(v, dist.get(u) + cost);
                    prev.put(v, u);
                    queue.add(new Node(v, dist.get(u) + cost));
                }
            }
        }

        Position curr = mercenary.getPlayerPos().asLayer(0);
        Position previousPos = curr;

        if (dist.get(curr).isInfinite()) {
            return null;
        }

        while (!curr.equals(pos)) {
            previousPos = curr;
            curr = prev.get(curr);
        }
        return previousPos;
    }

    public Double travelCost(Map<Position, List<Entity>> map, Position u, Position v) {
        if (!mercenary.canPass(map, v)) {
            return Double.POSITIVE_INFINITY;
        }

        List<Entity> entities = map.get(v.asLayer(0));
        if (entities.size() > 0 && entities.get(0).isType("swamp_tile")) {
            // v is a swamp tile
            return (double) ((SwampTile) entities.get(0)).getFactor();
        } else {
            return (double) 1;
        }
    }

    public Position moveCloser(Map<Position, List<Entity>> map, List<AnimationQueue> animations) {
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
        return newPos.asLayer(3);
    }

    /**
     * Moves away from the player when they are invincible
     */
    @Override
    public void moveAway(Map<Position, List<Entity>> map, List<AnimationQueue> animations) {
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

        Direction direction = Position.getTranslationDirection(pos, newPos);
        AnimationUtility.translateMovingEntity(animations, false, mercenary, direction);
        
        mercenary.moveToPos(map, newPos.asLayer(3));
    }
    
}
