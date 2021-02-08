import processing.core.PImage;

public interface Entity
{
    PImage getCurrentImage();
    Point getPosition();
    void nextImage();
    int getAnimationPeriod();
    void setPosition(Point p);
}
