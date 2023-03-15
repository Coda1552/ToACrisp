package coda.toacrisp.client;

import net.minecraft.client.KeyMapping;

import java.util.ArrayList;
import java.util.List;

public class TACKeyBindings {
    public static final List<KeyMapping> LIST = new ArrayList<>();
    public static final KeyMapping DRAGON_DESCEND = register("dragonDescend", 90);

    private static KeyMapping register(String name, int key) {
        KeyMapping keyBinding = new KeyMapping("key." + name, key, "key.categories.toacrisp");
        LIST.add(keyBinding);
        return keyBinding;
    }
}