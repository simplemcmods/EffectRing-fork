package com.sysnote8.effectring.item;

import java.util.HashMap;
import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.jetbrains.annotations.Nullable;

import com.sysnote8.effectring.util.NBTUtil;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;

public class RingItem extends ItemBase implements IBauble {

    private static final String TagPotList = "effects";

    public RingItem() {
        super("ring");
        setMaxStackSize(1);
    }

    public static HashMap<Potion, Integer> getPotion(ItemStack stack) {
        if (stack == null || !NBTUtil.hasTag(stack, TagPotList)) return null;
        NBTTagList effectList = NBTUtil.getList(stack, TagPotList, Constants.NBT.TAG_COMPOUND, false);
        if (effectList.isEmpty()) return null;
        HashMap<Potion, Integer> potions = new HashMap<Potion, Integer>();
        for (int i = 0; i < effectList.tagCount(); i++) {
            NBTTagCompound tagCompound = effectList.getCompoundTagAt(i);
            if (tagCompound.hasKey("id")) {
                ResourceLocation resourceLocation = new ResourceLocation(tagCompound.getString("id"));
                if (Potion.REGISTRY.getKeys().contains(resourceLocation)) {
                    int amplifier = tagCompound.getInteger("amplifier") + 1;
                    if (amplifier <= 0) {
                        amplifier = 1;
                    }
                    potions.put(Potion.REGISTRY.getObject(resourceLocation), amplifier);
                }
            }
        }
        return potions;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (!stack.isEmpty() && NBTUtil.hasTag(stack, TagPotList)) {
            tooltip.add(I18n.format("gui.tooltip.effects"));
            HashMap<Potion, Integer> potions = getPotion(stack);
            if (potions != null) {
                potions.forEach((potion, value) -> tooltip.add(I18n.format(potion.getName()) + ": " + value));
            }
        } else {
            tooltip.add(I18n.format("gui.tooltip.noeffects"));
        }
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        HashMap<Potion, Integer> potions = getPotion(stack);
        if (potions != null && potions.keySet().stream().count() == potions.values().stream().count() &&
                potions.values().stream().count() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onEquipped(ItemStack stack, EntityLivingBase player) {
        updateEffect(player, stack);
    }

    @Override
    public void onUnequipped(ItemStack stack, EntityLivingBase player) {
        updateEffect(player, stack);
    }

    public void updateEffect(EntityLivingBase player, ItemStack stack) {
        if (!player.world.isRemote) {
            if (stack.isEmpty() || !(player instanceof EntityPlayer)) return;
            HashMap<Potion, Integer> stack_potion = getPotion(stack);
            if (stack_potion != null) {
                stack_potion.forEach((potion, level) -> {
                    player.removePotionEffect(potion);
                });
            }
            IInventory inventory = BaublesApi.getBaubles((EntityPlayer) player);
            ItemStack ringA = inventory.getStackInSlot(1);
            ItemStack ringB = inventory.getStackInSlot(2);
            HashMap<Potion, Integer> potionA = getPotion(ringA);
            HashMap<Potion, Integer> potionB = getPotion(ringB);
            if (potionA == null && potionB == null) return;
            HashMap<Potion, Integer> potions = potionA == null ? potionB : potionA;
            if (potionB != null && potionA != null) {
                potionB.forEach((key, value) -> potions.merge(key, value, (v1, v2) -> v1 + v2));
            }
            potions.forEach((potion, level) -> {
                level -= 1;
                player.removePotionEffect(potion);
                if (level < 0) { // Negative
                    level = 0;
                }
                if (255 < level) { // Over 255
                    level = 255;
                }
                player.addPotionEffect(new PotionEffect(potion, Integer.MAX_VALUE, level, true, false));
            });
        }
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemStack) {
        return BaubleType.RING;
    }
}
