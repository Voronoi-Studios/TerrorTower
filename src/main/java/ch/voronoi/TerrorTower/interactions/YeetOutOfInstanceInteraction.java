package ch.voronoi.TerrorTower.interactions;

import ch.voronoi.TerrorTower.TerrorPlugin;
import com.hypixel.hytale.builtin.instances.InstancesPlugin;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.asset.type.model.config.Model;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAsset;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.component.ModelComponent;
import com.hypixel.hytale.server.core.modules.entity.player.PlayerSkinComponent;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction;

public class YeetOutOfInstanceInteraction extends SimpleInstantInteraction {

    public static final BuilderCodec<YeetOutOfInstanceInteraction> CODEC = BuilderCodec.builder(
            YeetOutOfInstanceInteraction.class, YeetOutOfInstanceInteraction::new, SimpleInstantInteraction.CODEC)
            .documentation("yeets everyone from the instance where player performing the interaction is")
            .build();
    
    @Override
    protected void firstRun(InteractionType type, InteractionContext ctx, CooldownHandler arg2) {
        var playerComp = ctx.getEntity().getStore().getComponent(ctx.getEntity(), Player.getComponentType());
        if(playerComp == null) {
            TerrorPlugin.getInstance().getLogger().atSevere().log("player doing the interaction doesnt have Player component and therefore isnt player wtf");
        }
        var world = playerComp.getWorld();
            world.execute(()->{
                ModelAsset masset = ModelAsset.getAssetMap().getAsset("Player");
                for(var playerRef :world.getPlayerRefs()) {
                    try {
                    ctx.getEntity().getStore().putComponent(playerRef.getReference(), ModelComponent.getComponentType(), new ModelComponent(Model.createScaledModel(masset, 1f)));
                    PlayerSkinComponent skin = ctx.getEntity().getStore().getComponent(playerRef.getReference(), PlayerSkinComponent.getComponentType());
                    skin.setNetworkOutdated();
                    } catch (Exception ignored) {
                        ignored.printStackTrace();
                    }
                    InstancesPlugin.exitInstance(playerRef.getReference(), ctx.getEntity().getStore());
                }
                InstancesPlugin.safeRemoveInstance(world);
            });
    }

}
