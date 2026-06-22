package ch.voronoi.TerrorTower.interactions;

import ch.voronoi.TerrorTower.TerrorPlugin;
import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerCondition;
import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerContext;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.server.core.universe.PlayerRef;

public class IsGhostCondition extends TriggerCondition {

    public static final BuilderCodec<IsGhostCondition> CODEC = BuilderCodec.builder(
            IsGhostCondition.class, IsGhostCondition::new, BASE_CODEC)
            .build();
    
    @Override
    public boolean test(TriggerContext context) {
        PlayerRef ref = context.getEntityRef().getStore().getComponent(context.getEntityRef(), PlayerRef.getComponentType());
        return TerrorPlugin.getInstance().teams.getOrDefault(ref, false);
    }

}
