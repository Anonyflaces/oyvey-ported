package me.alpha432.oyvey.features.modules.render;

import com.google.common.eventbus.Subscribe;
import me.alpha432.oyvey.event.impl.Render3DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.render.RenderUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.Box;

import java.awt.*;

public class ItemESP extends Module {

    public Setting<Double> range = register(new Setting<>("Range", 50.0, 5.0, 100.0));
    public Setting<String> color = register(new Setting<>("Color", "Red")); // Red, Green, Blue, Yellow

    public ItemESP() {
        super("ItemESP", "ESP for dropped items through walls", Category.RENDER, true, false, true);
    }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        if (mc.world == null || mc.player == null) return;

        // Hier nutzen wir direkt die Entities im World-Iterable
        for (Entity entity : mc.world.getEntities()) {
            if (!(entity instanceof ItemEntity item)) continue;

            double distanceSq = mc.player.squaredDistanceTo(item);
            if (distanceSq > range.getValue() * range.getValue()) continue;

            Box box = item.getBoundingBox();
            RenderUtil.drawBox(event.getMatrix(), box, getColor(), 1.5f);
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
