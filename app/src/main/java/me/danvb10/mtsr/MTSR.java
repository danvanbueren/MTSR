package me.danvb10.mtsr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;

import net.minecraft.resource.ResourceType;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import me.danvb10.mtsr.config.MTSRConfig;
import me.danvb10.mtsr.upscale.UpscaleManager;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class MTSR implements ModInitializer, ClientModInitializer {
	public static final String MOD_ID = "mtsr";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		MTSRConfig.init();
	}

	@Override
	public void onInitializeClient() {

		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new IdentifiableResourceReloadListener() {
			@Override
			public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
				return CompletableFuture.runAsync(() -> {

					manager.findResources("textures", id -> id.getPath().endsWith(".png"))
							.forEach((id, resource) -> {
								if (UpscaleManager.shouldUpscale(id)) {
									try {
										BufferedImage input = UpscaleManager.readImage(resource.getInputStream());
										BufferedImage upscaled = UpscaleManager.getUpscaledImage(id, input);
										UpscaleManager.injectTexture(id, upscaled);
									} catch (IOException e) {
										System.err.println("Failed to process texture: " + id);
										e.printStackTrace();
									} catch (NoSuchAlgorithmException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
							});

				}, applyExecutor);
			}

			@Override
			public Identifier getFabricId() {
				return new Identifier(MOD_ID, "resource_reload_listener");
			}
		});

	}
}