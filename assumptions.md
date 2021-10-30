# Assumptions for Dungeon Mania

# Assumptions for Collectable Entities:

- Player can collect an unlimited number of entities (with the exception of key).
- (Maybe) Place a limit on the number of swords/armour the player can hold (Avoids unrealistic combat).
- TheOneRing is used immediately after the player dies (the player is not given a choice to use it or not).
- If both invincibility and invisibility potions are active at the same time, priority will be given to the invisibility potion.
- If a sword or bow is used to destroy a zombie toast spawner, this will also reduce the weapon's durability.
- Player can only have one potion active at a time for each type (if the player already has an active invincibility potion, they cannot use another one. However, they can use an invisibility potion since it is a different type).

# Assumptions for Buildable Entities:

- Shield/bow is used automatically in combat. Player is not given an option to use it or not.
- When building a shield, the player's treasure will automatically be used as material (since it has less importance than a key). If the player does not have enough treasure, then a key will be used.

# Assumptions for Battles:

- Player always attacks first, then the enemy attacks.
- If difficulty is set to Peaceful, the player can still attack enemies but enemies cannot attack the player.
- If the player is invincible and attacking an enemy equipped with armour, this will reduce the armour's durability. Similarly, if the player is invincible and attacks an enemy, this will reduce the weapons' durability.
- Allied mercenaries will only fight an enemy if the player is already fighting that enemy.
- Enemies will not fight against the player's allied mercenaries; they will only fight the player.

# Assumptions for Goals:
- Every map must have at least one goal
- All goals must be valid goals
- Goals must be structured correctly within the dungeon .json file
