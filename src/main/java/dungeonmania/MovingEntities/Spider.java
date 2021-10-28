package dungeonmania.MovingEntities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dungeonmania.util.Position;
import dungeonmania.util.Direction;
import dungeonmania.Entity;


public class Spider extends MovingEntity implements MovingEntityObserver{
    private Position startPos;
    private boolean clockwise = true; // 1 for clockwise
    private List<Position> path = new ArrayList<Position>();
    private int pathPos = 0;

    public Spider(String id, String type, Position pos){
        super(id, type, pos, 3, 3);
        this.startPos = pos;
        this.setPath();
    }

    public void move(Map<Position, List<Entity>> map) {
        if (super.getPos().equals(startPos)) {
            // Spider has just spawned, move up
            Position newPos = this.path.get(0);
            super.moveToPos(map, newPos);
            return;
        }

        int forwardMove = pathPos + 1;
        int backwardMove = pathPos - 1;

        if (forwardMove > 7) {
            forwardMove = 0;
        } 
        if (backwardMove < 0) {
            backwardMove = 7;
        }
        boolean canMoveForward = canPass(map, this.path.get(forwardMove));
        boolean canMoveBackward = canPass(map, this.path.get(backwardMove));

        if (!canMoveForward && !canMoveBackward) {
            // Spider is trapped
            this.clockwise = true;
            return;
        }
        if (!canMoveForward) {
            this.clockwise = false;
            super.moveToPos(map, this.path.get(backwardMove));
        } else if (!canMoveBackward) {
            this.clockwise = true;
            super.moveToPos(map, this.path.get(forwardMove));
        } else {
            // can move in either direction
            if (clockwise) {
                super.moveToPos(map, this.path.get(forwardMove));
            } else {
                super.moveToPos(map, this.path.get(backwardMove));
            }

        }

    }

    public boolean canPass(Map<Position, List<Entity>> map, Position pos) {
        List<Entity> StaticEntities = map.get(new Position(pos.getX(), pos.getY(), 1));
        if (StaticEntities.isEmpty()) {
            return true;
        } else if (!StaticEntities.get(0).getType().equals("boulder")) {
            return true;
        } else {
            return false;
        }
    }



    public void setPath() {
        this.path.add(startPos.translateBy(Direction.UP));
        this.path.add(startPos.translateBy(Direction.UP).translateBy(Direction.RIGHT));
        this.path.add(startPos.translateBy(Direction.RIGHT));
        this.path.add(startPos.translateBy(Direction.RIGHT).translateBy(Direction.DOWN));
        this.path.add(startPos.translateBy(Direction.DOWN));
        this.path.add(startPos.translateBy(Direction.DOWN).translateBy(Direction.LEFT));
        this.path.add(startPos.translateBy(Direction.LEFT));
        this.path.add(startPos.translateBy(Direction.LEFT).translateBy(Direction.UP));
    }


    @Override
    public void update(MovingEntitySubject obj) {
        super.setPlayerLocation(((Player) obj).getPos());
    }

}