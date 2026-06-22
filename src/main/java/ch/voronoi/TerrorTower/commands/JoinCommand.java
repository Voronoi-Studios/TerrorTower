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

public class JoinCommand extends AbstractPlayerCommand {

    public JoinCommand() {
        super("join", "join the waiting lobby");
    }

    @Override
    protected void execute(CommandContext commandContext, Store<EntityStore> store, Ref<EntityStore> ref, PlayerRef playerRef, World world) {
        //check if the game is running, and if it isnt - 
        //teleport player to the lobby instance, clear inventory, effects, and gamemode
        if(TerrorPlugin.getInstance().isGameRunning.get()) {
            playerRef.sendMessage(Message.raw("game is already running, please wait for it to end"));
            return;
        }
        World targetWorld = Universe.get().getWorld(TerrorPlugin.INSTANCE_NAME);

        if (targetWorld == null) {
            commandContext.sendMessage(Message.raw("World " + TerrorPlugin.INSTANCE_NAME+" not found - the game needs initial setup or the world isnt ready"));
            return;
        }

        TransformComponent transformComponent = store.getComponentConcurrent(ref, TransformComponent.getComponentType());
        Transform transform;
        if(transformComponent == null) {
            transform = null;
        }else {
            transform = transformComponent.getTransform();
        }
        playerRef.sendMessage(Message.raw("Joining the instance..."));
        InstancesPlugin.teleportPlayerToInstance(ref, store, targetWorld, transform);
        playerRef.sendMessage(Message.raw("TODO: GAME RULES HERE"));
        //TODO clear inventory, effects, force gamemode - maybe via trigger effects?
    }

}
