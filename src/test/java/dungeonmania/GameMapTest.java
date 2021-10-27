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

import dungeonmania.gamemap.GameMap;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;


public class GameMapTest {
    // Helper function to get the json test files:
    public JsonObject getTestJsonPath(String fileName) {
        DungeonManiaController n = new DungeonManiaController();
        return n.getJsonFile("src\\test\\java\\dungeonmania\\json_test_files\\" + fileName + ".json");
    }

    // Test mapToListEntityResponse, to check whether or not it processes 
    // json files properly.
    @Test
    public void testMapToListOfEntityResponse() {
        GameMap map = new GameMap("Peaceful", null);

        // Get the json entity response list:
        JsonObject main = getTestJsonPath("file1");
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

    // Check if we are sucessfully ignoring the third layer:
    @Test
    public void testNoLayerFromReadingJsonMap () {
        // Get the json entity response list:
        JsonObject main = getTestJsonPath("file1");

        // Load game:
        GameMap map = new GameMap(main);

        for (EntityResponse i : map.mapToListEntityResponse(main)) {
           assertEquals(i.getPosition().getLayer(), 0);
        }
    }

    // Check if we are processing the third layer sucessfully:
    @Test
    public void testLayerFromReadingJsonMap() {
        // Get the json entity response list:
        JsonObject main = getTestJsonPath("test_layer");

        // Load game:
        GameMap map = new GameMap(main);

        for (EntityResponse i : map.mapToListEntityResponse(main)) {
            assertEquals(i.getPosition().getLayer(), 3);
        }
    }

}   
