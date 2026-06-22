package ch.voronoi.TerrorTower;

import ch.voronoi.TerrorTower.interactions.TeamEnterEffect;
import com.hypixel.hytale.component.Archetype;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage.EntitySource;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class OnPlayerHitPlayer extends EntityEventSystem<EntityStore, Damage> {
    public OnPlayerHitPlayer() {
        super(Damage.class);
    }

    @Override
    public void handle(int index,
                        ArchetypeChunk<EntityStore> archetypeChunk,
                        Store<EntityStore> store,
                        CommandBuffer<EntityStore> commandBuffer,
                        Damage event) {
        TerrorPlugin.getInstance().getLogger().atInfo().log("entity damage event");
        if(event.getSource() instanceof EntitySource) {
            TerrorPlugin.getInstance().getLogger().atInfo().log("source is entity");
            EntitySource source = (EntitySource) event.getSource();
            var damagerRef = source.getRef();
            var victimRef = archetypeChunk.getReferenceTo(index);
            

            PlayerRef damager = store.getComponent(damagerRef, PlayerRef.getComponentType());
            PlayerRef victim = store.getComponent(victimRef, PlayerRef.getComponentType());
            if(damager == null || victim == null) {
                TerrorPlugin.getInstance().getLogger().atInfo().log("either source or victim are not player");
                //not player to player damage
                return;
            }
            if(!TerrorPlugin.getInstance().isGameRunning.get()) {
                TerrorPlugin.getInstance().getLogger().atInfo().log("the game isnt running so nothing happened");
                return;
            }
            boolean isDamagerGhost = TerrorPlugin.getInstance().teams.getOrDefault(damager, false);
            boolean isVictimGhost = TerrorPlugin.getInstance().teams.getOrDefault(victim, false);
            //TODO potential distance checks if we cant limit it through instance settings
            
            if(isDamagerGhost) {
                if(isVictimGhost) { //no friendly fire
                    event.setCancelled(true);
                }else { 

                    Player pl = store.getComponent(damagerRef, Player.getComponentType());
                    pl.getWorld().execute(()->{
                        TeamEnterEffect.assignTeam(store, victim, true, true);
                    });
                }
                
            }else { //attacker is player
                if(isVictimGhost) {
                    //TODO @Jonas do we allow players to punch away ghosts?
                }else {//no friendly fire
                    event.setCancelled(true);
                }
            }
        }

    }

    @Override
    public Query<EntityStore> getQuery() {
        return Archetype.empty();
    }
}