import processing.core.PImage;

import java.util.List;
import java.util.Random;

public class Ore extends ActiveEntity{

    public int actionPeriod;
    public int animationPeriod;

    private static final String BLOB_KEY = "blob";
    private static final String BLOB_ID_SUFFIX = " -- blob";
    private static final int BLOB_PERIOD_SCALE = 4;
    private static final int BLOB_ANIMATION_MIN = 50;
    private static final int BLOB_ANIMATION_MAX = 150;
    private static final Random rand = new Random();

    public Ore(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod)
    {
        super(id, position, images);
        this.actionPeriod = actionPeriod;
    }


    public int getAnimationPeriod() { return this.animationPeriod; }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Point pos = this.getPosition();

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        ActiveEntity blob = Create.createOreBlob(this.getID() + BLOB_ID_SUFFIX, pos,
                this.actionPeriod / BLOB_PERIOD_SCALE,
                BLOB_ANIMATION_MIN + rand.nextInt(
                        BLOB_ANIMATION_MAX
                                - BLOB_ANIMATION_MIN),
                imageStore.getImageList(BLOB_KEY));

        world.addEntity(blob);
        blob.scheduleActions(scheduler, world, imageStore);
    }


    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
        scheduler.scheduleEvent(this, Create.createActivityAction(this, world, imageStore),
            this.actionPeriod);
    }
}
