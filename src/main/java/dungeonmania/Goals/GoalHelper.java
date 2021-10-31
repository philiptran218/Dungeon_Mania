package dungeonmania.Goals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public class GoalHelper {

    /**
     * Calls for the conversion of a jsonObject to a composite pattern for goals
     * 
     * @param jsonMap - a jsonObject representing the dungeon.
     * @return GoalInterface - the root of the composite pattern containing the goals of the map.
     */
    public static GoalInterface getGoalPattern(JsonObject dungeon) {
        return goalJsonToPattern(getGoalsFromJson(dungeon));
    }

    /**
     * Gets and returns the goals of the dungeon from a jsonObject which stores
     * the dungeon. 
     * 
     * @param dungeon - a jsonObject containing the dungeon.
     * @return JsonObject - a jsonObject containing the goals of the dungeon.
     */
    public static JsonObject getGoalsFromJson (JsonObject dungeon) {
        return dungeon.getAsJsonObject("goal-condition");
    }

    /**
     * Recursively converts a jsonObject to a composite pattern for goals
     * 
     * @param jsonGoal - a jsonObject containing the goals of the dungeon.
     * @return GoalInterface - the root of the composite pattern containing the goals of the map.
     */
    public static GoalInterface goalJsonToPattern(JsonObject jsonGoal) {
        if (getGoalType(jsonGoal).equals("AND")) {
            GoalInterface goal = GoalFactory.getGoal(getGoalType(jsonGoal));
            for (JsonElement entity : jsonGoal.getAsJsonArray("subgoals")) {
                goal.add(goalJsonToPattern(entity.getAsJsonObject()));
            }
            return goal;
        } else if (getGoalType(jsonGoal).equals("OR")) {
            GoalInterface goal = GoalFactory.getGoal(getGoalType(jsonGoal));
            for (JsonElement entity : jsonGoal.getAsJsonArray("subgoals")) {
                goal.add(goalJsonToPattern(entity.getAsJsonObject()));
            }
            return goal;
        } else {
            return GoalFactory.getGoal(getGoalType(jsonGoal));
        }
    }

    /**
     * Gets the goal type from the jsonObject
     * 
     * @param jsonGoal - a single goal from the jsonObject containing all the goals.
     * @return String - a string representing the type of the goal.
     */
    private static String getGoalType(JsonObject jsonGoal) {
        return jsonGoal.get("goal").getAsString();
    }

    /**
     * Recursively converts a composite pattern to a jsonObject for goals.
     * 
     * @param rootGoal - the root of the composite pattern containing the goals of the map.
     * @return JSONObject- a jsonObject containing the goals of the dungeon.
     */
    public static JSONObject goalPatternToJson(GoalInterface rootGoal) {
        JSONObject goal = new JSONObject();
        if (isSingleGoal(rootGoal)) {
            return goal.put("goal", rootGoal.getGoalName());
        // If it's not a single goal, it is a composite goal
        } else {
            goal.put("goal", rootGoal.getGoalName());
            JSONArray goalChildren = new JSONArray();
            for (GoalInterface goalChild : rootGoal.getChildren()) {
                goalChildren.put(goalPatternToJson(goalChild));
            }
            return goal.put("subgoals", goalChildren);
        }
    }
    
    /**
     * Checks if a goal is a single goal and not a composite goal.
     * 
     * @param rootGoal - a GoalInterface object.
     * @return boolean - true if the goal is a single goal, false otherwise.
     */
    private static boolean isSingleGoal(GoalInterface rootGoal) {
        switch (rootGoal.getGoalName()) {
            case "enemies":
                return true;
            case "exit":
                return true;
            case "treasure":
                return true;
            case "boulders":
                return true;
            default:
                return false;
        }
    }

    /**
     * Converts a composite pattern into a string for goals.
     * 
     * @param goal - the root of the composite pattern containing the goals of the map.
     * @param map - the current state of the dungeon.
     * @return String - a string representing all the goals of the dungeon.
     */
    public static String goalPatternToString(GoalInterface goal, Map<Position, List<Entity>> map) {
        if (goal.getGoalName().equals("AND") && !goal.isGoalComplete(map)) {
            return "(" + String.join(" AND ", getIncompleteChildrenGoals(goal, map)) + ")";
        } else if (goal.getGoalName().equals("OR") && !goal.isGoalComplete(map)) {
            return "(" + String.join(" OR ", getIncompleteChildrenGoals(goal, map)) + ")";
        } else if (!goal.isGoalComplete(map)) {
            return ":" + goal.getGoalName();
        } else {
            return "";
        }
    }

    /**
     * Collects the incomplete children goals of a composite goal and returns a
     * list of those goals. 
     * 
     * @param goal - a composite GoalInterface.
     * @param map - the current state of the dungeon.
     * @return currentGoals - a list of strings where each string represents a child goal.
     */
    private static List<String> getIncompleteChildrenGoals(GoalInterface goal, Map<Position, List<Entity>> map) {
        List<String> currentGoals = new ArrayList<String>();
        for (GoalInterface childGoal : goal.getChildren()) {
            if (!childGoal.isGoalComplete(map)) {
                currentGoals.add(goalPatternToString(childGoal, map));
            }
        }
        return currentGoals;
    }
}
