package dungeonmania.MovingEntities;

import java.util.concurrent.ThreadLocalRandom;

import dungeonmania.util.Position;


public class Mercenary extends MovingEntity implements MovingEntityObserver{
    private MercenaryState enemyState;
    private MercenaryState allyState;
    private MercenaryState state;

    private Position playerlocation;

    private int battleRadius;
    public Mercenary(Position startLocation) {
        super(5, 5, startLocation);
        this.enemyState = new EnemyState(this);
        this.allyState = new AllyState(this);
    }


    public boolean generateArmour() {
        int num = ThreadLocalRandom.current().nextInt(0,10);
        // num = 0,1,2,3,4,5,6,7,8,9

        // 30% chance that zombie spawns with armour
        return num >= 7;
    }

    public void move(){

    }


    @Override
    public void update(MovingEntitySubject obj) {
        Player player = (Player) obj;
    }

}

