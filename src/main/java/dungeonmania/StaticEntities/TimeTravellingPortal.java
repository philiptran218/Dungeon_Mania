package dungeonmania.StaticEntities;

import java.util.List;

import dungeonmania.Entity;
import dungeonmania.gamemap.GameMap;
import dungeonmania.util.Position;

public class TimeTravellingPortal extends StaticEntity {
    GameMap map;

    public TimeTravellingPortal(String id, String type, Position pos, GameMap map) {
        super(id, type, pos);
        this.map = map;
    }
    
    /**
     * Give position of entity checks if it is passing through a time travelling
     * portal.
     * @param pos
     * @return True is the player is moving into the portal, false otherwise.
     */
    public static boolean movingIntoTimePortal(List<Entity> timeTelePortal) {
        for (Entity e : timeTelePortal) {
            if (e.isType("time_travelling_portal")) { return true; }
        }
        return false;
    }

    public static Integer calcualteTickBack(int tick) {
        if (tick >= 30) {
            return 30;
        }
        return tick;
    }
}