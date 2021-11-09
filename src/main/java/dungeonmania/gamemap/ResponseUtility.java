package dungeonmania.gamemap;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Entity;
import dungeonmania.Goals.GoalHelper;
import dungeonmania.Goals.GoalInterface;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.MovingEntities.*;

public class ResponseUtility {
    // Map: ***********************
    private GameMap map;

    // Map Goals: *****************
    private GoalInterface rootGoal;
    /**
     * Constructor for response.
     * @param map
     */
    public ResponseUtility(GameMap gameMap) {
        this.map = gameMap;
        this.rootGoal = GoalHelper.getGoalPattern(gameMap.getJsonMap());
    }
    
    /**
     * Returns a dungeon response based on the current state of the game.
     * @return DungeonResponse on the current state of map.
     */
    public DungeonResponse returnDungeonResponse() {
        return new DungeonResponse(map.getMapId(), map.getDungeonName(), mapToListEntityResponse(), 
            map.getPlayer().getInventoryResponse(), getBuildablesResponse(), getGoalResponse());
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
     * Get the goals that needs to be completed for the map.
     * @return String of goals that needs to be completed.
     */
    public String getGoalResponse() {
        return GoalHelper.goalPatternToString(this.getRootGoal(), this.map.getMap());
    }

    // Getters and setters:

    public GoalInterface getRootGoal() {
        return rootGoal;
    }
}
