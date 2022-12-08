import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public abstract class Dude extends Person {

    private final int resourceLimit;
    private int health;


    public Dude(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod, int resourceLimit, int health) {
        super(id, position, images,  actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
        this.health = health;
    }

    public Point nextPosition(WorldModel world, Point destPos) {
        PathingStrategy strat = new AStarPathingStrategy();
     //   PathingStrategy strat = new SingleStepPathingStrategy();
        List<Point> path = strat.computePath(
                this.getPosition(),
                destPos,
                (p) -> (!(world.isOccupied(p)) || world.getOccupancyCell(p).getClass() == Stump.class) && world.withinBounds(p),
                Functions::adjacent,
                PathingStrategy.CARDINAL_NEIGHBORS
        );


     /*   int horiz = Integer.signum(destPos.x - getPosition().x);
        Point newPos = new Point(getPosition().x + horiz, getPosition().y);

        if (horiz == 0 || world.isOccupied(newPos) && world.getOccupancyCell(newPos).getClass() != Stump.class) {
            int vert = Integer.signum(destPos.y - getPosition().y);
            newPos = new Point(getPosition().x, getPosition().y + vert);

            if (vert == 0 || world.isOccupied(newPos) && world.getOccupancyCell(newPos).getClass() != Stump.class) {
                newPos = getPosition();
            }
        }*/

        if (path.size() == 0) { return this.getPosition();}

        return path.get(0);
    }


    public int getResourceLimit(){return resourceLimit;}

    public void decreaseHealth(){
        this.health--;
    }
    public int getHealth(){return health;}
    public boolean transformZombie(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        Entity zombie = new Zombie("zombie", getPosition(), imageStore.getImageList("zombie"), 1, .2);
        world.removeEntity(this, scheduler);

        world.addEntity(zombie);
        ((HasAnimation)zombie).scheduleActions(scheduler, world, imageStore);
        return true;
    }
}