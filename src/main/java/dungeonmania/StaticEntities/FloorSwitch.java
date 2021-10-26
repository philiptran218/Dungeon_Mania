package dungeonmania.StaticEntities;

public class FloorSwitch extends StaticEntity {
    private boolean triggered = false;

    public FloorSwitch() {
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
