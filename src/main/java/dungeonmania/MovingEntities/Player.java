package dungeonmania.MovingEntities;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.util.Position;

public class Player extends MovingEntity implements MovingEntitySubject{
    private List<MovingEntityObserver> listObservers = new ArrayList<MovingEntityObserver>();

    public Player(Position startLocation){
        super(10, 10, startLocation);
    }

    @Override
    public void move() {
        notifyObservers();        
    }
    
    public void bribeMerc() {

    }


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
