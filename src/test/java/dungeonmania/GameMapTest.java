package dungeonmania;

import java.io.File;

import com.google.gson.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


import org.junit.jupiter.api.Test;



public class GameMapTest {
    public void deleteSavedGames(String fileName) {
        File file = new File("src/main/resources/saved_games/" + fileName + ".json");
        file.delete();
    }

    // Helper function to get the json test files:
    public JsonObject getTestJsonPath(String fileName) {
        DungeonManiaController n = new DungeonManiaController();
        return n.getJsonFile(fileName);
    }

    @Test 
    void testLoadMapFunction() {
        assertDoesNotThrow(() -> {
            DungeonManiaController d = new DungeonManiaController();
            d.newGame("key_door_saving", "peaceful");
            d.saveGame("saved");
        });
        deleteSavedGames("saved");
    }

}   
