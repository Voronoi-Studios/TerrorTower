package ch.voronoi.TerrorTower.hud;

import ch.voronoi.TerrorTower.TerrorPlugin;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.system.DelayedSystem;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class FloorHudSystem extends DelayedSystem<EntityStore> {

    public FloorHudSystem(float intervalSec) {
        super(intervalSec);
    }

    @Override
    public void delayedTick(float dt, int index, Store<EntityStore> store) {
        double heighest = 0;
        for(var playerRef : TerrorPlugin.getInstance().teams.keySet()) {
            if(!TerrorPlugin.getInstance().teams.getOrDefault(playerRef, false)) {
                TransformComponent transform = playerRef.getReference().getStore().getComponentConcurrent(playerRef.getReference(), TransformComponent.getComponentType());
                if(heighest < transform.getPosition().y) {
                    heighest = transform.getPosition().y;
                }
            }
            
        }
        
        
        for(var playerRef : TerrorPlugin.getInstance().teams.keySet()) { //TODO iterate all players online instead of the ones in the game, remove agressive logging
                        
            Player player = playerRef.getReference().getStore().getComponentConcurrent(playerRef.getReference(), Player.getComponentType());
            if(!TerrorPlugin.getInstance().isGameRunning.get()) { //game not running - remove hud
                if(player.getHudManager().getCustomHuds().containsKey("floorhud")) {
                    TerrorPlugin.getInstance().getLogger().atInfo().log("removing floorhud");
                    player.getHudManager().removeCustomHud(playerRef, "floorhud");
                }else {
                    TerrorPlugin.getInstance().getLogger().atInfo().log("no hud present to remove");
                }
            }else { //game is already running, update hud
                if(!player.getHudManager().getCustomHuds().containsKey("floorhud")) { //no hud yet - add hud
                    TerrorPlugin.getInstance().getLogger().atInfo().log("adding floorhud");
                    FloorHUD hud = new FloorHUD(playerRef, "floorhud", 0);
                    TerrorPlugin.getInstance().getLogger().atInfo().log("adding hud");
                    player.getHudManager().addCustomHud(playerRef, hud);
                    hud.updateFloors(TerrorPlugin.getInstance().teams.getOrDefault(playerRef, false), heighest);
                }else { //update existing hud
                    var maybeHud = player.getHudManager().getCustomHud("floorhud");
                    if(!(maybeHud instanceof FloorHUD)) {
                        TerrorPlugin.getInstance().getLogger().atSevere().log("hud isnt instance of my hud, mod conflict!!!");
                        return;
                    }
                    FloorHUD hud = (FloorHUD) maybeHud;
                    hud.updateFloors(TerrorPlugin.getInstance().teams.getOrDefault(playerRef, false), heighest);
                }
            }
        };
    }
    
}
