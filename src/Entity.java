import processing.core.PImage;

public abstract class Entity
{
    private String id;
    private Point position;

    abstract PImage getCurrentImage();
    abstract void nextImage();
    abstract int getAnimationPeriod();

    public Entity(String id, Point position){
        this.id = id;
        this.position = position;
    }

    public String getID(){
        return this.id;
    }

    public Point getPosition(){
        return this.position;
    }

    public void setPosition(Point p) {
        this.position = p;
    }
}
