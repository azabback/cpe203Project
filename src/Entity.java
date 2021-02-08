import processing.core.PImage;

public interface Entity
{
    PImage getCurrentImage();
    Point getPosition();
    EntityKind getKind();
    void nextImage();
    int getAnimationPeriod();
    void setPosition(Point p);
}
