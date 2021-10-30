package dungeonmania.Battles;

import dungeonmania.MovingEntities.MovingEntity;
import dungeonmania.MovingEntities.Player;

public interface BattleState {

    public MovingEntity fight(Player p1, MovingEntity p2);

}