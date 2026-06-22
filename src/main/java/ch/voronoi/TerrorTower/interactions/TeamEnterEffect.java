package ch.voronoi.TerrorTower.interactions;

import java.util.concurrent.TimeUnit;

import ch.voronoi.TerrorTower.TerrorPlugin;
import com.hypixel.hytale.builtin.instances.InstancesPlugin;
import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerContext;
import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerEffect;
import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerEventType;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.model.config.Model;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAsset;
import com.hypixel.hytale.server.core.entity.effect.EffectControllerComponent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.component.ModelComponent;
import com.hypixel.hytale.server.core.modules.entity.player.PlayerSkinComponent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.EventTitleUtil;

public class TeamEnterEffect extends TriggerEffect {

    private Boolean isGhost = false;
    
    public static final BuilderCodec<TeamEnterEffect> CODEC = BuilderCodec.builder(TeamEnterEffect.class, TeamEnterEffect::new, BASE_CODEC)
            .append(new KeyedCodec<>("GHOST", Codec.BOOLEAN), (effect, value) -> effect.isGhost = value, effect -> effect.isGhost).add()
            .build();
    
    
    public static void assignTeam(Store<EntityStore> store, PlayerRef player, boolean ghost, boolean showMessage) {

        TerrorPlugin.getInstance().getLogger().atInfo().log("began assigning team to player");
        
        TerrorPlugin.getInstance().teams.put(player, ghost);
        
        var effectController = store.getComponent(player.getReference(), EffectControllerComponent.getComponentType());
        if(effectController == null) {
            //no effects probably - nothing to clear
        }else {
            effectController.clearEffects(player.getReference(), store);
            TerrorPlugin.getInstance().getLogger().atInfo().log("cleared player effects");
        }
        

        if(TerrorPlugin.getInstance().teams.values().stream().allMatch(isGhost -> isGhost) && TerrorPlugin.getInstance().isGameRunning.get()) { //ghosts won

            if(showMessage) {
                TerrorPlugin.getInstance().teams.forEach((ref, _)->{ //show everyone a message
                    boolean isVictory = true;
                    if(ref == player) isVictory = false; //last player who died gets a less sparkly message
                    EventTitleUtil.showEventTitleToPlayer(ref, Message.raw("Terrors win"), Message.raw("Game over"), isVictory, "", 10F, 0.5F, 0.5F);
                });
            }
            
            TerrorPlugin.getInstance().getLogger().atInfo().log("ended game - Terrors won");
            TerrorPlugin.getInstance().isGameRunning.set(false);
            TerrorPlugin.getInstance().teams.clear();
            TerrorPlugin.getInstance().getLogger().atInfo().log("cleared team and stopped game. isGameRunning"+TerrorPlugin.getInstance().isGameRunning.get());
            var playerComp = store.getComponent(player.getReference(), Player.getComponentType());
            var world = playerComp.getWorld();
            TerrorPlugin.getInstance().getLogger().atInfo().log("started 20 second timer");
            TerrorPlugin.executorService.schedule(()->{
                TerrorPlugin.getInstance().getLogger().atInfo().log("20 seconds passed - removing instance");
                world.execute(()->{
                    TerrorPlugin.getInstance().getLogger().atInfo().log("hopped to world thread");
                    ModelAsset masset = ModelAsset.getAssetMap().getAsset("Player");
                    
                    for(var playerRef :world.getPlayerRefs()) {
                        try {
                            store.putComponent(playerRef.getReference(), ModelComponent.getComponentType(), new ModelComponent(Model.createScaledModel(masset, 1f)));
                            PlayerSkinComponent skin = store.getComponent(playerRef.getReference(), PlayerSkinComponent.getComponentType());
                            skin.setNetworkOutdated();
                        } catch (Exception ignored) {
                            ignored.printStackTrace();
                        }
                        InstancesPlugin.exitInstance(playerRef.getReference(), store);
                    }
                    InstancesPlugin.safeRemoveInstance(world);
                });
            }, 20, TimeUnit.SECONDS);
        }
        
        if(ghost) {
            TerrorPlugin.getInstance().getLogger().atInfo().log("assigning ghost as a team");
            if(showMessage) EventTitleUtil.showEventTitleToPlayer(player, Message.raw("You are now a Terror"), Message.raw(" "), false, "", 3F, 0.5F, 0.5F);
            
            ModelAsset masset = ModelAsset.getAssetMap().getAsset("Ghost");
            float scale = 0.7F; 
            if(masset != null) {
                TerrorPlugin.getInstance().getLogger().atInfo().log("applying ghost model");
                store.putComponent(player.getReference(), ModelComponent.getComponentType(), new ModelComponent(Model.createScaledModel(masset, scale)));
            }else {
                TerrorPlugin.getInstance().getLogger().atInfo().log("couldnt find asset ghost");
            }
        }else {
            TerrorPlugin.getInstance().getLogger().atInfo().log("assigning Explorer as a team");
            if(showMessage) EventTitleUtil.showEventTitleToPlayer(player, Message.raw("You chosen team Explorers"), Message.raw("Good luck!"), false, "", 3F, 0.5F, 0.5F);

            TerrorPlugin.getInstance().getLogger().atInfo().log("applying player model");
            ModelAsset masset = ModelAsset.getAssetMap().getAsset("Player");
            store.putComponent(player.getReference(), ModelComponent.getComponentType(), new ModelComponent(Model.createScaledModel(masset, 1f)));
            PlayerSkinComponent skin = store.getComponent(player.getReference(), PlayerSkinComponent.getComponentType());
            skin.setNetworkOutdated();
        }
        
    }
    
    
    @Override
    public void execute(TriggerContext context) {
        if(context.getEventType().equals(TriggerEventType.ENTER) || context.getEventType().equals(TriggerEventType.EXIT)) {
            var playerRef = context.getStore().getComponent(context.getEntityRef(), PlayerRef.getComponentType());
            assignTeam(context.getStore(), playerRef, isGhost, false);
           
        }else {
            TerrorPlugin.getInstance().getLogger().atWarning().log("using TeamEnterEffect not via enter/exit event, whyyyyy (no code ran)");
        }
    }
}
