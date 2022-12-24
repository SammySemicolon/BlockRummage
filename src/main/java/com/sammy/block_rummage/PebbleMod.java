package com.sammy.block_rummage;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(PebbleMod.MODID)
public class PebbleMod {
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MODID = "block_rummage";

	public PebbleMod() {
	}

	public static ResourceLocation path(String path) {
		return new ResourceLocation(MODID, path);
	}
}