package ch.voronoi.TerrorTower.commands;

import ch.voronoi.TerrorTower.TerrorPlugin;
import com.hypixel.hytale.builtin.instances.InstancesPlugin;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class InitialSetupCommand extends AbstractPlayerCommand {

    public InitialSetupCommand() {
        super("setup", "initial setup before first game run");
        requirePermission("tower.setup");
    }

    @Override
    protected void execute(CommandContext commandContext, Store<EntityStore> store, Ref<EntityStore> ref, PlayerRef playerRef, World world) {
        if(TerrorPlugin.getInstance().isGameRunning.get()) {
            playerRef.sendMessage(Message.raw("the game is already running, what are you trying to do?"));
            return;
        }
        
        //TODO @Jonas
        //generate instance 
        Transform returnPoint = playerRef.getTransform();

        World instanceWorldoriginal = Universe.get().getWorld("tower"); 
        //TODO seems that instances are just copies of existing worlds. Do we really need instances rather than straight up new worlds?
        //Can we forcce-regenerate worlds instead?
        
        // Spawn an instance named "Challenge_Combat_1"
        InstancesPlugin.get().spawnInstance(TerrorPlugin.INSTANCE_NAME, instanceWorldoriginal, returnPoint).thenAccept(instanceWorld -> {

            Universe.get().sendMessage(Message.raw("Instance ready: " + instanceWorld.getName()));
            

            for(PlayerRef pl :Universe.get().getPlayers()) { //join everyone in the server after its done
                TransformComponent transformComponent = pl.getReference().getStore().getComponentConcurrent(pl.getReference(), TransformComponent.getComponentType());
                Transform transform;
                if(transformComponent == null) {
                    transform = null;
                }else {
                    transform = transformComponent.getTransform();
                }
                InstancesPlugin.teleportPlayerToInstance(pl.getReference(), pl.getReference().getStore(), instanceWorld, transform);
                //potentially teleport to starting area? how is that even marked anywhere?
            }
        });
        
    }

}
