import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public final class Entity
{
    public EntityKind kind;
    public String id;
    public Point position;
    public List<PImage> images;
    public int imageIndex;
    public int resourceLimit;
    public int resourceCount;
    public int actionPeriod;
    public int animationPeriod;

    public Entity(
            EntityKind kind,
            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int resourceCount,
            int actionPeriod,
            int animationPeriod)
    {
        this.kind = kind;
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public PImage getCurrentImage() {
        return this.images.get(this.imageIndex);
    }

    public int getAnimationPeriod() {
        switch (this.kind) {
            case MINER_FULL:
            case MINER_NOT_FULL:
            case ORE_BLOB:
            case QUAKE:
                return this.animationPeriod;
            default:
                throw new UnsupportedOperationException(
                        String.format("getAnimationPeriod not supported for %s",
                                this.kind));
        }
    }

    public void nextImage() {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }

    public void executeMinerFullActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> fullTarget =
                Functions.findNearest(world, this.position, EntityKind.BLACKSMITH);

        if (fullTarget.isPresent() && Functions.moveToFull(this, world,
                fullTarget.get(), scheduler))
        {
            Functions.transformFull(this, world, scheduler, imageStore);
        }
        else {
            Functions.scheduleEvent(scheduler, this,
                    Functions.createActivityAction(this, world, imageStore),
                    this.actionPeriod);
        }
    }

    public void executeMinerNotFullActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget =
                Functions.findNearest(world, this.position, EntityKind.ORE);

        if (!notFullTarget.isPresent() || !Functions.moveToNotFull(this, world,
                notFullTarget.get(),
                scheduler)
                || !Functions.transformNotFull(this, world, scheduler, imageStore))
        {
            Functions.scheduleEvent(scheduler, this,
                    Functions.createActivityAction(this, world, imageStore),
                    this.actionPeriod);
        }
    }

    public void executeOreActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Point pos = this.position;

        Functions.removeEntity(world, this);
        Functions.unscheduleAllEvents(scheduler, this);

        Entity blob = Functions.createOreBlob(this.id + Functions.BLOB_ID_SUFFIX, pos,
                this.actionPeriod / Functions.BLOB_PERIOD_SCALE,
                Functions.BLOB_ANIMATION_MIN + Functions.rand.nextInt(
                        Functions.BLOB_ANIMATION_MAX
                                - Functions.BLOB_ANIMATION_MIN),
                Functions.getImageList(imageStore, Functions.BLOB_KEY));

        Functions.addEntity(world, blob);
        Functions.scheduleActions(blob, scheduler, world, imageStore);
    }
}
