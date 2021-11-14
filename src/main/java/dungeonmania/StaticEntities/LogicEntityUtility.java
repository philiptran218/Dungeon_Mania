package dungeonmania.StaticEntities;

import java.util.List;

import dungeonmania.Entity;

public class LogicEntityUtility {

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

    public static boolean isLogicCarrier(List<Entity> entities) {
        return entities != null && entities.size() > 0 
                && ((entities.get(0) instanceof Wire) || (entities.get(0) instanceof FloorSwitch));
    }
}
