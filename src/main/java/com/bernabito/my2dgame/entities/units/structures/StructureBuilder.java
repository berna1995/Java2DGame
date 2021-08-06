package com.bernabito.my2dgame.entities.units.structures;

/**
 * @author Matteo Bernabito
 */

public final class StructureBuilder {

    private StructureBuilder() {

    }

    public static Structure buildStructureById(float x, float y, int id) {
        switch (id) {
            case 1:
                return new Rock(x, y);
            case 2:
                return new WoodPlanks(x, y);
            case 3:
                return new Cauldron(x, y);
            case 4:
                return new Tree(x, y, Tree.TreeType.OAK);
            case 5:
                return new Tree(x, y, Tree.TreeType.PINE);
            default:
                return null;
        }
    }

}
