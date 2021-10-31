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

    public static GoalInterface getGoalPattern(JsonObject jsonMap) {
        return goalJsonToPattern(getGoalsFromJson(jsonMap));
    }

    /**
     * Convert JsonObject containing goals into a composite pattern
     */
    public static GoalInterface goalJsonToPattern(JsonObject jsonGoal) {
        if (jsonGoal.get("goal").getAsString().equals("AND")) {
            GoalInterface goal = new AndGoal();
            for (JsonElement entity : jsonGoal.getAsJsonArray("subgoals")) {
                goal.add(goalJsonToPattern(entity.getAsJsonObject()));
            }
            return goal;
        } else if (jsonGoal.get("goal").getAsString().equals("OR")) {
            GoalInterface goal = new OrGoal();
            for (JsonElement entity : jsonGoal.getAsJsonArray("subgoals")) {
                goal.add(goalJsonToPattern(entity.getAsJsonObject()));
            }
            return goal;
        } else {
            return GoalFactory.getGoal(jsonGoal.get("goal").getAsString());
        }
    }

    public static JSONObject goalPatternToJson(GoalInterface rootGoal) {
        JSONObject goal = new JSONObject();
        if (isLeafGoal(rootGoal)) {
            return goal.put("goal", rootGoal.getGoalName());
        } else {
            goal.put("goal", rootGoal.getGoalName());
            JSONArray goalChildren = new JSONArray();
            for (GoalInterface goalChild : rootGoal.getChildren()) {
                goalChildren.put(goalPatternToJson(goalChild));
            }
            return goal.put("subgoals", goalChildren);
        }
    }
    
    private static boolean isLeafGoal(GoalInterface rootGoal) {
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

    public static String goalPatternToString(GoalInterface goal, Map<Position, List<Entity>> map) {
        List<String> currentGoals = new ArrayList<String>();
        if (goal.getGoalName().equals("AND") && !goal.isGoalComplete(map)) {
            for (GoalInterface childGoal : goal.getChildren()) {
                if (!childGoal.isGoalComplete(map)) {
                    currentGoals.add(goalPatternToString(childGoal, map));
                }
            }
            return String.join(" AND ", currentGoals);
        } else if (goal.getGoalName().equals("OR") && !goal.isGoalComplete(map)) {
            for (GoalInterface childGoal : goal.getChildren()) {
                if (!childGoal.isGoalComplete(map)) {
                    currentGoals.add(goalPatternToString(childGoal, map));
                }
            }
            return String.join(" OR ", currentGoals);
        }
        if (!goal.getGoalName().equals("OR") && !goal.isGoalComplete(map)) {
            return ":" + goal.getGoalName();
        } else {
            return "";
        }
    }
    
    public static JsonObject getGoalsFromJson (JsonObject dungeon) {
        return dungeon.getAsJsonObject("goal-condition");
    }
}
