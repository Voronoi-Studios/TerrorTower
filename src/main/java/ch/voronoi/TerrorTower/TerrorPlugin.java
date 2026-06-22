package ch.voronoi.TerrorTower;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerCondition;
import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerEffect;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import ch.voronoi.TerrorTower.hud.FloorHudSystem;
import ch.voronoi.TerrorTower.interactions.GameEndEffect;
import ch.voronoi.TerrorTower.interactions.GameStartEffect;
import ch.voronoi.TerrorTower.interactions.IsGhostCondition;
import ch.voronoi.TerrorTower.interactions.IsGhostInteraction;
import ch.voronoi.TerrorTower.interactions.TeamEnterEffect;
import ch.voronoi.TerrorTower.interactions.YeetOutOfInstanceInteraction;


public class TerrorPlugin extends JavaPlugin {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    public ConcurrentHashMap<PlayerRef, Boolean> teams = new ConcurrentHashMap<PlayerRef, Boolean>(); //true - is ghost, false - is explorer
    public static final String INSTANCE_NAME = "tower_instance";

    public static final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public AtomicBoolean isGameRunning = new AtomicBoolean(false);

    private static TerrorPlugin instance;
    public static TerrorPlugin getInstance() {
        return instance;
    }
    public TerrorPlugin(JavaPluginInit init) {
        super(init);
        LOGGER.atInfo().log("Hello from %s version %s", this.getName(), this.getManifest().getVersion().toString());
        instance = this;
    }

    @Override
    protected void setup() {
        TerrorPlugin.getInstance().getLogger().atInfo().log("Setup entered");
        TriggerEffect.CODEC.register("TowerJoinTeam", TeamEnterEffect.class, TeamEnterEffect.CODEC);
        TriggerCondition.CODEC.register("IsGhostCondition", IsGhostCondition.class, IsGhostCondition.CODEC);

        this.getCodecRegistry(Interaction.CODEC).register("IsGhostInteraction", IsGhostInteraction.class, IsGhostInteraction.CODEC);

        this.getCodecRegistry(Interaction.CODEC).register("YeetOutOfInstanceInteraction", YeetOutOfInstanceInteraction.class, YeetOutOfInstanceInteraction.CODEC);

        TriggerEffect.CODEC.register("TowerGameStart", GameStartEffect.class, GameStartEffect.CODEC);
        TriggerEffect.CODEC.register("TowerGameReachRoof", GameEndEffect.class, GameEndEffect.CODEC);

        //this.getCommandRegistry().registerCommand(new TowerCommandCollection()); ///no commands

        this.getEntityStoreRegistry().registerSystem(new OnPlayerHitPlayer());

        this.getEntityStoreRegistry().registerSystem(new FloorHudSystem(0.5f));

        TerrorPlugin.getInstance().getLogger().atInfo().log("Setup done");
    }

    @Override
    protected void shutdown() {

    }
}
