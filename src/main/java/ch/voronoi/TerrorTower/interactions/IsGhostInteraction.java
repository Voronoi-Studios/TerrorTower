package ch.voronoi.TerrorTower.interactions;

import ch.voronoi.TerrorTower.TerrorPlugin;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.protocol.InteractionState;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInteraction;
import com.hypixel.hytale.server.core.universe.PlayerRef;

public class IsGhostInteraction extends SimpleInteraction {

    public static final BuilderCodec<IsGhostInteraction> CODEC = BuilderCodec.builder(
            IsGhostInteraction.class, IsGhostInteraction::new, SimpleInteraction.CODEC)
            .documentation("An interaction that is successful if the target entity is a player who is a ghost.")
            .build();

    @Override
    protected void tick0(boolean firstRun, float time, InteractionType type, InteractionContext context, CooldownHandler cooldownHandler) {
        PlayerRef ref = context.getEntity().getStore().getComponent(context.getEntity(), PlayerRef.getComponentType());
        boolean success = false;
        if(ref==null) {
            success = false;
        }
        if(TerrorPlugin.getInstance().teams.getOrDefault(ref, false)) {
            success = true;
        }

        context.getState().state = success ? InteractionState.Finished : InteractionState.Failed;
        super.tick0(firstRun, time, type, context, cooldownHandler);
    }

}
