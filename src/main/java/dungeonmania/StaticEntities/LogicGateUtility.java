package dungeonmania.StaticEntities;

import java.util.List;

public class LogicGateUtility {

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
                    return !(inputValues.stream().anyMatch(x -> true));
                }
                return false;
            default:
                return false;
        }
    }
}
