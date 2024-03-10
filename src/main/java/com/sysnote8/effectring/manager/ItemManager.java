package com.sysnote8.effectring.manager;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

import com.sysnote8.effectring.item.ItemBase;
import com.sysnote8.effectring.item.RingItem;

public class ItemManager {

    public static final List<ItemBase> items = new ArrayList<>();
    static {
        // Items add to list
        items.add(new RingItem());
    }

    public static void register(IForgeRegistry<Item> registry) {
        // Register items
        for (ItemBase item : items) {
            registry.register(item);
        }
    }

    public static void registerModels() {
        // Register item models
        for (ItemBase item : items) {
            item.ModelRegister();
        }
    }
}
