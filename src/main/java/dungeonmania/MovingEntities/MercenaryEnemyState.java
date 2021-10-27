package dungeonmania.MovingEntities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        Position playerPos = mercenary.getPlayerLocation();
        Position pos = mercenary.getPos();
        Position differencePos = Position.calculatePositionBetween(pos, playerPos);
        boolean moved = false;
        if (differencePos.getY() > 0) {
            if (mercenary.canPass(map, pos.translateBy(Direction.DOWN))) {
                mercenary.moveInDir(map, Direction.DOWN);
                moved = true;
            }
        } else if (differencePos.getY() < 0) {
            if (mercenary.canPass(map, pos.translateBy(Direction.UP))) {
                mercenary.moveInDir(map, Direction.UP);
                moved = true;
            }
        }

        if (differencePos.getX() > 0) {
            if (mercenary.canPass(map, pos.translateBy(Direction.RIGHT)) && !moved) {
                mercenary.moveInDir(map, Direction.RIGHT);
            } 
        } else if (differencePos.getX() < 0) {
            if (mercenary.canPass(map, pos.translateBy(Direction.LEFT)) && !moved) {
                mercenary.moveInDir(map, Direction.LEFT);
            } 
        }
    }
    
}
