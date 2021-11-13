package dungeonmania;

import dungeonmania.MovingEntities.*;
import dungeonmania.StaticEntities.*;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.gamemap.DungeonGenerator;
import dungeonmania.gamemap.EnermySpawner;
import dungeonmania.gamemap.GameMap;
import dungeonmania.response.models.AnimationQueue;
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

import javax.swing.event.SwingPropertyChangeSupport;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class DungeonManiaController {
    // Game Map
    private GameMap gameMap;
    private EnermySpawner enermySpawner;
    private List<AnimationQueue> animations = new ArrayList<>();


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
        animations.clear();
        if (!getGameModes().contains(gameMode.toLowerCase())) {
            throw new IllegalArgumentException("Game mode does not exist.");
        }
        // Set game index to zero
        // Set map:
        this.gameMap = new GameMap(gameMode, dungeonName, getJsonFile(dungeonName));
        AnimationUtility.initialiseHealthBarForAllEntities(animations, gameMap.getPlayer(), gameMap.getMovingEntityList(), false);
        // New directory
        File theDir = new File("time_travel_record/" + gameMap.getMapId());
        if (!theDir.exists()){ theDir.mkdirs(); }
        // Tick save
        MapUtility.saveTickInstance(gameMap, gameMap.getGameIndex().toString());
        // Create enermy spawner
        this.enermySpawner = new EnermySpawner(gameMap);
        // Return DungeonResponse
        return new ResponseUtility(gameMap).returnDungeonResponse(animations);
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
        return new ResponseUtility(gameMap).returnDungeonResponse(animations);
    }

    /**
     * Load the game with the given name, throws IllegalArgumentException
     * if the name provided is not a saved game.
     * @param name (String)
     * @return DungeonResponse
     * @throws IllegalArgumentException
     */
    public DungeonResponse loadGame(String name) throws IllegalArgumentException {
        animations.clear();
        if (MapUtility.getSavedMap(name, null) == null) {
            throw new IllegalArgumentException("File not found.");
        }
        JsonObject obj = MapUtility.getSavedMap(name, null);
        this.gameMap = new GameMap(name, obj.get("map-id").getAsString());
        // Create enermy spawner
        this.enermySpawner = new EnermySpawner(gameMap);
        AnimationUtility.initialiseHealthBarForAllEntities(animations, gameMap.getPlayer(), gameMap.getMovingEntityList(), true);
        // Return DungeonResponse
        return new ResponseUtility(gameMap).returnDungeonResponse(animations);
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
        animations.clear();
        AnimationUtility.setPlayerHealthBar(animations, gameMap.getPlayer());
        // If itemUsed is NULL move the player:
        if (itemUsed == null && !MapUtility.entityOnASwampTile(gameMap, null)) {
            gameMap.getPlayer().move(gameMap.getMap(), movementDirection, animations);
        } else if (itemUsed != null) {
            // Get the entity on map:
            gameMap.getPlayer().useItem(gameMap.getMap(), itemUsed);
        }

        // Ticks the duration of any active potions
        gameMap.getPlayer().tickPotions();
        // Move all the moving entities by one tick:
        for (MovingEntity e : gameMap.getMovingEntityList()) {
            if (!(e.getPos().equals(e.getPlayerPos()) && !e.isType("mercenary")) && !MapUtility.entityOnASwampTile(gameMap, e.getId())) {
                AnimationUtility.setMovingEntityHealthBar(animations, e);
                e.move(gameMap.getMap(), animations);
            }
            
            Integer nextIndex = gameMap.getGameIndex() + 1;
            if (e.isType("older_player") && MapUtility.getSavedMap(nextIndex.toString(), gameMap.getMapId()) == null) {
                // Remove older player
                gameMap.getMap().get(e.getPos()).remove(e);
            } else if (e.isType("older_player") && MapUtility.findOlderPlayerMoveDirection(gameMap) != null) {
                ((Player) e).move(gameMap.getMap(), MapUtility.findOlderPlayerMoveDirection(gameMap), animations);
            }
        }
        
        // Player battles enemies on the same tile
        List<MovingEntity> removeEntity = new ArrayList<>();
        for (MovingEntity e : gameMap.getMovingEntityList()) { 
            if (e.isType("mercenary") || e.isType("assassin")) {
                Mercenary merc = (Mercenary) e;
                if (e.getPos().equals(e.getPlayerPos()) && !merc.isAlly()) {
                    removeEntity.add(gameMap.getBattle().fight(gameMap.getPlayer(), e));
                }
            }
            else {
                if (e.getPos().equals(gameMap.getPlayer().getPos())) {
                    if (e.isType("older_player")) {
                        boolean hasMidNight = (((Player) e).getInventory().getItem("midnight_armour") != null);
                        boolean hasSunStone = (((Player) e).getInventory().getItem("sun_stone") != null);
                        if (!(hasMidNight || hasSunStone)) {
                            removeEntity.add(gameMap.getBattle().fight(gameMap.getPlayer(), e)); 
                        }
                    } else {
                        removeEntity.add(gameMap.getBattle().fight(gameMap.getPlayer(), e)); 
                    }
                }
            }
        }
        if (!removeEntity.isEmpty()) {
            AnimationUtility.setPlayerHealthBarAfterBattle(animations, gameMap.getPlayer());
            AnimationUtility.shakeHealthBar(animations, gameMap.getPlayer());
        }
        if (!removeEntity.contains(null)) {
            // Remove dead entities from list after battle is finished
            // Remove the entity from the map:
            for (Entity e : removeEntity) {
                gameMap.getMap().get(e.getPos()).remove(e);
            }

        }
        // Ticks the sceptre effect on allies after battling has ended
        gameMap.getPlayer().tickAllies();
        
        // Ticks the zombie toast spawner
        for (Map.Entry<Position, List<Entity>> entry : gameMap.getMap().entrySet()) {
            for(Entity e : entry.getValue()) {
                if (e.getType().equals("zombie_toast_spawner")) {
                    ((ZombieToastSpawner) e).tick(e.getPos(), gameMap.getMap(), gameMap.getGameState(), animations);
                }
            }
        }

        // Spawn mobs on the map
        enermySpawner.spawnMob(animations);

        // Check for swamp tile after all movements has occured,
        // and removes accordinly as well as tick each one.
        MapUtility.tickAllSwampTiles(gameMap);

        // Save the file:
        gameMap.incrementGameIndex();
        MapUtility.saveTickInstance(gameMap, gameMap.getGameIndex().toString());

        // Return DungeonResponse
        return new ResponseUtility(gameMap).returnDungeonResponse(animations);
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
        } else if (e.getType().equals("assassin")) {
            gameMap.getPlayer().bribeAssassin(gameMap.getMap(), (Assassin) e);
        } else if (e.getType().equals("zombie_toast_spawner")) {
            gameMap.getPlayer().attackZombieSpawner(gameMap.getMap(), (ZombieToastSpawner) e);
        } else {
            throw new IllegalArgumentException("Entity not interactable");
        }

        return new ResponseUtility(gameMap).returnDungeonResponse(animations);
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
        if (!validBuildables().contains(buildable)) {
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
        return new ResponseUtility(gameMap).returnDungeonResponse(animations);
    }

    public List<String> validBuildables() {
        return Arrays.asList("bow", "shield", "sceptre", "midnight_armour");
    }

    /**
     * Given a number of ticks, rewind the game the number of time 
     * the tick specified.
     * @param ticks
     * @return DungeonResponse
     * @throws IllegalArgumentException
     */
    public DungeonResponse rewind(int ticks) throws IllegalArgumentException {
        animations.clear();
        // Track which tick we need to get to
        gameMap.setDestinationTick(gameMap.getGameIndex());
        // Save game after each instance and load what you need.
        if (ticks <= 0) { throw new IllegalArgumentException("Invalid rewind tick."); }
        // If not enough rewind, do not do anything
        Integer gameIndex = gameMap.getGameIndex();
        if (gameIndex < ticks || gameIndex == 0) {
            return new ResponseUtility(gameMap).returnDungeonResponse(animations); 
        }
        // Rewind
        for (int i = 0; i < ticks; i++) {
            gameIndex -= 1;
        }
        // Load new game
        gameMap = new GameMap(gameIndex.toString(), gameMap.getMapId());
        AnimationUtility.initialiseHealthBarForAllEntities(animations, gameMap.getPlayer(), gameMap.getMovingEntityList(), true);
        MapUtility.addOldPlayer(gameMap);
        // Return response
        return new ResponseUtility(gameMap).returnDungeonResponse(animations);
    }

    /**
     * Generate a Dungeon maze with a start and exit Position, and create the game
     * @param xStart x coordinate of player spawning position
     * @param yStart y coordinate of player spawning position
     * @param xEnd x coordinate of the exit
     * @param yEnd y coordinate of the exit
     * @param gameMode difficulty of the game
     * @return DungeonResponse
     * @throws IllegalArgumentException
     */
    public DungeonResponse generateDungeon(int xStart, int yStart, int xEnd, int yEnd, String gameMode) throws IllegalArgumentException {
        if (!getGameModes().contains(gameMode.toLowerCase())) {
            throw new IllegalArgumentException("Game mode does not exist.");
        }
        // Create the dungeon
        DungeonGenerator.generate(xStart, yStart, xEnd, yEnd);

        try {
            Thread.sleep(1500);
        } catch (Exception e) {
        }

        // Create the game
        return newGame("random", gameMode);
    }
}
