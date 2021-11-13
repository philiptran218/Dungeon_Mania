package dungeonmania.gamemap;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Entity;
import dungeonmania.CollectableEntities.CollectableEntity;
import dungeonmania.Goals.GoalUtility;
import dungeonmania.response.models.AnimationQueue;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.MovingEntities.*;

public class ResponseUtility {
    // Map: ***********************
    private GameMap map;
    /**
     * Constructor for response.
     * @param map
     */
    public ResponseUtility(GameMap gameMap) {
        this.map = gameMap;
    }
    

     /**
     * Returns a dungeon response based on the current state of the game.
     * @return DungeonResponse on the current state of map.
     */
    public DungeonResponse returnDungeonResponseNewGame() {
        return new DungeonResponse(map.getMapId(), map.getDungeonName(), getEntityResponse(), 
            getInventoryResponse(), getBuildablesResponse(), getNewGameGoalResponse());
    }

    /**
     * Returns a dungeon response based on the current state of the game.
     * @return DungeonResponse on the current state of map.
     */
    public DungeonResponse returnDungeonResponse(List<AnimationQueue> animations) {
        return new DungeonResponse(map.getMapId(), map.getDungeonName(), getEntityResponse(), 
            getInventoryResponse(), getBuildablesResponse(), getGoalResponse(), animations);
    }

    /**
     * Takes an the json map object then looks at entity field and 
     * returns all entities on the map as a list of entity response.
     * @return List<EntityResponse> List of entity response.
     */
    public List<EntityResponse> getEntityResponse() {
        // EntityResponse list to help append entities on the map
        List<EntityResponse> entityList = new ArrayList<EntityResponse>();
        // Loops through entities on the map entities on the map
        for (Entity e : map.getAllEntity()) {
            // Checks if the the entity is a mecenary or toast_spawner to 
            boolean isInteractable = (e.isType("mercenary") || e.isType("zombie_toast_spawner"));
            if (e.getType().equals("mercenary") && ((Mercenary) e).isAlly()){
                isInteractable = false;
            }
            // Add the entity to the map
            entityList.add(new EntityResponse(e.getId(), e.getType(), e.getPos(), isInteractable));
        }
        
        return entityList;
    }

    /**
     * Looks through the player's inventory and checks if the
     * player has enough materials to build a bow, shield, sceptre or midnight armour.
     * @return List<String> List of buildable items.
     */
    public List<String> getBuildablesResponse() {
        List<String> buildable = new ArrayList<>();
        int numWood = map.getPlayer().getInventory().getNoItemType("wood");
        int numArrow = map.getPlayer().getInventory().getNoItemType("arrow");
        boolean hasKey = map.getPlayer().hasItem("key");
        boolean hasTreasure = map.getPlayer().hasItem("treasure");
        int numSunStone = map.getPlayer().getInventory().getNoItemType("sun_stone");
        boolean hasArmour = map.getPlayer().hasItem("armour");
        boolean hasZombie = map.getMovingEntityList().stream().anyMatch(e -> e.getType().equals("zombie_toast"));

        // Checks if sufficient materials
        if (numWood > 0 && numArrow > 2) {
            buildable.add("bow");
        }
        if (numWood > 1 && (hasKey || hasTreasure || numSunStone > 0)) {
            buildable.add("shield");
        }
        if ((numWood > 0 || numArrow > 1) && (hasKey || hasTreasure || numSunStone > 1) && numSunStone > 0) {
            buildable.add("sceptre");
        }
        if (hasArmour && numSunStone > 0 && !hasZombie) {
            buildable.add("midnight_armour");
        }
        return buildable;
    }

    /**
     * Converts the player's inventory into a list of item response.
     * @return List<ItemResponse> List of ItemResponse.
     */
    public List<ItemResponse> getInventoryResponse() {
        List<ItemResponse> itemResponse = new ArrayList<>();
        // Loop through the player and adds his items to the lists
        for (CollectableEntity c : map.getPlayer().getInventoryList()) {
            itemResponse.add(new ItemResponse(c.getId(), c.getType()));
        }
        return itemResponse;
    }

    /**
     * Get the goals that needs to be completed for the map.
     * @return String of goals that needs to be completed.
     */
    private String getGoalResponse() {
        return GoalUtility.goalPatternToString(GoalUtility.getGoalPattern(map.getJsonMap()), map.getMap());
    }

    /**
     * Get the goals that needs to be completed for the map when a new game is opened.
     * @return String of goals that needs to be completed.
     */
    private String getNewGameGoalResponse() {
        return GoalUtility.goalJsontoString(GoalUtility.getGoalsFromJson(map.getJsonMap()));
    }
}
