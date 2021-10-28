package dungeonmania.MovingEntities;

import dungeonmania.util.Position;


public class Spider extends MovingEntity implements MovingEntityObserver{
    private Position startLocation;
    private int direction = 1; // 1 for clockwise

    public Spider(String id, String type, Position pos){
        super(id, type, pos, 3, 3);
        //startLocation);
        //this.startLocation = startLocation;
    }

    public void move() {

    }

    public void setPath() {

    }

    @Override
    public void update(MovingEntitySubject obj) {
        Player player = (Player) obj;
    }

}