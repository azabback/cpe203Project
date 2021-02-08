import processing.core.PImage;

import java.util.List;

public class Quake implements Entity, ActiveEntity{


    public String id;
    private Point position;
    public List<PImage> images;
    public int imageIndex;
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
        this.id = QUAKE_ID;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = QUAKE_ACTION_PERIOD;
        this.animationPeriod = QUAKE_ANIMATION_PERIOD;
    }

    public PImage getCurrentImage() {
        return this.images.get(this.imageIndex);
    }

    public int getAnimationPeriod() { return this.animationPeriod; }

    public Point getPosition(){ return this.position; }

    public EntityKind getKind(){ return EntityKind.QUAKE; }

    public void setPosition(Point p) { this.position = p; }

    public void nextImage() {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }


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
