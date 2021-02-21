public class Activity implements Action {

    public ActiveEntity entity;
    public WorldModel world;
    public ImageStore imageStore;
    public int repeatCount;

    public Activity(
            ActiveEntity entity,
            WorldModel world,
            ImageStore imageStore)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = 0;
    }

    public void executeAction(EventScheduler scheduler)
    {
        this.entity.executeActivity(this.world, this.imageStore, scheduler);
    }
}
