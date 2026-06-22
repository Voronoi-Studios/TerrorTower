package ch.voronoi.TerrorTower.commands;

import ch.voronoi.TerrorTower.TerrorPlugin;
import com.hypixel.hytale.builtin.instances.InstancesPlugin;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class StopCommand extends AbstractPlayerCommand {

    public StopCommand() {
        super("stop", "force stop currently running game");
        requirePermission("tower.stop");
    }

    @Override
    protected void execute(CommandContext commandContext, Store<EntityStore> store, Ref<EntityStore> ref, PlayerRef playerRef, World world) {
        
        playerRef.sendMessage(Message.raw("forcefully stopping the game"));
        TerrorPlugin.getInstance().isGameRunning.set(false);
            
        World targetWorld = Universe.get().getWorld(TerrorPlugin.INSTANCE_NAME);

        if (targetWorld == null) {
            commandContext.sendMessage(Message.raw("World " + TerrorPlugin.INSTANCE_NAME+" not found - cant delete instance"));
        }else {
            targetWorld.execute(() -> { //close current instance
                InstancesPlugin.safeRemoveInstance(targetWorld); //teleports everyone out of the instance
            });
        }

        
        //TODO @Jonas close current tower instance, create a new one instead as if the game ended?
        
        //create new tower instance, but dont teleport anyone in yet
        
    }

}
