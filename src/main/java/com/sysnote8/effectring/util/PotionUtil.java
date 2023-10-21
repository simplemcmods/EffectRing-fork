package com.sysnote8.effectring.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class PotionUtil {
    public static Potion getPotion(String potion_id) {
        return Potion.REGISTRY.getObject(new ResourceLocation(potion_id));
    }
}
