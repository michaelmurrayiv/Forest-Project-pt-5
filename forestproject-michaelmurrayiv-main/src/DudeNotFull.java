import processing.core.PImage;

import java.util.*;

public class DudeNotFull extends Dude {
    private int resourceCount;
    public DudeNotFull(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod, int resourceLimit){
        super(id, position, images, actionPeriod, animationPeriod, resourceLimit);
        this.resourceCount = 0;
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        List<Class> myList = new ArrayList<>();
        myList.add(Tree.class);
        myList.add(Sapling.class);

        Optional<Entity> target = world.findNearest(getPosition(), myList);
        if (target.isEmpty() || !moveTo(world, target.get(), scheduler) || !transformNotFull(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this, createActivityAction(world, imageStore), getActionPeriod());
        }
    }
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (Functions.adjacent(getPosition(), target.getPosition())) {

            resourceCount += 1;
            ((Plant)target).decreaseHealth();

            return true;
        } else {
            Point nextPos = nextPosition(world, target.getPosition());

            if (!getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }

    public boolean transformNotFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (resourceCount >= getResourceLimit()) {

            Entity dude = new DudeFull(getId(), getPosition(), getImages(), getActionPeriod(), getAnimationPeriod(), getResourceLimit());
            world.removeEntity(this, scheduler);

            scheduler.unscheduleAllEvents(this);

            world.addEntity(dude);
            ((HasAnimation)dude).scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }


}
