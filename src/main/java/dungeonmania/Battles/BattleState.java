package dungeonmania.Battles;

import dungeonmania.MovingEntities.MovingEntity;

public interface BattleState {

    public void fight(MovingEntity e1, MovingEntity e2);

}