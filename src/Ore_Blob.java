import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Ore_Blob implements Entity, ActiveEntity, MovingEntity{

    public EntityKind kind;
    public String id;
    private Point position;
    public List<PImage> images;
    public int imageIndex;
    public int actionPeriod;
    public int animationPeriod;

    private static final String QUAKE_ID = "quake";
    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;

    private static final String BLOB_KEY = "blob";
    private static final String BLOB_ID_SUFFIX = " -- blob";
    private static final int BLOB_PERIOD_SCALE = 4;
    private static final int BLOB_ANIMATION_MIN = 50;
    private static final int BLOB_ANIMATION_MAX = 150;

    private static final String ORE_ID_PREFIX = "ore -- ";
    private static final int ORE_CORRUPT_MIN = 20000;
    private static final int ORE_CORRUPT_MAX = 30000;

    private static final Random rand = new Random();

    private static final String QUAKE_KEY = "quake";
    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

    private static final String ORE_KEY = "ore";

    public Ore_Blob(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod,
            int animationPeriod)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public PImage getCurrentImage() {
        return this.images.get(this.imageIndex);
    }

    public int getAnimationPeriod() { return this.animationPeriod; }

    public void nextImage() {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }

    public Point getPosition(){ return this.position; }

    public EntityKind getKind(){ return EntityKind.ORE_BLOB; }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> blobTarget =
                world.findNearest(this.position, EntityKind.VEIN);
        long nextPeriod = this.actionPeriod;

        if (blobTarget.isPresent()) {
            Point tgtPos = blobTarget.get().getPosition();

            if (this.moveToOreBlob(world, blobTarget.get(), scheduler)) {
                Quake quake = Create.createQuake(tgtPos,
                        imageStore.getImageList(QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += this.actionPeriod;
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent( this,
                Create.createActivityAction(this, world, imageStore),
                nextPeriod);
    }



    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                Create.createActivityAction(this, world, imageStore),
                this.actionPeriod);
        scheduler.scheduleEvent(this,
                Create.createAnimationAction(this, 0),
                this.getAnimationPeriod());
    }

    public boolean moveToOreBlob(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (this.position.adjacent(target.getPosition())) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else {
            Point nextPos = this.nextPosition(world, target.getPosition());

            if (!this.position.equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }


    public Point nextPosition(WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz, this.position.y);

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 || (occupant.isPresent() && !(occupant.get().getKind()
                == EntityKind.ORE)))
        {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x, this.position.y + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 || (occupant.isPresent() && !(occupant.get().getKind()
                    == EntityKind.ORE)))
            {
                newPos = this.position;
            }
        }

        return newPos;
    }
}
