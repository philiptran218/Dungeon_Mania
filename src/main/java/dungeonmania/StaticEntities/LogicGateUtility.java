package dungeonmania.StaticEntities;

import java.util.List;

public class LogicGateUtility {

    public static boolean applyLogic(String logic, List<Boolean> inputValues) {
        switch (logic) {
            case "and":
                return inputValues.stream().reduce(Boolean::logicalAnd).orElse(false);
            case "or":
                return inputValues.stream().reduce(Boolean::logicalOr).orElse(false);
            case "xor":
                return inputValues.stream().reduce(Boolean::logicalXor).orElse(false);
            case "not":
                for (Boolean value : inputValues) {
                    if (value == true) {
                        return false;
                    }
                }
                return true;
            default:
                return false;
        }
    }
}
