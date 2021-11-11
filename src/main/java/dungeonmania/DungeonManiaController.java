package dungeonmania;

import dungeonmania.MovingEntities.*;
import dungeonmania.StaticEntities.*;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.gamemap.EnermySpawner;
import dungeonmania.gamemap.GameMap;
import dungeonmania.gamemap.MapUtility;
import dungeonmania.gamemap.ResponseUtility;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class DungeonManiaController {
    // Game Map
    private GameMap gameMap;
    private EnermySpawner enermySpawner;

    /**
     * Empty Constructor
     */
    public DungeonManiaController() {
    }

    /**
     * Get the json file with all pixel for entities.
     * @return
     */
    public String getSkin() {
        return "default";
    }

    /**
     * Language intialisation.
     * @return String
     */
    public String getLocalisation() {
        return "en_US";
    }

    /**
     * Returns a list of game modes.
     * @return List<String> List of all game modes.
     */
    public List<String> getGameModes() {
        return Arrays.asList("standard", "peaceful", "hard");
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
        try {
            String jsonString = FileLoader.loadResourceFile("/dungeons/" + fileName + ".json");
            return new Gson().fromJson(jsonString, JsonObject.class);
        } catch (Exception e) {
                throw new IllegalArgumentException("File not found.");
        }
    }
    
    /**
     * Creates a new game in the ManiaController, and throw IllegalArgumentException
     * if the dungeon name is not valid or game mode given is not valid.
     * @param dungeonName (String)
     * @param gameMode (String)
     * @return DungeonResponse
     * @throws IllegalArgumentException
     */
    public DungeonResponse newGame(String dungeonName, String gameMode) throws IllegalArgumentException {
        if (!getGameModes().contains(gameMode.toLowerCase())) {
            throw new IllegalArgumentException("Game mode does not exist.");
        }
        // Set game index to zero
        // Set map:
        this.gameMap = new GameMap(gameMode, dungeonName, getJsonFile(dungeonName));
        // New directory
        File theDir = new File("time_travel_record/" + gameMap.getMapId());
        if (!theDir.exists()){ theDir.mkdirs(); }
        // Tick save
        MapUtility.saveTickInstance(gameMap, gameMap.getGameIndex().toString());
        // Create enermy spawner
        this.enermySpawner = new EnermySpawner(gameMap);
        // Return DungeonResponse
        return new ResponseUtility(gameMap).returnDungeonResponse();
    }
    
    /**
     * Save the the current game that is running with the given name.
     * @param name (String)
     * @return DungeonResponse
     * @throws IllegalArgumentException
     */
    public DungeonResponse saveGame(String name) throws IllegalArgumentException {
        // Advanced 
        MapUtility.saveMapAsJson(gameMap, name);
        // Return DungeonResponse
        return new ResponseUtility(gameMap).returnDungeonResponse();
    }

    /**
     * Load the game with the given name, throws IllegalArgumentException
     * if the name provided is not a saved game.
     * @param name (String)
     * @return DungeonResponse
     * @throws IllegalArgumentException
     */
    public DungeonResponse loadGame(String name) throws IllegalArgumentException {
        JsonObject obj = MapUtility.getSavedMap(name, null);
        this.gameMap = new GameMap(name, obj.get("map-id").getAsString());
        // Create enermy spawner
        this.enermySpawner = new EnermySpawner(gameMap);
        // Return DungeonResponse
        System.out.println(new ResponseUtility(gameMap).returnDungeonResponse().getInventory().get(0).getType());
        return new ResponseUtility(gameMap).returnDungeonResponse();
    }

    /**
     * Returns a list of all saved sames.
     * @return List<String> List of saved games.
     */
    public List<String> allGames() {
        try {
            return FileLoader.listFileNamesInDirectoryOutsideOfResources("saved_games");
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public List<String> allSavedTicks() {
        try {
            return FileLoader.listFileNamesInDirectoryOutsideOfResources("time_travel_record");
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }


    /**
     * For every entity on the map, update its status whether it is moving the 
     * entity or static entity exibiting its behaviour. Throws exception when 
     * the item used is not valid.
     * @param itemUsed (String)
     * @param movementDirection (Direction)
     * @return DungeonResponse
     * @throws IllegalArgumentException
     * @throws InvalidActionException
     * @throws CloneNotSupportedException
     */
    public DungeonResponse tick(String itemUsed, Direction movementDirection) throws IllegalArgumentException, InvalidActionException {
        // If itemUsed is NULL move the player:
        if (itemUsed == null && !MapUtility.entityOnASwampTile(gameMap, null)) {
            gameMap.getPlayer().move(gameMap.getMap(), movementDirection);
        } else if (itemUsed != null) {
            // Get the entity on map:
            gameMap.getPlayer().useItem(gameMap.getMap(), itemUsed);
        }

        // Ticks the duration of any active potions
        gameMap.getPlayer().tickPotions();
        
        // Move all the moving entities by one tick:
        for (MovingEntity e : gameMap.getMovingEntityList()) {
            if (!(e.getPos().equals(e.getPlayerPos()) && !e.isType("mercenary")) && !MapUtility.entityOnASwampTile(gameMap, e.getId())) {
                e.move(gameMap.getMap());
            }
        }
        
        // Player battles enemies on the same tile
        List<MovingEntity> removeEntity = new ArrayList<>();
        for (MovingEntity e : gameMap.getMovingEntityList()) { 
            if (e instanceof Mercenary) {
                Mercenary merc = (Mercenary) e;
                if (e.getPos().equals(e.getPlayerPos()) && !merc.isAlly()) {
                    removeEntity.add(gameMap.getBattle().fight(gameMap.getPlayer(), e));
                }
            }
            else {
                if (e.getPos().equals(gameMap.getPlayer().getPos()) && !(e instanceof Player)) {
                    removeEntity.add(gameMap.getBattle().fight(gameMap.getPlayer(), e));
                }
            }
        }
        if (!removeEntity.contains(null)) {
            // Remove dead entities from list after battle is finished
            // Remove the entity from the map:
            for (Entity e : removeEntity) {
                gameMap.getMap().get(e.getPos()).remove(e);
            }

        }
        
        // Ticks the zombie toast spawner
        for (Map.Entry<Position, List<Entity>> entry : gameMap.getMap().entrySet()) {
            for(Entity e : entry.getValue()) {
                if (e.getType().equals("zombie_toast_spawner")) {
                    ((ZombieToastSpawner) e).tick(e.getPos(), gameMap.getMap(), gameMap.getGameState());
                }
            }
        }

        // Spawn mobs on the map
        enermySpawner.spawnMob();

        // Check for swamp tile after all movements has occured,
        // and removes accordinly as well as tick each one.
        MapUtility.tickAllSwampTiles(gameMap);

        // Save the file:
        gameMap.incrementGameIndex();
        MapUtility.saveTickInstance(gameMap, gameMap.getGameIndex().toString());

        // Return DungeonResponse
        return new ResponseUtility(gameMap).returnDungeonResponse();
    }

    /**
     * If an entity is clicked on the frontend and the entity is interactable
     * passes its id into this function and perform actions as required based 
     * on which interactable entity it is.
     * @param entityId (String)
     * @return DungeonResponse
     * @throws IllegalArgumentException
     * @throws InvalidActionException
     */
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

        return new ResponseUtility(gameMap).returnDungeonResponse();
    }

    /**
     * Given a buildable item, build the item using the material in the player's
     * inventory and removes the items used and add the item crafted into 
     * the player's inventory.
     * @param buildable (String)
     * @return DungeonResponse
     * @throws IllegalArgumentException
     * @throws InvalidActionException
     */
    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        // Checks if item being built is a bow, shield, sceptre or midnight_armour
        if (isNotValidBuildable(buildable)) {
            throw new IllegalArgumentException();
        }
        Inventory playerInv = gameMap.getPlayer().getInventory();
        // Checks if player has enough materials to build the item
        if (!playerInv.hasEnoughMaterials(buildable)) {
            throw new InvalidActionException("Not enough materials!");
        }
        Boolean hasZombie = gameMap.getMovingEntityList().stream().anyMatch(e -> e.getType().equals("zombie_toast"));
        // Checks if there are zombies in map while building midnight armour
        if (buildable.equals("midnight_armour") && hasZombie) {
            throw new InvalidActionException("Cannot build midnight armour. Zombies are present.");
        }

        // Player can then build the item
        playerInv.buildItem(buildable);
        return new ResponseUtility(gameMap).returnDungeonResponse();
    }

    private boolean isNotValidBuildable(String buildable) {
        return !(buildable.equals("bow") || buildable.equals("shield") || buildable.equals("sceptre")
                 || buildable.equals("midnight_armour"));
    }

    /**
     * Given a number of ticks, rewind the game the number of time 
     * the tick specified.
     * @param ticks
     * @return DungeonResponse
     * @throws IllegalArgumentException
     */
    public DungeonResponse rewind(int ticks) throws IllegalArgumentException {
        // Save game after each instance and load what you need.
        if (ticks <= 0) { throw new IllegalArgumentException("Invalid rewind tick."); }
        // If not enough rewind, do not do anything
        Integer gameIndex = gameMap.getGameIndex();
        if (gameIndex < ticks || gameIndex == 0) { 
            return new ResponseUtility(gameMap).returnDungeonResponse(); 
        }
        // Rewind
        for (int i = 0; i < ticks; i++) {
            gameIndex -= 1;
        }
        // Load new game
        gameMap = new GameMap(gameIndex.toString(), gameMap.getMapId());
        MapUtility.addOldPlayer(gameMap);
        // Return response
        return new ResponseUtility(gameMap).returnDungeonResponse();
    }
}
