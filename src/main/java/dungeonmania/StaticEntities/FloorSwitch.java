package dungeonmania.StaticEntities;

import dungeonmania.util.Position;

public class FloorSwitch extends StaticEntity {
    private boolean triggered = false;

    public FloorSwitch(String id, String type, Position pos) {
        super(id, type, pos);
        super.setCanStandOn(true);
        super.setType("FloorSwitch");
    }
    
    public boolean isTriggered() {
        return triggered;
    }

    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }
}
