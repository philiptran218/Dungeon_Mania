package dungeonmania.MovingEntities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dungeonmania.util.Position;
import dungeonmania.util.Direction;
import dungeonmania.Entity;
import dungeonmania.CollectableEntities.CollectableEntity;


public class Player extends MovingEntity implements MovingEntitySubject{
    private List<MovingEntityObserver> listObservers = new ArrayList<MovingEntityObserver>();

    public Player(String id, String type, Position pos){
        super(id, type, pos, 10, 10);
    }

    public void move(Map<Position, List<Entity>> map, Direction direction) {
        notifyObservers();
        Position newPos = super.getPos().translateBy(direction);        
        if (canPass(map, newPos)) {
            moveInDir(map, direction);
        } else {
            // Do nothing
        }
    }

    public boolean canPass(Map<Position, List<Entity>> map, Position pos) {
        return map.get(new Position(pos.getX(), pos.getY(), 1)).isEmpty();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Observer Pattern
    @Override
    public void registerObserver(MovingEntityObserver o) {
        if(! listObservers.contains(o)) {
            listObservers.add(o);
        }
    }

    @Override
    public void removeObserver(MovingEntityObserver o) {
        listObservers.remove(o);        
    }
    @Override
    public void notifyObservers() {
        for (MovingEntityObserver obs: listObservers) {
            obs.update(this);
        }        
    }

}
