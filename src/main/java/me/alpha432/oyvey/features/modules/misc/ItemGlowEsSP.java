package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class ItemGlowESP extends Module {

    private final MinecraftClient mc = MinecraftClient.getInstance();
    private final double RADIUS = 30.0;

    public ItemGlowESP() {
        super("ItemGlowESP", "Glowing players, mobs and items in 30 blocks", Category.MISC, true, false, false);
    }

    @Override
    public void onEnable() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (mc.player == null || mc.world == null) return;

            for (Entity entity : mc.world.getEntities()) {
                if (entity == mc.player) continue;

                double distance = entity.distanceTo(mc.player);

                if (distance <= RADIUS) {
                    if (entity instanceof PlayerEntity || entity instanceof MobEntity || entity instanceof ItemEntity) {
                        entity.setGlowing(true);
                    }
                } else {
                    if (entity instanceof PlayerEntity || entity instanceof MobEntity || entity instanceof ItemEntity) {
                        entity.setGlowing(false);
                    }
                }
            }
        });
    }

    @Override
    public void onDisable() {
        if (mc.world == null) return;

        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof PlayerEntity || entity instanceof MobEntity || entity instanceof ItemEntity) {
                entity.setGlowing(false);
            }
        }
    }
}
