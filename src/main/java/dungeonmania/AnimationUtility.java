package dungeonmania;

import java.util.Arrays;
import java.util.List;

import dungeonmania.MovingEntities.MovingEntity;
import dungeonmania.MovingEntities.Player;
import dungeonmania.response.models.AnimationQueue;
import dungeonmania.util.Direction;

public class AnimationUtility {
    public static void initialiseHealthBarForAllEntities(List<AnimationQueue> animations, Player player, List<MovingEntity> movingEntities, boolean isRewind) {
        if (isRewind == false) {
            animations.add(new AnimationQueue("PostTick", player.getId(), Arrays.asList("healthbar set 1", "healthbar tint 0x00ff00"), false, -1));
        }
        else {
            setPlayerHealthBar(animations, player);
        }
        for (MovingEntity entity: movingEntities) {
            animations.add(new AnimationQueue("PostTick", entity.getId(), Arrays.asList("healthbar set 1", "healthbar tint 0x00ff00"), false, -1));
        }
    }
    public static void setPlayerHealthBar(List<AnimationQueue> animations, Player player) {
        double health = player.getHealth() / player.getMaxHealth();
        if (health < 0.5) {
            animations.add(new AnimationQueue("PostTick", player.getId(), Arrays.asList("healthbar set " + health, "healthbar tint 0xff0000"), false, -1));
        }
        else {
            animations.add(new AnimationQueue("PostTick", player.getId(), Arrays.asList("healthbar set " + health, "healthbar tint 0x00ff00"), false, -1));
        }
    }
    public static void shakeHealthBar(List<AnimationQueue> animations, Player player) {
        animations.add(new AnimationQueue("PostTick", player.getId(), Arrays.asList("healthbar shake, over 0.5s, ease Sin"), false, 0.5));
    }
    public static void translatePlayer(List<AnimationQueue> animations, boolean isMovingIntoStatic, Player player, Direction direction) {
        if (isMovingIntoStatic == false) {
            if (direction.getOffset().getX() == 1) {
                animations.add(new AnimationQueue("PostTick", player.getId(), Arrays.asList("translate-x -1", "translate-x 1, over 0.3s"), false, -1));
            }
            else if (direction.getOffset().getX() == -1) {
                animations.add(new AnimationQueue("PostTick", player.getId(), Arrays.asList("translate-x 1", "translate-x -1, over 0.3s"), false, -1));
            }
            else if (direction.getOffset().getY() == 1) {
                animations.add(new AnimationQueue("PostTick", player.getId(), Arrays.asList("translate-y -1", "translate-y 1, over 0.3s"), false, -1));
            }
            else if (direction.getOffset().getY() == -1) {
                animations.add(new AnimationQueue("PostTick", player.getId(), Arrays.asList("translate-y 1", "translate-y -1, over 0.3s"), false, -1));
            }
        }
    }
    public static void translateBoulder(List<AnimationQueue> animations, Direction direction, String boulderId) {
        if (direction.getOffset().getX() == 1) {
            animations.add(new AnimationQueue("PostTick", boulderId, Arrays.asList("translate-x -1", "translate-x 1, over 0.3s", "rotate 180, over 0.5s"), false, -1));
        }
        else if (direction.getOffset().getX() == -1) {
            animations.add(new AnimationQueue("PostTick", boulderId, Arrays.asList("translate-x 1", "translate-x -1, over 0.3s", "rotate 180, over 0.5s"), false, -1));
        }
        else if (direction.getOffset().getY() == 1) {
            animations.add(new AnimationQueue("PostTick", boulderId, Arrays.asList("translate-y -1", "translate-y 1, over 0.3s", "rotate 180, over 0.5s"), false, -1));
        }
        else if (direction.getOffset().getY() == -1) {
            animations.add(new AnimationQueue("PostTick", boulderId, Arrays.asList("translate-y 1", "translate-y -1, over 0.3s", "rotate 180, over 0.5s"), false, -1));
        }
    }
}
