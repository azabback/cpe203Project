import processing.core.PImage;

import java.util.List;

public class Obstacle implements Entity{
    public String id;
    private Point position;
    public List<PImage> images;
    public int imageIndex;

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

    public Point getPosition(){ return this.position; }

    public EntityKind getKind(){ return EntityKind.OBSTACLE; }

}
