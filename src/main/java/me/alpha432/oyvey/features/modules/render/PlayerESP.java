package me.alpha432.oyvey.features.modules.render;

import com.google.common.eventbus.Subscribe;
import me.alpha432.oyvey.event.impl.Render3DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.Module.Category;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.render.RenderUtil;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.MinecraftClient;

import java.awt.*;
import java.util.List;

public class PlayerESP extends Module {

    private final MinecraftClient mc = MinecraftClient.getInstance();

    public Setting<Double> range = register(new Setting<>("Range", 50.0, 5.0, 100.0));
    public Setting<String> color = register(new Setting<>("Color", "Red")); // Red, Green, Blue, Yellow

    public PlayerESP() {
        super("PlayerESP", "ESP for players through walls", Category.RENDER, true, false, true);
    }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        if (mc.world == null || mc.player == null) return;

        List<AbstractClientPlayerEntity> players = mc.world.getPlayers();

        for (AbstractClientPlayerEntity player : players) {
            if (player == mc.player) continue;

            double distanceSq = mc.player.squaredDistanceTo(player);
            if (distanceSq > range.getValue() * range.getValue()) continue;

           
            RenderUtil.drawBox(event.getMatrix(), player.getBoundingBox(), getColor(), 2.0f);
        }
    }

    private Color getColor() {
        return switch (color.getValue().toLowerCase()) {
            case "green" -> new Color(0, 255, 0, 150);
            case "blue" -> new Color(0, 0, 255, 150);
            case "yellow" -> new Color(255, 255, 0, 150);
            default -> new Color(255, 0, 0, 150);
        };
    }
}
