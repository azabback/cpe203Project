public abstract class ActiveEntity extends Entity{

    abstract void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler);

    abstract void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore);

    public ActiveEntity(String id){
        super(id);
    }

}
