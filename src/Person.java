import processing.core.PImage;

import java.util.List;

public abstract class Person extends Living{

    public Person(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
    }
    abstract boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler);

    abstract Point nextPosition(WorldModel world, Point destPos);
}
