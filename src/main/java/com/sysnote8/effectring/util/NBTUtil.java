package com.sysnote8.effectring.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class NBTUtil {

    public static boolean hasNBT(ItemStack stack) {
        return stack.hasTagCompound();
    }

    public static NBTTagCompound getNBT(ItemStack stack) {
            if (!hasNBT(stack)) stack.setTagCompound(new NBTTagCompound());
        return stack.getTagCompound();
    }

    public static boolean hasTag(ItemStack stack, String tag) {
        return !stack.isEmpty() && hasNBT(stack) && getNBT(stack).hasKey(tag);
    }

    public static NBTTagList getList(ItemStack stack, String tag, int objtype, boolean isNullOnFail) {
        return hasTag(stack, tag) ? getNBT(stack).getTagList(tag, objtype) : isNullOnFail ? null : new NBTTagList();
    }
}
