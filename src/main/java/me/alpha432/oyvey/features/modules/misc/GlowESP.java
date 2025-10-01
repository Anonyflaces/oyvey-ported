package me.alpha432.oyvey.features.modules.misc;

import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class GlowESP extends Module {

    private final MinecraftClient mc = MinecraftClient.getInstance();

    public GlowESP() {
        super("GlowESP", "Glowing players and mobs in 30 blocks", Category.MISC, true, false, false);
    }

    @Override
    public void onEnable() {
        // Event registrieren, das bei jedem Client-Tick läuft
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (mc.player == null || mc.world == null) return;

            for (Entity entity : mc.world.getEntities()) {
                if (entity == mc.player) continue;

                double distance = entity.distanceTo(mc.player);

                if (distance <= 30) {
                    if (entity instanceof PlayerEntity || entity instanceof MobEntity) {
                        entity.setGlowing(true); // Leuchten aktivieren
                    }
                } else {
                    if (entity instanceof PlayerEntity || entity instanceof MobEntity) {
                        entity.setGlowing(false); // Entfernte Spieler/Mobs ausschalten
                    }
                }
            }
        });
    }

    @Override
    public void onDisable() {
        if (mc.world == null) return;

        // Glow entfernen, wenn Modul deaktiviert wird
        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof PlayerEntity || entity instanceof MobEntity) {
                entity.setGlowing(false);
            }
        }
    }
}
