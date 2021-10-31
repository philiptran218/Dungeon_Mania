package dungeonmania;

import java.io.File;
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
import dungeonmania.util.Direction;
import dungeonmania.util.Position;


public class GameMapTest {
    public void deleteSavedGames(String fileName) {
        File file = new File("src/main/resources/saved_games/" + fileName + ".json");
        System.out.println("Deleting " + fileName + " " + file.delete());
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
            d.newGame("key_door_saving", "Peaceful");
            d.saveGame("saved");
        });
        deleteSavedGames("saved");
    }

}   
