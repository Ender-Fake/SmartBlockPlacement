package com.enderium.smartblockplacement.text;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public interface TextGetter {

    MutableComponent get();

    MutableComponent get(Object... args);


    static TextGetter of(final String key) {
        return new TextGetter() {
            @Override
            public MutableComponent get() {
                return Component.translatable(key);
            }

            @Override
            public MutableComponent get(Object... args) {
                return Component.translatable(key, args);
            }
        };
    }

    static TextGetter ofText(final String text) {
        return new TextGetter() {
            @Override
            public MutableComponent get() {
                return Component.literal(text);
            }

            @Override
            public MutableComponent get(Object... args) {
                return Component.literal(text);
            }
        };
    }


}
