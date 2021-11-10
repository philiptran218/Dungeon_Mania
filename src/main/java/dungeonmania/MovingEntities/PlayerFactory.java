package dungeonmania.MovingEntities;

import com.google.gson.JsonObject;

import dungeonmania.gamemap.GameState;
import dungeonmania.util.Position;

public class PlayerFactory {
    private static final double PLAYER_DAMAGE = 2;

    public static Player getPlayer (String id, String type, Position pos, JsonObject jsonObj, GameState state) {
		if (jsonObj.get("health") != null) {
            double health = jsonObj.get("health").getAsDouble();
            return new Player(id, type, pos, health, state.getPlayerMaxHealth(),PLAYER_DAMAGE);
        } else {
            return new Player(id, type, pos, state.getPlayerMaxHealth(), state.getPlayerMaxHealth(), PLAYER_DAMAGE);
        }
	}
}
