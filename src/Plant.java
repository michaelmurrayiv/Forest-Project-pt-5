import processing.core.PImage;

import java.util.List;

public abstract class Plant extends Living {

    private int health;
    public static final double TREE_ANIMATION_MAX = 0.600;
    public static final double TREE_ANIMATION_MIN = 0.050;
    public static final double TREE_ACTION_MAX = 1.400;
    public static final double TREE_ACTION_MIN = 1.000;
    public static final int TREE_HEALTH_MAX = 3;
    public static final int TREE_HEALTH_MIN = 1;
    public Plant(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod, int health){
        super(id, position, images, actionPeriod, animationPeriod);
        this.health = health;
    }
    public void decreaseHealth() {health = health - 1;}
    public void increaseHealth() {health = health + 1;}
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        _executeActivity();
        if (!transformPlant(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this, createActivityAction(world, imageStore), getActionPeriod());
        }
    }
    abstract void _executeActivity();
    abstract boolean _transformPlant();
    public boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (health <= 0) {
            Entity stump = new Stump(WorldModel.getStumpKey() + "_" + getId(), getPosition(), imageStore.getImageList(WorldModel.getStumpKey()), 0);
            world.removeEntity(this, scheduler);
            world.addEntity(stump);

            return true;
        }
        if (_transformPlant()){
            Entity tree = new Tree(WorldModel.getTreeKey() + "_" + getId(), getPosition(), imageStore.getImageList(WorldModel.getTreeKey()), Functions.getNumFromRange(TREE_ACTION_MAX, TREE_ACTION_MIN), Functions.getNumFromRange(TREE_ANIMATION_MAX, TREE_ANIMATION_MIN), Functions.getIntFromRange(TREE_HEALTH_MAX, TREE_HEALTH_MIN));
            world.removeEntity(this, scheduler);

            world.addEntity(tree);
            ((HasAnimation)tree).scheduleActions(scheduler, world, imageStore);

            return true;

        }
        return false;
    }
    public int getHealth() {return health;}

}
