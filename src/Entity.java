import processing.core.PImage;

import java.util.List;

public abstract class Entity
{
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;

    public Entity(String id, Point position, List<PImage> images){
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }

    protected String getID(){
        return this.id;
    }

    protected Point getPosition(){
        return this.position;
    }

    protected void setPosition(Point p) {
        this.position = p;
    }

    protected PImage getCurrentImage() {
        return this.images.get(this.imageIndex);
    }

    protected List<PImage> getImages(){
        return this.images;
    }

    protected void nextImage() {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }
}
