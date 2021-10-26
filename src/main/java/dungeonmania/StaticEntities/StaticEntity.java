package dungeonmania.StaticEntities;

public abstract class StaticEntity {
    private boolean canStandOn;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getCanStandOn() {
        return canStandOn;
    }

    public void setCanStandOn(boolean canStandOn) {
        this.canStandOn = canStandOn;
    }

}
