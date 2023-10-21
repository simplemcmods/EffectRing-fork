package com.sysnote8.effectring;

import com.sysnote8.effectring.item.RingItem;
import com.sysnote8.effectring.manager.ItemManager;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Tags.modid, version = Tags.version, name = Tags.modname, acceptedMinecraftVersions = "[1.12.2]")
public class MainMod {

    public static final Logger LOGGER = LogManager.getLogger(Tags.modid);

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // register to the event bus so that we can listen to events
        MinecraftForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        ItemManager.register(event.getRegistry());
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void registerModels(ModelRegistryEvent event) {
        ItemManager.registerModels();
    }
}
