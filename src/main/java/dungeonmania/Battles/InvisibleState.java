package dungeonmania.Battles;

import dungeonmania.MovingEntities.*;

public class InvisibleState implements BattleState {

    /**
     * Since the player is invisible, no fighting occurs.
     */
    public MovingEntity fight(Player p1, MovingEntity p2) {
        return null;
    }
}
