package ch.voronoi.TerrorTower.interactions;

import ch.voronoi.TerrorTower.TerrorPlugin;
import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerContext;
import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerEffect;
import com.hypixel.hytale.codec.builder.BuilderCodec;

//to be triggered by the lever at the bottom, aka game start condition
public class GameStartEffect extends TriggerEffect {

    
    public static final BuilderCodec<GameStartEffect> CODEC = BuilderCodec.builder(
            GameStartEffect.class, GameStartEffect::new, BASE_CODEC)
            .build();
    

    @Override
    public void execute(TriggerContext arg0) {
        if(TerrorPlugin.getInstance().isGameRunning.get()) {
            TerrorPlugin.getInstance().getLogger().atInfo().log("got start effect call while game is already running");
            return;
        }
        TerrorPlugin.getInstance().getLogger().atInfo().log("started the game");
        TerrorPlugin.getInstance().isGameRunning.set(true);
    }

}
