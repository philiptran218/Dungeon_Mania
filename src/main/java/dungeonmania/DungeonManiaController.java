package dungeonmania;

import dungeonmania.MovingEntities.MovingEntity;
import dungeonmania.MovingEntities.Player;
import dungeonmania.MovingEntities.ZombieToast;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.gamemap.GameMap;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DungeonManiaController {
    private GameMap gameMap;

    public DungeonManiaController() {
    }

    public String getSkin() {
        return "default";
    }

    public String getLocalisation() {
        return "en_US";
    }

    public List<String> getGameModes() {
        return Arrays.asList("Standard", "Peaceful", "Hard");
    }

    public List<String> getUsableItems() {
        return Arrays.asList("bomb", "health_potion", "invincibility_potion", "invisibility_potion", null);
    }

    /**
     * /dungeons
     * 
     * Done for you.
     */
    public static List<String> dungeons() {
        try {
            return FileLoader.listFileNamesInResourceDirectory("/dungeons");
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Given a file name it will go to the source folder and locate dungeon map,
     * and if not found it will go into the test tolder to locate the test json 
     * file and return it as a json object.
     * @return Dungeon Map as JsonObject
     */
    public JsonObject getJsonFile(String fileName) {
        // "src\\main\\resources\\dungeons\\" + dungeonName + ".json"
        try {
            return JsonParser.parseReader(new FileReader("src\\main\\resources\\dungeons\\" + fileName + ".json")).getAsJsonObject();
        } catch (Exception e) {
            try {
                return JsonParser.parseReader(new FileReader("src\\test\\resources\\dungeons\\" + fileName + ".json")).getAsJsonObject();
            } catch (Exception r) {
                throw new IllegalArgumentException("File not found.");
            }
        }
    }
    
    public DungeonResponse newGame(String dungeonName, String gameMode) throws IllegalArgumentException {
        if (!getGameModes().contains(gameMode)) {
            throw new IllegalArgumentException("Game mode does not exist.");
        }
        // Set map:
        this.gameMap = new GameMap(gameMode, dungeonName, getJsonFile(dungeonName));

        return new DungeonResponse(gameMap.getMapId(), gameMap.getDungeonName(), gameMap.mapToListEntityResponse(), 
            new ArrayList<ItemResponse>(), new ArrayList<String>(), "Goals");
    }
    
    public DungeonResponse saveGame(String name) throws IllegalArgumentException {
        // Advanced 
        this.gameMap.saveMapAsJson(name);

        return new DungeonResponse(gameMap.getMapId(), gameMap.getDungeonName(), gameMap.mapToListEntityResponse(), 
            new ArrayList<ItemResponse>(), new ArrayList<String>(), "Goals");
    }

    public DungeonResponse loadGame(String name) throws IllegalArgumentException {
        this.gameMap = new GameMap(name);

        return new DungeonResponse(gameMap.getMapId(), gameMap.getDungeonName(), gameMap.mapToListEntityResponse(), 
            new ArrayList<ItemResponse>(), new ArrayList<String>(), "Goals");
    }

    public List<String> allGames() {
        try {
            return FileLoader.listFileNamesInResourceDirectory("/saved_games");
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public DungeonResponse tick(String itemUsed, Direction movementDirection) throws IllegalArgumentException, InvalidActionException {
        if (!getUsableItems().contains(itemUsed)) {
            throw new IllegalArgumentException("Invalid item used.");
        }
        // Check inventory in item.
        // ***********************
        // Move the player:
        gameMap.getPlayer().move(gameMap.getMap(), movementDirection);

        // Move all the moving entities by one tick:
        for (MovingEntity e : gameMap.getMovingEntityList()) {
            if (e.getType().equals("zombie_toast")) {
                ((ZombieToast) e).move(gameMap.getMap());
            }
        }
        return new DungeonResponse(gameMap.getMapId(), gameMap.getDungeonName(), gameMap.mapToListEntityResponse(), 
            new ArrayList<ItemResponse>(), new ArrayList<String>(), "Goals");
    }

    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        return null;
    }

    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        return null;
    }
}