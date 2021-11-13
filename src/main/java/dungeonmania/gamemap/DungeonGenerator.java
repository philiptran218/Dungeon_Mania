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

    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;

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


    /**
     * Given a start and end position, create the maze map using prim's algorithm
     * @param start Spawning position of the player
     * @param end Position of the exit
     * @return A map of the maze, where wall == false, empty == true
     */
    public static Map<Position, Boolean> RandomizedPrims(Position start, Position end) {
        Map<Position, Boolean> maze = new HashMap<Position, Boolean>();
        // Create a 50 x 50 map with false entries 
        // Walls == false
        // Empty == true
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                maze.put(new Position(i, j), false);               
            }
        }

        maze.put(start, true);
        List<Position> options = new ArrayList<Position>();

        // Adding neigbours of 'start' that are distance 2 away and are walls
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

            // Adding neighbours of 'next' that are distance 2 away and are empty
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

            // Add all neighbours of 'next' that are distance 2 away and are walls
            for (Position pos: getNeigbours(next, 2)) {
                if ( maze.get(pos) == false) {
                    // Is a wall i.e. false
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

    /**
     * Obtain neigbouring points of a position that are distance away from the position
     * and within the boundary of the maze
     * @param pos Position whose neigbours are to be found
     * @param distance Distance away the position
     * @return A list of the neighbours of the point
     */
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
            // Remove -1 and -1 to prevent double walling, but doesnt follow pseudo code
            if (!(0 < neighbour.getX() && neighbour.getX() < WIDTH - 1)) {
                // neighbour is  not within boundary of maze
                tempList.remove(neighbour);
            } else if (!(0 < neighbour.getY() && neighbour.getY() < HEIGHT - 1)) {
                // neighbour is  not within boundary of maze
                tempList.remove(neighbour);
            }
        }
        return tempList;
    }

    /**
     * Given a maze map, generates the JSON file for the dungeon. Places a player
     * at the start Position, and a exit at the end Position
     * @param maze map of the dungeon
     * @param start Position of the player
     * @param end Position of the exit
     * @return JSON file for the dungeon
     */
    public static JSONObject toJson(Map<Position, Boolean> maze, Position start, Position end) {
        // Main object for file
        JSONObject json = new JSONObject();
        // Add all fields:

        json.put("width", WIDTH);
        json.put("height", HEIGHT);

        JSONObject goals = new JSONObject();
        goals.put("goal", "exit");
        json.put("goal-condition", goals);

        JSONArray jsonArray = new JSONArray();

        for (Position pos: maze.keySet()) {
            JSONObject entity = new JSONObject();
            entity.put("x", pos.getX());
            entity.put("y", pos.getY());
            // NOTE: to prevent double walling, doesnt follow pseudo code
            // if (pos.getX() == WIDTH - 1 || pos.getY() == HEIGHT - 1) {
            //     entity.put("type","wall");
            // } else 
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
