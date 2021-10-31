package dungeonmania.Battles;

import dungeonmania.CollectableEntities.Bow;
import dungeonmania.MovingEntities.MovingEntity;
import dungeonmania.MovingEntities.Player;

public class Battle {

    BattleState state;
    BattleState InvincibleState;
    BattleState InvisibleState;
    BattleState NormalState;
    BattleState PeacefulState;
    BattleState HardState; 
    private String difficulty;

    /**
     * Constructor for Battle class
     */
    public Battle(String difficulty) {
        NormalState = new NormalState(this);
        PeacefulState = new PeacefulState(this);
        InvincibleState = new InvincibleState();
        InvisibleState = new InvisibleState();
        this.setDifficulty(difficulty);
        this.setInitialState();
    }

    // Getters and Setters
    public void setBattleState(BattleState state) {
        this.state = state;
    }
    public BattleState getBattleState() {
        return state;
    }

    public BattleState getNormalState() {
        return NormalState;
    }
    public BattleState getPeacefulState() {
        return PeacefulState;
    }
    public BattleState getInvisibleState() {
        return InvisibleState;
    }
    public BattleState getInvincibleState() {
        return InvincibleState;
    }

    public void setInitialState() {
        if (this.getDifficulty().equals("Peaceful")) {
            this.setBattleState(PeacefulState);
        }
        else {
            this.setBattleState(NormalState);
        }
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    public String getDifficulty() {
        return difficulty;
    }

    public MovingEntity fight(Player p1, MovingEntity p2) {
        return state.fight(p1, p2);
    }

    /**
     * Checks if the player has a bow so that they can make a second attack.
     * @param p
     * @return
     */
    public Boolean hasBow(Player p) {
        Bow bow = (Bow) p.getInventory().getItem("bow");
        
        if (bow == null) {
            return false;
        }
        return true;
    } 

}