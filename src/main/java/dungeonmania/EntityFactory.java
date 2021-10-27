package dungeonmania;


import com.google.gson.JsonElement;

import dungeonmania.CollectableEntities.*;
import dungeonmania.MovingEntities.*;
import dungeonmania.util.Position;

public class EntityFactory {
    public static Entity getEntityObject(String id, String type, Position pos, JsonElement keyId) {
        switch (type) {
            /*
            case "wall": 
                return new Spider(pos);
            case "exit": 
                return new Wall(position, type);
            case "boulder": 
                return new Wall(position, type);     
            case "switch": 
                return new Wall(position, type);
            case "door": 
                return new Wall(position, type);
            case "portal": 
                return new Wall(position, type);
            case "zombie_toast_spawner": 
                return new Wall(position, type);
            */
            case "spider": 
                return new Spider(id, type, pos);
            case "zombie_toast": 
                return new ZombieToast(id, type, pos);
            case "mercenary": 
                return new Mercenary(id, type, pos);
            case "treasure": 
                return new Treasure(id, type, pos);
            case "health_potion": 
                return new HealthPotion(id, type, pos);
            case "key":
                return new Key(id, type, pos, keyId.getAsInt());
            case "invincibility_potion": 
                return new InvincibilityPotion(id, type, pos);
            case "invisibility_potion": 
                return new InvisibilityPotion(id, type, pos);
            case "wood": 
                return new Wood(id, type, pos);
            case "arrow": 
                return new Arrow(id, type, pos);
            case "bomb": 
                return new Bomb(id, type, pos);
            case "sword": 
                return new Sword(id, type, pos);
            case "armour": 
                return new Armour(id, type, pos);
            case "one_ring": 
                //return new TheOneRing(id, type, pos);
            case "bow": 
                return new Bow(id, type, pos);
            case "shield": 
                return new Shield(id, type, pos);
            case "player": 
                //return new Player(id, type, pos);
            default: 
                return null;
        }
    }

}
