package dungeonmania.StaticEntities;

import java.util.List;

import dungeonmania.Entity;

public class LogicEntityUtility {

    /**
     * Given the logic of an entity (e.g. and, or, xor, etc) and a list of boolean values
     * representing the inputs, returns the resulting logic (e.g. true/false).
     * @param logic - a string representing the desired logic
     * @param inputValues - list of boolean values
     * @return a boolean value resulting from the logic being applied to the inputValues
     */
    public static boolean applyLogic(String logic, List<Boolean> inputValues) {
        switch (logic) {
            case "and":
                if (inputValues.size() >= 2) {
                    return inputValues.stream().reduce(Boolean::logicalAnd).orElse(false);
                }
                return false;
            case "or":
                return inputValues.stream().reduce(Boolean::logicalOr).orElse(false);
            case "none":
                return inputValues.stream().reduce(Boolean::logicalOr).orElse(false);
            case "xor":
                return inputValues.stream().reduce(Boolean::logicalXor).orElse(false);
            case "not":
                if (inputValues.size() >= 1) {
                    return !(inputValues.stream().anyMatch(x -> x.equals(true)));
                }
                return true;
            default:
                return false;
        }
    }
    
    /**
     * Returns true if the entity is a logic carrier i.e. a wire or a floor switch
     * 
     *  @param entities - a list of entities
     *  @return - a boolean value
     */
    public static boolean isLogicCarrier(List<Entity> entities) {
        return entities != null && entities.size() > 0 
                && ((entities.get(0) instanceof Wire) || (entities.get(0) instanceof FloorSwitch));
    }
}
