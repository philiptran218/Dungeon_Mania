package dungeonmania.StaticEntities;

import java.util.List;
import java.util.Map;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public class FloorSwitch extends StaticEntity {
    private boolean triggered = false;

    public FloorSwitch(String id, String type, Position pos) {
        super(id, type, pos);
        super.setCanStandOn(true);
        super.setType("switch");
    }
    
    public boolean isTriggered() {
        return triggered;
    }

    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }

    public boolean isUnderBoulder(Map<Position, List<Entity>> map) {
        Position pos = super.getPos();
        List<Entity> entities = map.get(new Position(pos.getX(), pos.getY(), 1));
        if (entities.isEmpty()) {
            return false;
        }
        if (entities.get(0) instanceof Boulder) {
            return true;
        } else {
            return false;
        }
    }
}
