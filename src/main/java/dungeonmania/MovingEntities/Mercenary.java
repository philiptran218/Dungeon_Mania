package dungeonmania.MovingEntities;

import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dungeonmania.util.Position;
import dungeonmania.util.Direction;
import dungeonmania.Entity;


public class Mercenary extends MovingEntity implements MovingEntityObserver{
    private MercenaryState enemyState;
    private MercenaryState allyState;
    private MercenaryState state;

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

    @Override
    public void update(MovingEntitySubject obj) {
        super.setPlayerLocation(((Player) obj).getPos());
    }


}

