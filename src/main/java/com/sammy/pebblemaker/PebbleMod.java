package com.sammy.pebblemaker;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(PebbleMod.MODID)
public class PebbleMod {
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MODID = "pebble_maker";
	public static final RandomSource RANDOM = RandomSource.create();

	public PebbleMod() {
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;
	}

	public static ResourceLocation path(String path) {
		return new ResourceLocation(MODID, path);
	}
}