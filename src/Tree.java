import processing.core.PImage;

import java.util.List;

public class Tree extends Plant {
    public Tree(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod, int health) {
        super(id, position, images, actionPeriod, animationPeriod, health);
    }
    public void _executeActivity() {}
    public boolean _transformPlant() {
        return false;
    }

}