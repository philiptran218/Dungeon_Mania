package dungeonmania.CollectableEntities;

import dungeonmania.util.Position;

public class SunStone extends Utility {

    /**
     * Constructor for Sun Stone
     * @param id
     * @param type
     * @param pos
     */
    public SunStone(String id, String type, Position pos) {
        super(id, type, pos);
    }

    /**
     * Is called if the sun stone is used a build a sceptre or midnight armour.
     */
    public void use() {
        getPlayer().getInventoryList().remove(this);
    }

    // ********************************************************************************************\\
    //                                    Getters and Setters                                      \\
    // ********************************************************************************************\\
}