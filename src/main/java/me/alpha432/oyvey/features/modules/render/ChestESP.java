package me.alpha432.oyvey.features.modules.render;

import com.google.common.eventbus.Subscribe;
import me.alpha432.oyvey.event.impl.Render3DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.Module.Category;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.render.RenderUtil;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.awt.*;

public class ChestESP extends Module {

    private final MinecraftClient mc = MinecraftClient.getInstance();

    public Setting<Double> range = register(new Setting<>("Range", 50.0, 5.0, 100.0));
    public Setting<String> color = register(new Setting<>("Color", "Red"));

    public ChestESP() {
        super("ChestESP", "ESP for chests through walls", Category.RENDER, true, false, true);
    }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        if (mc.world == null || mc.player == null) return;

        int radius = range.getValue().intValue();
        BlockPos playerPos = mc.player.getBlockPos();

        // Wir durchsuchen einen Würfel um den Spieler
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos pos = playerPos.add(x, y, z);
                    BlockState state = mc.world.getBlockState(pos);

                    if (state.getBlock() instanceof ChestBlock) {
                        Box box = new Box(pos.getX(), pos.getY(), pos.getZ(),
                                          pos.getX() + 1.0, pos.getY() + 1.0, pos.getZ() + 1.0);
                        RenderUtil.drawBox(event.getMatrix(), box, getColor(), 2.0f);
                    }
                }
            }
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
