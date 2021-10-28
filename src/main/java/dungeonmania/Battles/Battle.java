package dungeonmania.Battles;

import dungeonmania.MovingEntities.MovingEntity;

public class Battle {

    BattleState state;
    BattleState InvincibleState;
    BattleState InvisibleState;
    BattleState NormalState;

    /**
     * Constructor for Battle class
     */
    public Battle() {
        NormalState = new NormalState();
        InvincibleState = new InvincibleState();
        InvisibleState = new InvisibleState();
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
    public BattleState getInvisibleState() {
        return InvisibleState;
    }
    public BattleState getInvincibleState() {
        return InvincibleState;
    }

    public void fight(MovingEntity p1, MovingEntity p2) {
        state.fight(p1, p2);
    }


}