package com.sysnote8.effectring.item;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import com.sysnote8.effectring.MainMod;
import com.sysnote8.effectring.util.NBTUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import baubles.api.IBauble;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class RingItem extends ItemBase implements IBauble {
    private static final String TagPotList = "effects";
    public RingItem() {
        super("ring");
        setMaxStackSize(1);
    }

    public static HashMap<Potion,Integer> getPotion(ItemStack stack) {
        if(stack==null) return null;
        NBTTagList effectList = NBTUtil.getList(stack, TagPotList, Constants.NBT.TAG_COMPOUND, false);
        if(effectList.isEmpty()) return null;
        HashMap<Potion, Integer> potions = new HashMap<Potion, Integer>();
        for(int i=0;i<effectList.tagCount();i++) {
            NBTTagCompound tagCompound = effectList.getCompoundTagAt(i);
            potions.put(Potion.REGISTRY.getObject(new ResourceLocation(tagCompound.getString("id"))),tagCompound.getInteger("amplifier"));
        }
        return potions;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String displayName = super.getItemStackDisplayName(stack);
        if(NBTUtil.hasTag(stack,TagPotList)) {
            displayName+="(potions)";
        } else {
            displayName+="(empty)";
        }
        return displayName;
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return getPotion(stack) != null;
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
        if(!player.world.isRemote) {
            if (stack.isEmpty() || !(player instanceof EntityPlayer)) return;
            getPotion(stack).forEach((potion,level) -> {
                player.removePotionEffect(potion);
            });
            IInventory inventory = BaublesApi.getBaubles((EntityPlayer) player);
            ItemStack ringA = inventory.getStackInSlot(1);
            ItemStack ringB = inventory.getStackInSlot(2);
            HashMap<Potion, Integer> potionA = getPotion(ringA);
            HashMap<Potion, Integer> potionB = getPotion(ringB);
            if (potionA == null&&potionB == null) return;
            HashMap<Potion, Integer> potions = potionA==null?potionB:potionA;
            if (potionB != null && potionA != null) {
                potionB.forEach((key, value) -> potions.merge(key, value, Integer::sum));
            }
            potions.forEach((key, value) -> MainMod.LOGGER.info(key.getName() + "/" + value));
            potions.forEach((potion, level) -> {
                level-=1;
                player.removePotionEffect(potion);
                if (255 < level) {
                    level = 255;
                }
                if (1 <= level) {
                    player.addPotionEffect(new PotionEffect(potion, Integer.MAX_VALUE, level, true, false));
                }
            });
        }
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemStack) {
        return BaubleType.RING;
    }
}
