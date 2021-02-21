import processing.core.PImage;

import java.util.List;

public abstract class MinerEntity extends MovingEntity{

    private int resourceLimit;
    private int resourceCount;

    public MinerEntity(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod,
                       int resourceLimit, int resourceCount){
        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
    }

    public Point nextPosition(WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.x - this.getPosition().x);
        Point newPos = new Point(this.getPosition().x + horiz, this.getPosition().y);

        if (horiz == 0 || world.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.y - this.getPosition().y);
            newPos = new Point(this.getPosition().x, this.getPosition().y + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = this.getPosition();
            }
        }

        return newPos;
    }

    protected int getResourceLimit(){
        return this.resourceLimit;
    }

    protected int getResourceCount(){
        return this.resourceCount;
    }

    protected void incrementResourceCount(){
        this.resourceCount ++;
    }
}
