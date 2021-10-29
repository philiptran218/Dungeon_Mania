package dungeonmania;

import dungeonmania.MovingEntities.MovingEntity;
import dungeonmania.MovingEntities.Player;
import dungeonmania.StaticEntities.Boulder;
import dungeonmania.StaticEntities.FloorSwitch;
import dungeonmania.StaticEntities.Portal;
import dungeonmania.StaticEntities.StaticEntity;
import dungeonmania.StaticEntities.ZombieToastSpawner;
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
     * Returns a dungeon response based on the current state of the game.
     * @return DungeonResponse on the current state of map.
     */
    public DungeonResponse returnDungeonResponse() {
        return new DungeonResponse(gameMap.getMapId(), gameMap.getDungeonName(), gameMap.mapToListEntityResponse(), 
            gameMap.inventoryToItemResponse(), new ArrayList<String>(), "Goals");
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
        return new ArrayList<>();
    }

    public DungeonResponse tick(String itemUsed, Direction movementDirection) throws IllegalArgumentException, InvalidActionException {
        // Position of the zombie spawner
        Position zombieSpawner;

        Position dir = movementDirection.getOffset();
        // Position in front of player
        Position checkPosition;
        Entity tempEntity = gameMap.getMap().get(checkPosition).get(1);
        // Player position
        Position playerPosition;
        // ADD PLAYER MOVEMENT
        if (tempEntity.getType() == "boulder") {
            Position inFrontOfCheckPosition = new Position(checkPosition.getX() + dir.getX(),checkPosition.getY() + dir.getY(), 0);
            List <Entity> tempList = gameMap.getMap().get(inFrontOfCheckPosition);
            if (tempList.get(1) == null && tempList.get(3) == null) {
                tempList.add(1, tempEntity);
                gameMap.getMap().get(checkPosition).remove(tempEntity);
            }
        }
        // PROBABLY NOT NEEDED
        // if (tempEntity.getType() == "switch") {
        //     List <Entity> tempList = gameMap.getMap().get(checkPosition);
        //     if (tempList.get(1).getType() == "boulder") {
        //         Position inFrontOfCheckPosition = new Position(checkPosition.getX() + dir.getX(),checkPosition.getY() + dir.getY(), 0);
        //         List <Entity> entitiesOnPosition = gameMap.getMap().get(inFrontOfCheckPosition);
        //         if (entitiesOnPosition.get(1) == null && entitiesOnPosition.get(3) == null) {
        //             Entity newBoulder = tempList.get(1);
        //             gameMap.getMap().get(inFrontOfCheckPosition).add(1, newBoulder);
        //             gameMap.getMap().get(checkPosition).remove(newBoulder);
        //         }
        //         else if (entitiesOnPosition.get(0).getType() == "switch" && entitiesOnPosition.get(1) == null) {
        //             Entity newBoulder = tempList.get(1);
        //             gameMap.getMap().get(inFrontOfCheckPosition).add(1, newBoulder);
        //             gameMap.getMap().get(checkPosition).remove(newBoulder);
        //         }
        //     }
        // }
        if (tempEntity.getType() == "portal") {
            Portal portal = (Portal) tempEntity;
            Position teleportLocation = portal.getTeleportLocation();
            // Add code for playermovement
        }
        // Check inventory in item.
        /*
        if (gameMap.getPlayer().getInventory().getItem(itemUsed) == null) {

        }*/
        // Move the player:
        gameMap.setObservers();
        gameMap.getPlayer().move(gameMap.getMap(), movementDirection);

        // Move all the moving entities by one tick:
        for (MovingEntity e : gameMap.getMovingEntityList()) {
            e.move(gameMap.getMap());
        }
        // Return DungeonResponse
        return returnDungeonResponse();
    }

    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        boolean isValid = false;
        Position entityPosition = null;
        String type = "";
        for (Position key : gameMap.getMap().keySet()) {
            for (Entity counter : gameMap.getMap().get(key)) {
                if (counter.getId() == entityId) {
                    if (counter.getType() == "zombie_toast_spawner" || counter.getType() == "mercenary") {
                        isValid = true;
                        entityPosition = key;
                        type = counter.getType();
                    }
                }
            }
        }
        if (isValid == false) {
            throw new IllegalArgumentException();
        }
        // If the player wants to destroy the zombie toast spawner
        if (type == "zombie_toast_spawner") {
            Player playerEntity = null;
            boolean isAdjacent = false;
            // Checks if the interaction is valid
            ZombieToastSpawner spawner = (ZombieToastSpawner) gameMap.getMap().get(entityPosition).get(1);
            playerEntity = spawner.canSpawnerBeDestroyed(entityPosition, gameMap.getMap(), isAdjacent, playerEntity);
            if (isAdjacent == false) {
                throw new InvalidActionException("Player is not cardinally adjacent to spawner");
            }
            if (playerEntity.getInventory().getItem("sword") == null) {
                throw new InvalidActionException("Player does not have a weapon");
            }
            // Destroys the zombie toast spawner
            gameMap.getMap().get(entityPosition).remove(1);
        }
        return returnDungeonResponse();
    }

    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        return null;
    }

}