import processing.core.PImage;

import java.util.List;

public class Obstacle implements HasAnimation {
    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;
    private final double animationPeriod;


    public Obstacle(String id, Point position, List<PImage> images, int imageIndex, double animationPeriod){
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = imageIndex;
        this.animationPeriod = animationPeriod;
    }
    public List<PImage> getImages() {return images; }

    public int getImageIndex(){return imageIndex;}

    public void setPosition(Point position) { this.position = position;}
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this, createAnimationAction(0), getAnimationPeriod());
    }
    public Animation createAnimationAction(int repeatCount) {
        return new Animation(this, repeatCount);
    }
    public double getAnimationPeriod() {return animationPeriod;}
    public void nextImage() {
        imageIndex = imageIndex + 1;
    }
    public Point getPosition(){return position;}
    @Override
    public String getId() {
        return id;
    }

}
