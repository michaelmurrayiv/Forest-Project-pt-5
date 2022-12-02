import processing.core.PImage;

import java.util.List;

public abstract class Living implements HasAnimation, HasActivity {
    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;
    private final double animationPeriod;
    private final double actionPeriod;
    public Living(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod){
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }
    public List<PImage> getImages() {return images; }
    public int getImageIndex(){return imageIndex;}
    public void setPosition(Point position) {
        this.position = position;
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, createActivityAction(world, imageStore), actionPeriod);
        scheduler.scheduleEvent(this, createAnimationAction(0), getAnimationPeriod());
    }

    public Animation createAnimationAction(int repeatCount) {
        return new Animation(this, repeatCount);
    }

    public double getAnimationPeriod() {
        return animationPeriod;
    }
    public double getActionPeriod(){return actionPeriod;}

    public void nextImage() {
        imageIndex = imageIndex + 1;
    }
    public String getId() {
        return id;
    }

    public Activity createActivityAction(WorldModel world, ImageStore imageStore) {
        return new Activity(this, world, imageStore);}
    public Point getPosition(){return position;}
}
