package dungeonmania.StaticEntities;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Boulder extends StaticEntity {
    /**
     * Constructor for Boulder
     */
    public Boulder(String id, String type, Position pos) {
        super(id, type, pos);
        super.setCanStandOn(false);
    }
    // If a spider and boulder is about to move into the same spot at the same time.  

    public void push(Direction direction, Position position) {
        // Position dir = direction.getOffset();
        // Position newPosition = new Position(position.getX() + dir.getX(),position.getY() + dir.getY(), 1);
    }
}
