package dungeonmania;

import com.google.gson.JsonElement;

import dungeonmania.Battles.Battle;
import dungeonmania.CollectableEntities.*;
import dungeonmania.MovingEntities.*;
import dungeonmania.StaticEntities.*;
import dungeonmania.util.Position;

public class EntityFactory {
    public static Entity getEntityObject(String id, String type, Position pos, JsonElement keyId, Battle battle) {
        
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
                return new Door(id, type, staticPos, keyId.getAsInt());
            case "portal": 
                return new Portal(id, type, otherPos);
            case "zombie_toast_spawner": 
                return new ZombieToastSpawner(id, type, staticPos);
            case "spider": 
                return new Spider(id, type, movingPos);
            case "zombie_toast": 
                return new ZombieToast(id, type, movingPos);
            case "mercenary": 
                return new Mercenary(id, type, movingPos);
            case "treasure": 
                return new Treasure(id, type, collectPos);
            case "health_potion": 
                return new HealthPotion(id, type, collectPos);
            case "key":
                return new Key(id, type, collectPos, keyId.getAsInt());
            case "invincibility_potion": 
                return new InvincibilityPotion(id, type, collectPos);
            case "invisibility_potion": 
                return new InvisibilityPotion(id, type, collectPos);
            case "wood": 
                return new Wood(id, type, collectPos);
            case "arrow": 
                return new Arrow(id, type, collectPos);
            case "bomb": 
                return new Bomb(id, type, collectPos);
            case "sword": 
                return new Sword(id, type, collectPos);
            case "armour": 
                return new Armour(id, type, collectPos);
            case "one_ring": 
                return new TheOneRing(id, type, collectPos);
            case "bow": 
                return new Bow(id, type, collectPos);
            case "shield": 
                return new Shield(id, type, collectPos);
            case "player": 
                return new Player(id, type, movingPos, battle);
            default: 
                return null;
        }
    }

}
