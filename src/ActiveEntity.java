import processing.core.PImage;

import java.util.List;

public abstract class ActiveEntity extends Entity{

    private int actionPeriod;

    abstract void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler);

    abstract void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore);

    public ActiveEntity(String id, Point position, List<PImage> images, int actionPeriod){
        super(id, position, images);
        this.actionPeriod = actionPeriod;
    }

    protected int getActionPeriod(){
        return this.actionPeriod;
    }

}
