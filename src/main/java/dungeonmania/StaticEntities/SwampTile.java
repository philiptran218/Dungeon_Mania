package dungeonmania.StaticEntities;

import dungeonmania.util.Position;

public class SwampTile extends StaticEntity {
int factor;

    public SwampTile(String id, String type, Position pos, int factor) {
        super(id, type, pos);
        this.factor = factor;
    }

    // Getters and Setters
    public int getFactor() {
        return factor;
    }

    public void setFactor(int factor) {
        this.factor = factor;
    }
    
}