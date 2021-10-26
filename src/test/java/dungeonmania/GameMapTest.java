package dungeonmania;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


import org.junit.jupiter.api.Test;

import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;


public class GameMapTest {
    // Helper function to get the json test files:
    public String getTestJsonPath(int fileNum) {
        return "src\\test\\java\\dungeonmania\\json_test_files\\file" + fileNum + ".json";
    }

    // Check invalid map arguments
    @Test
    public void testInvalidMapToJason() {
        GameMap map = new GameMap("Peaceful");
        assertThrows(IllegalArgumentException.class, () -> map.getMapAsJson("src\\main\\resources\\dungeons"));
    }

    // Test valid map arguments
    @Test
    public void testValidMapToJson() {
        assertDoesNotThrow(() -> {
            GameMap map = new GameMap("Peaceful");
            map.getMapAsJson("src\\main\\resources\\dungeons\\advanced.json");
        });

        assertDoesNotThrow(() -> {
            GameMap map = new GameMap("Peaceful");
            map.getMapAsJson("src\\main\\resources\\dungeons\\boulders.json");
        });

        assertDoesNotThrow(() -> {
            GameMap map = new GameMap("Peaceful");
            map.getMapAsJson("src\\main\\resources\\dungeons\\maze.json");
        });
    }

    // Test mapToListEntityResponse, to check whether or not it processes 
    // json files properly.
    @Test
    public void testMapToListOfEntityResponse() {
        GameMap map = new GameMap("Peaceful");

        // Get the json entity response list:
        JsonObject main = map.getMapAsJson(getTestJsonPath(1));
        List<EntityResponse> entityList = map.mapToListEntityResponse(main);
        
        // Manually make the array:
        List<EntityResponse> tested = new ArrayList<>();
        tested.add(new EntityResponse("0", "wall", new Position(0, 0), false));
        tested.add(new EntityResponse("1", "wall", new Position(1, 1), false));
        tested.add(new EntityResponse("2", "wall", new Position(2, 2), false));

        // Loop through both to check if both responses contain the same information:
        for (int i = 0; i < 3; i++) {
            // Assign entity response
            EntityResponse a = entityList.get(i);
            EntityResponse b = tested.get(i);

            // Check interior of entity:
            //assertEquals(a.getId(), b.getId());
            assertEquals(a.getPosition(), b.getPosition());
            assertEquals(a.getType(), b.getType());
        }
    }

}
