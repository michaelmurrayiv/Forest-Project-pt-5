import processing.core.PImage;

import java.util.List;

public class Stump implements Entity{
    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;

    public Stump(String id, Point position, List<PImage> images, int imageIndex){
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = imageIndex;
    }


    public void setPosition(Point position) { this.position = position;}

    public int getImageIndex(){return imageIndex;}

    public Point getPosition(){return position;}
    public String getId(){return id;}

    public List<PImage> getImages() {return images; }
}
