import processing.core.PImage;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Bomb extends Person {
//TODO: two blocks up, down, right, left, and one diagonal. Percent chance that a block gets hit. If it hits water, turn to stone, else turn to fire. 
    public Bomb(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
    }
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        List<Class> myList = new ArrayList<>();
        myList.add(Stump.class);
        Optional<Entity> fairyTarget = world.findNearest(getPosition(), myList);

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().getPosition();

            if (moveTo(world, fairyTarget.get(), scheduler)) {

                Entity sapling = new Sapling(WorldModel.getSaplingKey() + "_" + ((Stump)fairyTarget.get()).getId(), tgtPos, imageStore.getImageList(WorldModel.getSaplingKey()), 1.000, 1.000,0);
                world.addEntity(sapling);
                ((HasAnimation)sapling).scheduleActions(scheduler, world, imageStore);
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

            world.removeEntity(target, scheduler);

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
