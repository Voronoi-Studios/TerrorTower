package ch.voronoi.TerrorTower.interactions;

import ch.voronoi.TerrorTower.TerrorPlugin;
import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerContext;
import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerEffect;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.model.config.Model;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAsset;
import com.hypixel.hytale.server.core.modules.entity.component.ModelComponent;
import com.hypixel.hytale.server.core.modules.entity.player.PlayerSkinComponent;
import com.hypixel.hytale.server.core.util.EventTitleUtil;

///to be triggered by lever at the top floor, aka explorers win condition
public class GameEndEffect extends TriggerEffect {

    public static final BuilderCodec<GameEndEffect> CODEC = BuilderCodec.builder(
            GameEndEffect.class, GameEndEffect::new, BASE_CODEC)
            .build();
    
    @Override
    public void execute(TriggerContext ctx) {
        /*
        World targetWorld = Universe.get().getWorld(TerrorPlugin.INSTANCE_NAME);
        if (targetWorld == null) {
            TerrorPlugin.getInstance().getLogger().atWarning().log("didnt find instance");
        }else {
            targetWorld.execute(() -> { //close current instance
                InstancesPlugin.safeRemoveInstance(targetWorld); //teleports everyone out of the instance
            });
        }
        
        */
        TerrorPlugin.getInstance().getLogger().atInfo().log("ending the game - explorers won");
        ModelAsset masset = ModelAsset.getAssetMap().getAsset("Player");
        TerrorPlugin.getInstance().teams.forEach((playerRef, isGhost)->{
            
            if(isGhost) {
                EventTitleUtil.showEventTitleToPlayer(playerRef, Message.raw("Explorers win"), Message.raw("Game over"), false, "", 10F, 1.5F, 1.5F);
                
            }else {
                EventTitleUtil.showEventTitleToPlayer(playerRef, Message.raw("Explorers win"), Message.raw("Victory"), true, "", 10F, 1.5F, 1.5F);
            }
            ctx.getStore().putComponent(playerRef.getReference(), ModelComponent.getComponentType(), new ModelComponent(Model.createScaledModel(masset, 1f)));
            PlayerSkinComponent skin = ctx.getStore().getComponent(playerRef.getReference(), PlayerSkinComponent.getComponentType());
            skin.setNetworkOutdated();
            
        });
        
        //reset the roles
        TerrorPlugin.getInstance().teams.clear();
        TerrorPlugin.getInstance().isGameRunning.set(false);
    }

}
