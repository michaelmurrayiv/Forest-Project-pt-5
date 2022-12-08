import processing.core.PImage;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Fighter extends Person {

    public Fighter(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
    }
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        List<Class> myList = new ArrayList<>();
        myList.add(Zombie.class);
        Optional<Entity> fighterTarget = world.findNearest(getPosition(), myList);

        if (fighterTarget.isPresent()) {
            Point tgtPos = fighterTarget.get().getPosition();

            if ((moveTo(world, fighterTarget.get(), scheduler)) && ((Zombie)fighterTarget.get()).getHealth() == 0) {
                world.setBackgroundCell(fighterTarget.get().getPosition(), new Background("tombstone", imageStore.getImageList("tombstone")));
                world.removeEntity((fighterTarget.get()),scheduler);

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
                PathingStrategy.CARDINAL_NEIGHBORS
        );

        if (path.size() == 0) { return this.getPosition();}

        return path.get(0);
    }
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (Functions.adjacent(getPosition(), target.getPosition())) {
            Zombie z = (Zombie) target;
            z.decreaseHealth();
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
