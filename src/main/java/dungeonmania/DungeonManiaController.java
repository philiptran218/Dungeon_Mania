package dungeonmania;

import dungeonmania.StaticEntities.Boulder;
import dungeonmania.StaticEntities.FloorSwitch;
import dungeonmania.StaticEntities.Portal;
import dungeonmania.StaticEntities.StaticEntity;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.gamemap.GameMap;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonArray;
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
        // Cahnge the name
        JsonObject jsonMap = getJsonFile("exit");

        // ASSIGN THE MAP STORES BY THE CONTROLLER HERE :: JUST FOR TESTING:
        GameMap map = new GameMap(gameMode, jsonMap);

        return new DungeonResponse("dungeonId", dungeonName, map.mapToListEntityResponse(jsonMap), new ArrayList<ItemResponse>(), new ArrayList<String>(), "goals");
    }
    
    public DungeonResponse saveGame(String name) throws IllegalArgumentException {
        return null;
    }

    public DungeonResponse loadGame(String name) throws IllegalArgumentException {
        return null;
    }

    public List<String> allGames() {
        return new ArrayList<>();
    }

    public DungeonResponse tick(String itemUsed, Direction movementDirection) throws IllegalArgumentException, InvalidActionException {
        // Position of the zombie spawner
        Position zombieSpawner;

        Position dir = movementDirection.getOffset();
        // Position in front of player
        Position checkPosition;
        Entity tempEntity = getMap().get(checkPosition).get(0);
        // Player position
        Position playerPosition;
        if (tempEntity.getType() == "boulder") {
            Position newPosition = new Position(checkPosition.getX() + dir.getX(),checkPosition.getY() + dir.getY(), 0);
            List <Entity> tempList = getMap().get(newPosition);
            if (tempList.get(0) == null) {
                tempList.add(tempEntity);
                getMap().get(checkPosition).remove(tempEntity);
            }
            else if (tempList.get(0).getType() == "switch" && tempList.get(1) == null) {
                tempList.add(tempEntity);
                getMap().get(checkPosition).remove(tempEntity);
            }
        }
        // ADD PLAYER MOVEMENT
        if (tempEntity.getType() == "switch") {
            Position inFrontOfCheckPosition;
            List <Entity> tempList = getMap().get(inFrontOfCheckPosition);
            if (tempList.get(1).getType() == "boulder") {
                Position newPosition = new Position(inFrontOfCheckPosition.getX() + dir.getX(),inFrontOfCheckPosition.getY() + dir.getY(), 0);
                List <Entity> entitiesOnPosition = gameMap.getMap().get(newPosition);
                if (entitiesOnPosition.get(0) == null) {
                    getMap().get(newPosition).add(tempEntity);
                    getMap().get(inFrontOfCheckPosition).remove(tempEntity);
                }
                else if (entitiesOnPosition.get(0).getType() == "switch" && entitiesOnPosition.get(1) != null) {
                    getMap().get(newPosition).add(tempEntity);
                    getMap().get(inFrontOfCheckPosition).remove(tempEntity);
                }
            }
        }
        if (tempEntity.getType() == "portal") {
            Portal portal = (Portal) tempEntity;
            Position teleportLocation = portal.getTeleportLocation();
            // Add code for playermovement
        }
        return null;
    }

    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        return null;
    }

    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        return null;
    }

}