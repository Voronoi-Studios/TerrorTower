package ch.voronoi.TerrorTower.hud;

import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;

@Deprecated //in case we keep getting issues with the visual effect
public class BlindnessHud extends CustomUIHud {
    public BlindnessHud(PlayerRef playerRef, String key, int zOrder) {
        super(playerRef, key, zOrder);
    }

    @Override
    protected void build(UICommandBuilder builder) {

        builder.append("heighthud.ui");
    }


}
