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

    /**
     * Constructor for Battle class
     */
    public Battle() {
        NormalState = new NormalState(this);
        PeacefulState = new PeacefulState(this);
        InvincibleState = new InvincibleState();
        InvisibleState = new InvisibleState();

        // Need a way of checking difficulty here to set state.
        state = NormalState;
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

    public void fight(Player p1, MovingEntity p2) {
        state.fight(p1, p2);
    }

    /**
     * Checks if the player has a bow so that they can make a second attack.
     * @param p
     * @return
     */
    public Boolean hasBow(Player p) {
        Bow bow = (Bow) p.getInventory().getItem("Bow");
        
        if (bow == null) {
            return false;
        }
        return true;
    } 

}