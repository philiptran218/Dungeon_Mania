package dungeonmania;

import dungeonmania.MovingEntities.*;
import dungeonmania.StaticEntities.*;
import dungeonmania.CollectableEntities.*;
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
    // Game Map
    private GameMap gameMap;

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
        return Arrays.asList("Standard", "Peaceful", "Hard");
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
    
    /**
     * Creates a new game in the ManiaController, and throw IllegalArgumentException
     * if the dungeon name is not valid or game mode given is not valid.
     * @param dungeonName (String)
     * @param gameMode (String)
     * @return DungeonResponse
     * @throws IllegalArgumentException
     */
    public DungeonResponse newGame(String dungeonName, String gameMode) throws IllegalArgumentException {
        if (!getGameModes().contains(gameMode)) {
            throw new IllegalArgumentException("Game mode does not exist.");
        }
        // Set map:
        this.gameMap = new GameMap(gameMode, dungeonName, getJsonFile(dungeonName));
        // Return DungeonResponse
        return gameMap.returnDungeonResponse();
    }
    
    /**
     * Save the the current game that is running with the given name.
     * @param name (String)
     * @return DungeonResponse
     * @throws IllegalArgumentException
     */
    public DungeonResponse saveGame(String name) throws IllegalArgumentException {
        // Advanced 
        this.gameMap.saveMapAsJson(name);
        // Return DungeonResponse
        return gameMap.returnDungeonResponse();
    }

    /**
     * Load the game with the given name, throws IllegalArgumentException
     * if the name provided is not a saved game.
     * @param name (String)
     * @return DungeonResponse
     * @throws IllegalArgumentException
     */
    public DungeonResponse loadGame(String name) throws IllegalArgumentException {
        this.gameMap = new GameMap(name);
        // Return DungeonResponse
        return gameMap.returnDungeonResponse();
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
     */
    public DungeonResponse tick(String itemUsed, Direction movementDirection) throws IllegalArgumentException, InvalidActionException {
        // If itemUsed is NULL move the player:
        if (itemUsed == null && !gameMap.isOnSwampTile(null)) {
            gameMap.getPlayer().move(gameMap.getMap(), movementDirection);
        } else if (itemUsed != null) {
            // Get the entity on map:
            gameMap.getPlayer().useItem(gameMap.getMap(), itemUsed);
        }

        // Ticks the duration of any active potions
        gameMap.getPlayer().tickPotions();
        
        // Move all the moving entities by one tick:
        for (MovingEntity e : gameMap.getMovingEntityList()) {
            if (!(e.getPos().equals(e.getPlayerPos()) && !e.isType("mercenary")) && !gameMap.isOnSwampTile(e.getId())) {
                if (gameMap.getMap() != null) {
                    System.out.println("THE MAP IS NOT NULL");
                }
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
        gameMap.spawnMob();

        // Tick the swamp:
        gameMap.swampTick();

        // Check for swamp tile after all movements has occured,
        // and removes accordinly
        gameMap.swampTileCheck();

        // Return DungeonResponse
        return gameMap.returnDungeonResponse();
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

        return gameMap.returnDungeonResponse();
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
        if (!(buildable.equals("bow") || buildable.equals("shield"))) {
            throw new IllegalArgumentException();
        }
        Player player = gameMap.getPlayer();

        Inventory playerInv = player.getInventory();
        if (buildable.equals("bow")) {
            if (playerInv.getNoItemType("wood") < 1 || playerInv.getNoItemType("arrow") < 3) {
                throw new InvalidActionException("Not enough materials!");
            }
            playerInv.useItem("wood");
            playerInv.useItem("arrow");
            playerInv.useItem("arrow");
            playerInv.useItem("arrow");
            Bow newBow = new Bow("" + System.currentTimeMillis(), "bow", null);
            player.getInventory().put(newBow, player);
        }
        // Otherwise we are crafting a shield
        else {
            if (playerInv.getNoItemType("wood") < 2 || (playerInv.getNoItemType("treasure") < 1 && playerInv.getNoItemType("key") < 1)) {
                throw new InvalidActionException("Not enough materials!");
            }
            playerInv.useItem("wood");
            playerInv.useItem("wood");
            if (playerInv.getNoItemType("treasure") >= 1 ) {
                playerInv.useItem("treasure");
            } else {
                playerInv.useItem("key");
            }
            Shield newShield = new Shield("" + System.currentTimeMillis(), "shield", null);
            player.getInventory().put(newShield, player);
        }
        return gameMap.returnDungeonResponse();
    }

}
