package dungeonmania.MovingEntities;

public class AllyState implements MercenaryState{
    private Mercenary mercenary;
    private Player player;


    public AllyState(Mercenary mercenary) {
        this.mercenary = mercenary;
    }
    @Override
    public void move() {
        
    }
    
}
