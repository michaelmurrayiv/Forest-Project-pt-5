import processing.core.PImage;

import java.util.*;

public class DudeFull extends Dude {

    public DudeFull(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod,int resourceLimit, int health){
        super(id, position, images, actionPeriod, animationPeriod, resourceLimit, health);
    }
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        List<Class> myList = new ArrayList<>();
        myList.add(House.class);

        Optional<Entity> fullTarget = world.findNearest(getPosition(), myList);

        if (fullTarget.isPresent() && moveTo(world, fullTarget.get(), scheduler)) {
            transformFull(world, scheduler, imageStore);
        } else {
            scheduler.scheduleEvent(this, createActivityAction(world, imageStore), getActionPeriod());
        }
    }
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (Functions.adjacent(getPosition(), target.getPosition())) {

            return true;
        } else {
            Point nextPos = nextPosition(world, target.getPosition());

            if (!getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }
    public boolean transformFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        Entity dude = new DudeNotFull(getId(), getPosition(), getImages(), getActionPeriod(), getAnimationPeriod(), getResourceLimit(), getHealth());
        world.removeEntity(this, scheduler);

        world.addEntity(dude);
        ((HasAnimation)dude).scheduleActions(scheduler, world, imageStore);
        return true;
    }

}
