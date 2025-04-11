package me.danvb10.mtsr;

import me.danvb10.mtsr.init.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.api.ClientModInitializer;

@Environment(EnvType.CLIENT)
public class ClientEntrypoint implements ClientModInitializer {
	public static final String MOD_ID = "mtsr";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}

	@Override
	public void onInitializeClient() {
		Config.init();

		// Logic

	}
}