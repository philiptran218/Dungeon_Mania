package dungeonmania.MovingEntities;

import java.util.concurrent.ThreadLocalRandom;

import dungeonmania.util.Position;


public class ZombieToast extends MovingEntity implements MovingEntityObserver{
    private boolean armour;

    public ZombieToast(Position startLocation) {
        super(4, 4, startLocation);
        this.armour = generateArmour();

    }

    public boolean generateArmour() {
        int num = ThreadLocalRandom.current().nextInt(0,10);
        // num = 0,1,2,3,4,5,6,7,8,9

        // 30% chance that zombie spawns with armour
        return num >= 7;
    }

    public int direction() {
        int num = ThreadLocalRandom.current().nextInt(0,4);
        // num = 0,1,2,3
        System.out.println(num);
        
        return 1;
    }

    public void move() {

    }

    public boolean canPass(String type){
        return true;

    }
    @Override
    public void update(MovingEntitySubject obj) {
        Player player = (Player) obj;
    }
}
