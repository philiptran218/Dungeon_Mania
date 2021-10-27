package dungeonmania.StaticEntities;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public abstract class StaticEntity extends Entity {
    private boolean canStandOn;

    public StaticEntity(String id, String type, Position pos) {
        super(id, type, pos);
    }

    public boolean getCanStandOn() {
        return canStandOn;
    }

    public void setCanStandOn(boolean canStandOn) {
        this.canStandOn = canStandOn;
    }

}
