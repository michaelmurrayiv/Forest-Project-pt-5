public interface HasAnimation extends Entity {
    Animation createAnimationAction(int repeatCount);
    double getAnimationPeriod();
    void nextImage();
    void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);
}
