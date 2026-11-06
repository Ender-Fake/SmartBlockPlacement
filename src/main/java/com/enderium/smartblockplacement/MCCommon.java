package com.enderium.smartblockplacement;

import net.minecraft.resources.Identifier;

public class MCCommon {

    /**
     * Adapts to the {@link Identifier} changes introduced in 1.21.
     */
    public static Identifier id(String namespace, String path) {
        //? if <1.21 {
        /*return new Identifier(namespace, path);
         *///?} else
        return Identifier.fromNamespaceAndPath(namespace, path);
    }

    public static Identifier idMC(String path) {
        //? if <1.21 {
        /*return Identifier.tryParse(path);
         *///?} else
        return Identifier.withDefaultNamespace(path);
    }

}
