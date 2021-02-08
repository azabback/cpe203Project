public class Activity implements Action {

    public ActiveEntity entity;
    public WorldModel world;
    public ImageStore imageStore;
    public int repeatCount;

    public Activity(
            ActiveEntity entity,
            WorldModel world,
            ImageStore imageStore,
            int repeatCount)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler)
    {
        this.entity.executeActivity(this.world, this.imageStore, scheduler);
    }
}
