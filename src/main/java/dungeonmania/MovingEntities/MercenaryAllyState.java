package dungeonmania.MovingEntities;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.AnimationUtility;
import dungeonmania.Entity;
import dungeonmania.response.models.AnimationQueue;

public class MercenaryAllyState implements MercenaryState{
    private Mercenary mercenary;

    /**
     * Constructor for mecenary ally state.
     * @param mercenary
     */
    public MercenaryAllyState(Mercenary mercenary) {
        this.mercenary = mercenary;
    }

    // ********************************************************************************************\\
    //                                         Functions                                           \\
    // ********************************************************************************************\\

    /**
     * player is an ally, mercenary follows the player
     */
    @Override
    public void move(Map<Position, List<Entity>> map, List<AnimationQueue> animations) {
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

            Direction direction = Position.getTranslationDirection(pos, newPos);
            AnimationUtility.translateMovingEntity(animations, false, mercenary, direction);
        } else if (Position.distance(previousPlayerPos, playerPos) != 0) {
            mercenary.moveToPos(map, previousPlayerPos);
            
            Direction direction = Position.getTranslationDirection(pos, previousPlayerPos);
            AnimationUtility.translateMovingEntity(animations, false, mercenary, direction);
        }

        mercenary.setPreviousPlayerPos(playerPos);
    }

    /**
     * Player is invincible, allymercenary moves normally
     */
    public void moveAway(Map<Position, List<Entity>> map, List<AnimationQueue> animations) {
        move(map, animations);
    }

}
