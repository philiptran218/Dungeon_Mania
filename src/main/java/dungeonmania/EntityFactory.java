package dungeonmania;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import dungeonmania.CollectableEntities.*;
import dungeonmania.MovingEntities.*;
import dungeonmania.StaticEntities.*;
import dungeonmania.gamemap.GameMap;
import dungeonmania.util.Position;

public class EntityFactory {
    private static final double PLAYER_DAMAGE = 2;

    public static Entity getEntityObject(String id, Position pos, JsonObject jsonObj, GameMap gameMap) {
        boolean isOldPlayer = false;
        // Fields
        String type = jsonObj.get("type").getAsString();
        // Check if it is old player
        if (type.equals("older_player")) { 
            type = "player";
            isOldPlayer = true; 
        }

        // Positions:
        Position otherPos = new Position(pos.getX(), pos.getY(), 4);
        Position movingPos = new Position(pos.getX(), pos.getY(), 3);
        Position collectPos = new Position(pos.getX(), pos.getY(), 2);
        Position staticPos = new Position(pos.getX(), pos.getY(), 1);
        Position absolPos = new Position(pos.getX(), pos.getY(), 0);

        switch (type) {
            case "wall": 
                return new Wall(id, type, staticPos);
            case "exit": 
                return new Exit(id, type, absolPos);
            case "boulder": 
                return new Boulder(id, type, staticPos);     
            case "switch": 
                return new FloorSwitch(id, type, absolPos);
            case "door": 
                return new Door(id, type, staticPos, jsonObj.get("key").getAsInt());
            case "door_unlocked":
                return new Door(id, "door_unlocked", absolPos, jsonObj.get("key").getAsInt());
            case "portal": 
                return new Portal(id, type, otherPos, jsonObj.get("colour").getAsString());
            case "zombie_toast_spawner":
                return new ZombieToastSpawner(id, type, staticPos);
            case "spider":
                Spider tmp = new Spider(id, type, movingPos);
                if (jsonObj.get("centre") != null) {
                    JsonObject centre = jsonObj.get("centre").getAsJsonObject();
                    tmp.setStartPosition(new Position(centre.get("x").getAsInt(), centre.get("y").getAsInt(), 3));
                }
                return tmp;
            case "zombie_toast": 
                return new ZombieToast(id, type, movingPos);
            case "mercenary": 
                return new Mercenary(id, type, movingPos);
            case "assassin":
                return new Assassin(id, type, movingPos);
            case "hydra":
                return new Hydra(id, type, movingPos);
            case "treasure": 
                return new Treasure(id, type, collectPos);
            case "health_potion": 
                return new HealthPotion(id, type, collectPos);
            case "key":
                return new Key(id, type, collectPos, jsonObj.get("key").getAsInt());
            case "invincibility_potion": 
                return new InvincibilityPotion(id, type, collectPos);
            case "invisibility_potion": 
                return new InvisibilityPotion(id, type, collectPos);
            case "wood": 
                return new Wood(id, type, collectPos);
            case "arrow": 
                return new Arrow(id, type, collectPos);
            case "bomb":
                if (jsonObj.get("logic") != null) {
                    return new Bomb(id, type, collectPos, jsonObj.get("logic").getAsString());
                } else {
                    return new Bomb(id, type, collectPos);
                }
            case "sword":
                Sword sword = new Sword(id, type, collectPos);
                if (jsonObj.get("durability") != null) {
                    sword.setDurability(jsonObj.get("durability").getAsInt());
                }
                return sword;
            case "armour": 
                Armour armour = new Armour(id, type, collectPos);
                if (jsonObj.get("durability") != null) {
                    armour.setDurability(jsonObj.get("durability").getAsInt());
                }
                return armour;
            case "one_ring": 
                return new TheOneRing(id, type, collectPos);
            case "bow": 
                Bow bow = new Bow(id, type, collectPos);
                if (jsonObj.get("durability") != null) {
                    bow.setDurability(jsonObj.get("durability").getAsInt());
                }
                return bow;
            case "shield": 
                Shield shield = new Shield(id, type, collectPos);
                if (jsonObj.get("durability") != null) {
                    shield.setDurability(jsonObj.get("durability").getAsInt());
                }
                return shield;
            case "anduril":
                Anduril anduril = new Anduril(id, type, collectPos);
                if (jsonObj.get("durability") != null) {
                    anduril.setDurability(jsonObj.get("durability").getAsInt());
                }
                return anduril;
            case "sun_stone":
                return new SunStone(id, type, collectPos);
            case "sceptre":
                return new Sceptre(id, type, collectPos);
            case "midnight_armour":
                MidnightArmour mArmour = new MidnightArmour(id, type, collectPos);
                if (jsonObj.get("durability") != null) {
                    mArmour.setDurability(jsonObj.get("durability").getAsInt());
                }
                return mArmour;
            case "time_turner":
                return new TimeTuner(id, type, collectPos);
            case "player": 
                Player player = null;
                if (jsonObj.get("health") != null) {
                    double health = jsonObj.get("health").getAsDouble();
                    player = new Player(id, type, movingPos, health, gameMap.getGameState().getPlayerMaxHealth(),PLAYER_DAMAGE);
                } else {
                    player = new Player(id, type, movingPos, gameMap.getGameState().getPlayerMaxHealth(), 
                                        gameMap.getGameState().getPlayerMaxHealth(), PLAYER_DAMAGE);
                }
                if (jsonObj.getAsJsonArray("active_potions") != null) {
                    for (JsonElement potionElem : jsonObj.getAsJsonArray("active_potions")) {
                        JsonObject potionJSON = potionElem.getAsJsonObject();
                        player.getPotions().put(potionJSON.get("type").getAsString(), potionJSON.get("duration").getAsInt());
                    }
                }
                Integer i = 0;
                // Set Player inventory:
                if (jsonObj.getAsJsonArray("inventory") != null) {
                    for (JsonElement entity : jsonObj.getAsJsonArray("inventory")) {
                        JsonObject obj = entity.getAsJsonObject();
                        Entity collectable = EntityFactory.getEntityObject("inventItem" + i, new Position(0, 0), obj, gameMap);
                        player.getInventory().put(collectable, player);
                        i++;
                    }
                }
                if (isOldPlayer) { 
                    player.setType("older_player");
                } else {
                    gameMap.setPlayer(player); 
                }
                return player;
            case "swamp_tile": 
                SwampTile swamp = new SwampTile(id, type, absolPos, jsonObj.get("movement_factor").getAsInt());
                if (jsonObj.get("entites_on_tile") != null) {
                    // Create and add all existing objects on the tile
                    Integer j = 1;
                    for (JsonElement entity : jsonObj.getAsJsonArray("entites_on_tile")) {
                        JsonObject jObject = entity.getAsJsonObject();
                        Position ePos = new Position(jObject.get("x").getAsInt(), jObject.get("y").getAsInt());
                        String newId = swamp.getId() + "onswamptile" + j;
                        // Create the object:
                        Entity e = EntityFactory.getEntityObject(newId, ePos, jObject, gameMap);
                        // Add to map
                        gameMap.getMap().get(e.getPos()).add(e);
                        // Add to swamp map
                        swamp.addToMap(e, jObject.get("ticks_remaining").getAsInt());
                        j++;
                    }
                }
                return swamp;
            case "light_bulb_on":
                if (jsonObj.get("logic") != null) {
                    return new LightBulb(id, type, absolPos, jsonObj.get("logic").getAsString());
                } else {
                    return new LightBulb(id, type, absolPos);
                }
            case "light_bulb_off":
                if (jsonObj.get("logic") != null) {
                    return new LightBulb(id, type, absolPos, jsonObj.get("logic").getAsString());
                } else {
                    return new LightBulb(id, type, absolPos);
                }
            case "switch_door":
                if (jsonObj.get("logic") != null) {
                    return new SwitchDoor(id, type, staticPos, jsonObj.get("logic").getAsString());
                } else {
                    return new SwitchDoor(id, type, staticPos);
                }
            case "wire":
                return new Wire(id, type, pos);
            default: 
                return null;
        }
    }

}
