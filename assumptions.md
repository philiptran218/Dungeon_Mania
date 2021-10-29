# Assumptions for Dungeon Mania

# Assumptions for Static Entities:
- If a boulder and a mob move to the same spot at the same time, the mobs do not move
- If there is no open spot for a zombie toast to spawn, no zombie toast will spawn
- Zombie toasts spawns from the top position. If that space is already occupied, it will check the right position and keeps going in a clockwise direction until a free spot is found.
- There can only be one static entity in one block
- Zombies can spawn on an exit, portal or unlocked door
- If the player has both a sword and a bow, the player will use a sword to destroy the zombie toast spawner

# Assumptions for Collectable Entities:

- Player can collect an unlimited number of entities (with the exception of key).
- (Maybe) Place a limit on the number of swords/armour the player can hold (Avoids unrealistic combat).
- TheOneRing is used immediately after the player dies (the player is not given a choice to use it or not).
- If both invincibility and invisibility potions are active at the same time, priority will be given to the invisibility potion.
- If a sword or bow is used to destroy a zombie toast spawner, this will also reduce the weapon's durability.

# Assumptions for Buildable Entities:

- (Maybe) Player can only hold 1 bow and 1 shield at a time. Creating another bow/shield discards the currently existing bow/shield. (Prevents unrealistic combat)
- Shield/bow is used automatically in combat. Player is not given an option to use it or not.

# Assumptions for Battles:

- Player always attacks first, then the enemy attacks.
- If difficulty is set to Peaceful, the player can still attack enemies but enemies cannot attack the player.