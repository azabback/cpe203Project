import processing.core.PImage;

import java.util.List;

public abstract class MovingEntity extends AnimatedEntity {

    public MovingEntity(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod){
        super(id, position, images, actionPeriod, animationPeriod);
    }

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                Create.createActivityAction(this, world, imageStore),
                this.getActionPeriod());
        scheduler.scheduleEvent(this,
                Create.createAnimationAction(this, 0),
                this.getAnimationPeriod());
    }

    abstract Point nextPosition(WorldModel world, Point destPos);

    abstract boolean moveTo(WorldModel world,
                            Entity target,
                            EventScheduler scheduler);
}
