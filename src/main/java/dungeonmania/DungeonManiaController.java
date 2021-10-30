package dungeonmania;

import dungeonmania.MovingEntities.*;
import dungeonmania.StaticEntities.*;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.gamemap.GameMap;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
     * Returns a dungeon response based on the current state of the game.
     * @return DungeonResponse on the current state of map.
     */
    public DungeonResponse returnDungeonResponse() {
        return new DungeonResponse(gameMap.getMapId(), gameMap.getDungeonName(), gameMap.mapToListEntityResponse(), 
            gameMap.inventoryToItemResponse(), new ArrayList<String>(), gameMap.getGoals());
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
        // Return DungeonResponse
        return returnDungeonResponse();
    }
    
    public DungeonResponse saveGame(String name) throws IllegalArgumentException {
        // Advanced 
        this.gameMap.saveMapAsJson(name);
        // Return DungeonResponse
        return returnDungeonResponse();
    }

    public DungeonResponse loadGame(String name) throws IllegalArgumentException {
        this.gameMap = new GameMap(name);
        // Return DungeonResponse
        return returnDungeonResponse();
    }

    public List<String> allGames() {
        try {
            return FileLoader.listFileNamesInResourceDirectory("/saved_games");
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public DungeonResponse tick(String itemUsed, Direction movementDirection) throws IllegalArgumentException, InvalidActionException {
        // Ticks the zombie toast spawner
        for (Map.Entry<Position, List<Entity>> entry : gameMap.getMap().entrySet()) {
            for(Entity e : entry.getValue()) {
                if (e.getType().equals("zombie_toast_spawner")) {
                    ((ZombieToastSpawner) e).tick(e.getPos(), gameMap.getMap(), gameMap.getGameState());
                }
            }
        }
        // If itemUsed is NULL move the player:
        if (itemUsed == null) {
            gameMap.getPlayer().move(gameMap.getMap(), movementDirection);
        } else {
            // Get the entity on map:
            Entity c = gameMap.getPlayer().getInventory().getItemById(itemUsed);
            
            if (!getUsableItems().contains(c.getType())) {
                throw new IllegalArgumentException("Cannot use item.");
            }

            // Check inventory in item.
            if (!gameMap.getPlayer().hasItem(c.getType())) {
                throw new InvalidActionException("Player does not have the item.");
            }
        }


        // Move all the moving entities by one tick:
        for (MovingEntity e : gameMap.getMovingEntityList()) {
            e.move(gameMap.getMap());
        }
        // Return DungeonResponse
        return returnDungeonResponse();
    }

    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        // Checks if the entity is on the map.
        if (gameMap.getEntityOnMap(entityId) == null) {
            throw new IllegalArgumentException("Entity does not exist.");
        }
        Entity e = gameMap.getEntityOnMap(entityId);

        if (e.getType().equals("mercenary")) {
            gameMap.getPlayer().bribeMercenary(gameMap.getMap(), (Mercenary) e);
        } else if (e.getType().equals("zombie_toast_spawner")) {
            gameMap.getPlayer().attackZombieSpawner(gameMap.getMap(), (ZombieToastSpawner) e);
        } else {
            throw new IllegalArgumentException("Entity not interactable");
        }

        return returnDungeonResponse();
    }

    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        return null;
    }
}