package ch.voronoi.TerrorTower.hud;

import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;


public class FloorHUD extends CustomUIHud {
    public FloorHUD(PlayerRef playerRef, String key, int zOrder) {
        super(playerRef, key, zOrder);
    }

    private static final int STARTING_HEIGHT = 29; 
    
    @Override
    protected void build(UICommandBuilder builder) {
        builder.append("heighthud.ui");
    }
    

    public void updateFloors(boolean isGhost, double heighestPosition) {

        var transform = this.getPlayerRef().getTransform();
        double height = transform.getPosition().y;
        int floor = (int)((height - STARTING_HEIGHT) / 7);
        
        UICommandBuilder builder = new UICommandBuilder();
        builder.set("#SelfFloor.Text", "Current floor: "+floor);
        if(!isGhost) {
            builder.set("#HeighestFloor.Visible", false);
        }else {
            builder.set("#HeighestFloor.Visible", true);
            int heighest = (int)((heighestPosition - STARTING_HEIGHT) / 7);
            
            builder.set("#HeighestFloor.Text", "Heighest explorer floor: "+heighest);
        }
        
        update(false, builder);  // false = don't clear existing HUD
    }
    
}
