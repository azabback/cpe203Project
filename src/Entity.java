import processing.core.PImage;

public abstract class Entity
{
    private String id;

    abstract PImage getCurrentImage();
    abstract Point getPosition();
    abstract void nextImage();
    abstract int getAnimationPeriod();
    abstract void setPosition(Point p);

    public Entity(String id){
        this.id = id;
    }

    public String getID(){
        return this.id;
    }
}
