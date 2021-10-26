package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonObject;

import org.junit.jupiter.api.Test;

public class MovingEntityTest {
    // Helper:
    // Helper function to get the json test files:
    public JsonObject getTestJsonPath(int fileName) {
        DungeonManiaController controller = new DungeonManiaController();
        return controller.getJsonFile("src\\test\\java\\dungeonmania\\json_test_files\\" + fileName + ".json");
    }
    // Test basic player movements
    @Test
    public void basicPlayerMovements() {
        
    }
}
