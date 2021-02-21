import processing.core.PImage;

import java.util.List;

public class Obstacle extends Entity{

    public int animationPeriod;

    public Obstacle(
            String id,
            Point position,
            List<PImage> images)
    {
        super(id, position, images);
    }

    public int getAnimationPeriod() { return this.animationPeriod; }

}
