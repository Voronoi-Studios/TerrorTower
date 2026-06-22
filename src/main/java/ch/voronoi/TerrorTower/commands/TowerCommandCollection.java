package ch.voronoi.TerrorTower.commands;

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;

public class TowerCommandCollection extends AbstractCommandCollection  {

    public TowerCommandCollection() {
        super("tower", "Tower minigame commands");
        addSubCommand(new HelpCommand());
        addSubCommand(new JoinCommand());
        //addSubCommand(new StartCommand()); -> use GameStartInteraction instead
        addSubCommand(new StopCommand());
        addSubCommand(new InitialSetupCommand());
    }

}
