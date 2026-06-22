package ch.voronoi.TerrorTower.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class HelpCommand extends AbstractPlayerCommand {

    public HelpCommand() {
        super("help", "show usage for tower minigame commands");
    }

    @Override
    protected void execute(CommandContext commandContext, Store<EntityStore> store, Ref<EntityStore> ref, PlayerRef playerRef, World world) {
        //TODO send usage info
        playerRef.sendMessage(Message.raw("TODO - FILL OUT USAGE INFO"));

    }

}
