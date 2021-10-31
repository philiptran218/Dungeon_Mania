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
import dungeonmania.CollectableEntities.Armour;


public class Mercenary extends MovingEntity {
    private MercenaryState enemyState;
    private MercenaryState allyState;
    private MercenaryState state;
    private Armour armour;
    private int price = 1;
    private Position previousPlayerPos;

    private int battleRadius = 3;
    public Mercenary(String id, String type, Position pos) {
        super(id, type, pos, 5, 5);
        this.enemyState = new MercenaryEnemyState(this);
        this.allyState = new MercenaryAllyState(this);
        this.state = enemyState;
        this.armour = generateArmour();
    }

    public Armour generateArmour() {
        int num = ThreadLocalRandom.current().nextInt(0,10);
        // num = 0,1,2,3,4,5,6,7,8,9

        // 30% chance that zombie spawns with armour
        if (num >= 7) {
            return new Armour("" + System.currentTimeMillis(), "armour", null);
        }
        return null;
    }

    public void move(Map<Position, List<Entity>> map){
        
        List<Entity> entities = map.get(super.getPlayerPos()).stream()
                                                             .filter(e -> e.getType().equals("player"))
                                                             .collect(Collectors.toList());
        Player player = (Player) entities.get(0);
        
        if (player.getInvisDuration() > 0) {
            return;
        }
        else if (player.getInvincDuration() > 0) {
            state.moveAway(map);
        }
        else {
            state.move(map);
        }
    }

    public boolean canPass(Map<Position, List<Entity>> map, Position pos) {
        return map.get(new Position(pos.getX(), pos.getY(), 1)).isEmpty();    
    }

    public void bribe() {
        this.state = allyState;
    }

    public int getPrice() {
        return price;
    }

    public boolean isAlly() {
        return state.equals(allyState);
    }

    public boolean hasArmour() {
        return this.armour != null;
    }
    public Armour getArmour() {
        Armour armour = this.armour;
        return armour;
    }

    public void setArmour(Armour armour) {
        this.armour = armour;
    }

    public int getBattleRadius() {
        return battleRadius;
    }

    public Position getPreviousPlayerPos() {
        return previousPlayerPos;
    }

    public void setPreviousPlayerPos(Position previousPlayerPos) {
        this.previousPlayerPos = previousPlayerPos;
    }

    
}

