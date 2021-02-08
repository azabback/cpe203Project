import processing.core.PImage;

import java.util.List;

public class Obstacle implements Entity{
    public String id;
    private Point position;
    public List<PImage> images;
    public int imageIndex;
    public int animationPeriod;

    public Obstacle(
            String id,
            Point position,
            List<PImage> images)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }

    public PImage getCurrentImage() {
        return this.images.get(this.imageIndex);
    }

    public void nextImage() {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }

    public Point getPosition(){ return this.position; }

    public EntityKind getKind(){ return EntityKind.OBSTACLE; }

    public int getAnimationPeriod() { return this.animationPeriod; }

}
