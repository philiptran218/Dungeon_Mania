# Assumptions for Dungeon Mania

# Assumptions for Static Entities:
- If a boulder and a mob move to the same spot at the same time, the mobs do not move.
- If there is no open spot for a zombie toast to spawn, no zombie toast will spawn.
- Zombie toasts spawns from the top position. If that space is already occupied, it will check the right position and keeps going in a clockwise direction until a free spot is found.
- There can only be one static entity in one block.
- Zombies can spawn on an exit, portal or unlocked door.
- If the player has both a sword and a bow, the player will use a sword to destroy the zombie toast spawner.
- Spiders will not teleport through portals.
- Allies of player will be slowed down when crossing the swamp tile.

# Assumptions for Collectable Entities:
- Player can collect an unlimited number of entities (with the exception of key).
- TheOneRing is used immediately after the player dies (the player is not given a choice to use it or not).
- If both invincibility and invisibility potions are active at the same time, priority will be given to the invisibility potion.
- If a sword or bow is used to destroy a zombie toast spawner, this will also reduce the weapon's durability.
- Bomb explodes immediately if placed next to a switch with a boulder on top.
- Player can only have one potion active at a time for each type (if the player already has an active invincibility potion, they cannot use another one. However, they can use an invisibility potion since it is a different type).
- Bomb will only detonate if a boulder is pushed onto a switch that is cardinally adjacent to the bomb. The bomb will not detonate if it is placed cardinally adjacent to a boulder that is already on top of a switch.
- Player will retain the sun stone after using it except for building new weapons.
- If bribing a mercenary/assassin, a sceptre will be used first. If there is no sceptre, the sun stone will be used first since it doesn't get disposed of. If the player doesn't have sun stone, then treasure will be used.

# Assumptions for Moving Entities:
- Zombies/Spiders will move normally when the Player is inivisble
- Mercenary will stand still when Player is invisible
- When player is invincible, Spider will stay on its circular path around its spawning position. However it will move to the position that is furthest away from the player
- Zombies, Mercenaries and Spiders will not be able to push boulders
- Spiders will not spawn on a tile with a player, boulder, or is below a tile with a boulder.
- Player will only teleport through a portal if they can exit the opposite portal in the same direction that they entered from
- The spawn position for mercenary changes when a game is saved and loaded; and the new spwan position will be the start position for player in the newly loaded game.
- The hydra's health is allowed to go beyond it's original maximum health.
- Bribed allies can move on the same tile together.

# Assumptions for Buildable Entities:

- Shield/bow/midnight armour is used automatically in combat. Player is not given an option to use it or not.
- When building a shield, the player's treasure will automatically be used as material (since it has less importance than a key). If the player does not have enough treasure, then a key will be used.
- When building a sceptre, wood will be used first unless the player does not have enough wood, then arrows will be used. Same condition applies for key/treasure, where treasure will be used first and then key if the player does not have enough treasure.
- Sun stone is substitutable for treasure when building weapons. However, it will be used last if player already has treasure or a key, since the sun stone is the most valuable item.
- A Sceptre can be used an unlimited amount of times (can control unlimited enemies and has no durability).
- Sceptre will also be confined to the usual bribe radius. It cannot be used outside of this range.
- Even though midnight armour provides extra damage, its durability will only be decreased if the player takes damage rather than attacking enemies (treated as an armour).

# Assumptions for Battles:

- Player always attacks first, then the enemy attacks.
- If difficulty is set to Peaceful, the player can still attack enemies but enemies cannot attack the player.
- If the player is invincible and attacking an enemy equipped with armour, this will reduce the armour's durability. Similarly, if the player is invincible and attacks an enemy, this will reduce the weapons' durability.
- Allied mercenaries/assassins will only fight an enemy if the player is already fighting that enemy.
- Enemies will not fight against the player's allied mercenaries/assassins; they will only fight the player.
- If the player loots armour from an enemy, the armour will still retain its current durability.

# Assumptions for Goals:
- Every map must have at least one goal
- All goals must be valid goals
- Goals must be structured correctly within the dungeon .json file

# Assumptions for Logic Switches
- Floor switches which are connected together with wires CANNOT have NOT logic, this will cause an infinite loop.

- Floor switches with logic behave like wires if the logic is satisfied i.e. If a switch is activated, all of the other interactable entities adjacent to the switch are activated. The input of whether a boulder is on top the switch or not is also included to compute the switch's logic state.

- Of the Logic Switch entities, bombs will detonate first and if the detonation destroys wires that connect other logic switches, input/power will not provided to those logic switches

- The order in which the bombs detonate will be random.