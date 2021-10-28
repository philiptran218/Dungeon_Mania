package dungeonmania.StaticEntities;

import dungeonmania.util.Position;

public class FloorSwitch extends StaticEntity {
    private boolean triggered = false;

    public FloorSwitch(String id, String type, Position pos) {
        super(id, type, pos);
        super.setCanStandOn(true);
<<<<<<< HEAD
=======
        super.setType("switch");
>>>>>>> master
    }
    
    public boolean isTriggered() {
        return triggered;
    }

    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }
}
