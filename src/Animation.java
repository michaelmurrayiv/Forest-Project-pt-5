public class Animation implements Action{
    private final Entity entity;
    private final int repeatCount;


    public Animation(Entity entity, int repeatCount){
        this.entity = entity;
        this.repeatCount = repeatCount;
    }
    public void executeAction(EventScheduler scheduler){
        ((HasAnimation)entity).nextImage();

        if (repeatCount != 1) {
            scheduler.scheduleEvent(entity, (Action) ((HasAnimation)entity).createAnimationAction(Math.max(repeatCount - 1, 0)), ((HasAnimation)entity).getAnimationPeriod());
        }

    }

}
