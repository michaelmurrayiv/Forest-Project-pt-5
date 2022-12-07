import processing.core.PImage;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Plane extends Living {

    public Plane(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
    }
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

            if (moveTo(world, findTargetPosition(), scheduler)) {

                world.removeEntity(this, scheduler);
            }
        

        scheduler.scheduleEvent(this, createActivityAction(world, imageStore), getActionPeriod());
    }

    private Point findTargetPosition()
    {
        //get other side of the world
        Point target = new Point(40,this.getPosition().getY());
        return target;
        
    }
    public Point nextPosition(WorldModel world, Point destPos) {
        // PathingStrategy strat = new AStarPathingStrategy();
        PathingStrategy strat = new SingleStepPathingStrategy();
        List<Point> path = strat.computePath(
                this.getPosition(),
                destPos,
                (p) -> world.withinBounds(p),
                Functions::adjacent,
                PathingStrategy.CARDINAL_NEIGHBORS
                );

        if (path.size() == 0) { return this.getPosition();}

        return path.get(0);
    }
    public boolean moveTo(WorldModel world, Point target, EventScheduler scheduler) {
        if (Functions.adjacent(getPosition(), target)) {

            return true;
        } else {
            Point nextPos = nextPosition(world, target);

            if (!getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }

}
