public interface HasActivity extends Entity {
    Activity createActivityAction(WorldModel world, ImageStore imageStore);
    void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
}
