import processing.core.PImage;

import java.util.List;

public class Sapling extends Plant {
    private final int healthLimit;
    public Sapling(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod, int health){
        super(id, position, images, actionPeriod, animationPeriod, health);
        this.healthLimit = 5;

    }
    public void _executeActivity(){
        increaseHealth();
    }
    public boolean _transformPlant() {
        if (getHealth() >= healthLimit) {
            return true;
        } else { return false;}
    }
}
