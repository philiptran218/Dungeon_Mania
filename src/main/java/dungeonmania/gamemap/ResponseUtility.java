package dungeonmania.gamemap;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Entity;
import dungeonmania.CollectableEntities.CollectableEntity;
import dungeonmania.Goals.GoalUtility;
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
    public DungeonResponse returnDungeonResponse() {
        return new DungeonResponse(map.getMapId(), map.getDungeonName(), mapToListEntityResponse(), 
            getInventoryResponse(), getBuildablesResponse(), getGoalResponse());
    }

    /**
     * Takes an the json map object then looks at entity field and 
     * returns all entities on the map as a list of entity response.
     * @return List<EntityResponse> List of entity response.
     */
    public List<EntityResponse> mapToListEntityResponse() {
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
     * player has enough materials to build a bow or a shield.
     * @return List<String> List of buildable items.
     */
    public List<String> getBuildablesResponse() {
        List<String> buildable = new ArrayList<>();
        int numWood = map.getPlayer().getInventory().getNoItemType("wood");
        int numArrow = map.getPlayer().getInventory().getNoItemType("arrow");
        boolean hasKey = map.getPlayer().hasItem("key");
        boolean hasTreasure = map.getPlayer().hasItem("treasure");

        // Checks if sufficient materials
        if (numWood > 0 && numArrow > 2) {
            buildable.add("bow");
        }
        if (numWood > 1 && (hasKey || hasTreasure)) {
            buildable.add("shield");
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
    public String getGoalResponse() {
        return GoalUtility.goalPatternToString(GoalUtility.getGoalPattern(map.getJsonMap()), this.map.getMap());
    }

}
