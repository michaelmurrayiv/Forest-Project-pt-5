import processing.core.PImage;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Zombie extends Person {

    public Zombie(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
    }
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        List<Class> myList = new ArrayList<>();
        myList.add(DudeNotFull.class);
        myList.add(DudeFull.class);
        Optional<Entity> zombieTarget = world.findNearest(getPosition(), myList);

        if (zombieTarget.isPresent()) {
            Point tgtPos = zombieTarget.get().getPosition();

            if ((moveTo(world, zombieTarget.get(), scheduler)) && ((Dude)zombieTarget.get()).getHealth() == 0) {
                world.setBackgroundCell(zombieTarget.get().getPosition(), new Background("tombstone", imageStore.getImageList("tombstone")));
                ((Dude) zombieTarget.get()).transformZombie(world, scheduler, imageStore);
            }
        }

        scheduler.scheduleEvent(this, createActivityAction(world, imageStore), getActionPeriod());
    }
    public Point nextPosition(WorldModel world, Point destPos) {
        PathingStrategy strat = new AStarPathingStrategy();
     //   PathingStrategy strat = new SingleStepPathingStrategy();
        List<Point> path = strat.computePath(
                this.getPosition(),
                destPos,
                (p) -> (!(world.isOccupied(p))) && world.withinBounds(p),
                Functions::adjacent,
                PathingStrategy.DIAGONAL_CARDINAL_NEIGHBORS
                );

        if (path.size() == 0) { return this.getPosition();}

        return path.get(0);
    }
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (Functions.adjacent(getPosition(), target.getPosition())) {
            Dude d = (Dude)target;
            d.decreaseHealth();
            return true;
        } else {
            Point nextPos = nextPosition(world, target.getPosition());

            if (!getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }


}
