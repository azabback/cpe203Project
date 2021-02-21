import processing.core.PImage;

import java.util.List;

public class Quake extends ActiveEntity{

    public int actionPeriod;
    public int animationPeriod;

    private static final String QUAKE_ID = "quake";
    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;
    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;


    public Quake(
            Point position,
            List<PImage> images)
    {
        super(QUAKE_ID, position, images);
        this.actionPeriod = QUAKE_ACTION_PERIOD;
        this.animationPeriod = QUAKE_ANIMATION_PERIOD;
    }

    public int getAnimationPeriod() { return this.animationPeriod; }


    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }


    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
                scheduler.scheduleEvent(this,
                        Create.createActivityAction(this, world, imageStore),
                        this.actionPeriod);
                scheduler.scheduleEvent(this, Create.createAnimationAction(this,
                        QUAKE_ANIMATION_REPEAT_COUNT),
                        this.getAnimationPeriod());
        }
}
