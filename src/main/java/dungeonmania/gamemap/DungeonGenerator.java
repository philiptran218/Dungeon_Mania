package dungeonmania.gamemap;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.util.Position;

public class DungeonGenerator {

    public static void generate (int xStart, int yStart, int xEnd, int yEnd) throws IllegalArgumentException {
        Position start = new Position(xStart, yStart);
        Position end = new Position(xEnd, yEnd);
        JSONObject json = toJson(RandomizedPrims(start, end), start, end);

        try {  
            // Writes the json file into the folder
            FileWriter file = new FileWriter("src/main/resources/dungeons/random.json");
            file.write(json.toString(4));
            file.flush();
            file.close();
        } catch (IOException e) {  
            e.printStackTrace();  
        }
    }

    public static Map<Position, Boolean> RandomizedPrims(Position start, Position end) {
        Map<Position, Boolean> maze = new HashMap<Position, Boolean>();
        // Create a 50 x 50 map with false entries 
        // Walls == false
        // Empty == true
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                maze.put(new Position(i, j), false);               
            }
        }

        maze.put(start, true);

        List<Position> options = new ArrayList<Position>();

        for (Position pos: getNeigbours(start, 2)) {
            if (maze.get(pos) == false) {
                // Is a wall i.e. false
                options.add(pos);
            }
        }

        while (options.size() > 0) {
            int num1 = ThreadLocalRandom.current().nextInt(0,options.size());
            // num  = 0,...size - 1;
            Position next = options.remove(num1);

            List<Position> neighbours = new ArrayList<Position>();
            for (Position pos: getNeigbours(next, 2)) {
                if (maze.get(pos) == true) {
                    // Is empty i.e. true
                    neighbours.add(pos);
                }
            }

            if (neighbours.size() > 0) {
                int num2 = ThreadLocalRandom.current().nextInt(0,neighbours.size());
                // num  = 0,...size - 1;
                Position neighbour = neighbours.remove(num2);

                maze.put(next, true);
                // midPos is the position inbetween next and neigbhour
                Position midPos = new Position((next.getX() + neighbour.getX())/2, (next.getY() + neighbour.getY())/2);
                
                maze.put(midPos, true);
                maze.put(neighbour, true);
            }

            for (Position pos: getNeigbours(next, 2)) {
                if ( maze.get(pos) == false) {
                    options.add(pos);
                }
            }
        }
        List<Position> neighbours = getNeigbours(end, 1);

        Boolean isConnected = false;
        for (Position pos: neighbours) {
            if (maze.get(pos) == true) { 
                // true i.e. empty
                isConnected = true;
            }
        }

        if (isConnected == false) {
            int num = ThreadLocalRandom.current().nextInt(0,neighbours.size());
            // num  = 0,...size - 1;
            Position neighbour = neighbours.remove(num);
            maze.put(neighbour, true);
        }

        return maze;
    }


    public static List<Position> getNeigbours(Position pos, int distance) {
        List<Position> neighbours = new ArrayList<Position>();
        neighbours.add(new Position(pos.getX(), pos.getY() + distance));
        neighbours.add(new Position(pos.getX(), pos.getY() - distance));
        neighbours.add(new Position(pos.getX() + distance, pos.getY()));
        neighbours.add(new Position(pos.getX() - distance, pos.getY()));

        List<Position> tempList = new ArrayList<Position>();
        for (Position tempPos: neighbours) {
            tempList.add(tempPos);
        }

        for (Position neighbour: neighbours) {
            if (!(0 < neighbour.getX() && neighbour.getX() < 49)) {
                // neighbour is  not within boundary of maze
                tempList.remove(neighbour);
            } else if (!(0 < neighbour.getY() && neighbour.getY() < 49)) {
                // neighbour is  not within boundary of maze
                tempList.remove(neighbour);
            }
        }
        return tempList;
    }

    public static JSONObject toJson(Map<Position, Boolean> maze, Position start, Position end) {
        // Main object for file
        JSONObject json = new JSONObject();
        // Add all fields:

        json.put("width", 50);
        json.put("height", 50);

        JSONObject goals = new JSONObject();
        goals.put("goal", "exit");
        json.put("goal-condition", goals);

        JSONArray jsonArray = new JSONArray();

        for (Position pos: maze.keySet()) {
            JSONObject entity = new JSONObject();
            entity.put("x", pos.getX());
            entity.put("y", pos.getY());

            if (pos.equals(start)) {
                entity.put("type", "player");
            } else if (pos.equals(end)) {
                entity.put("type", "exit");
            } else if (maze.get(pos) == false) {
                // Is a wall
                entity.put("type", "wall");
            } else {
                continue;
            }
            jsonArray.put(entity);
        }

        json.put("entities", jsonArray);
        return json;
    }
}
