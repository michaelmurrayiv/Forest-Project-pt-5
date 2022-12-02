import processing.core.PImage;

import java.util.List;

public class House implements Entity{
    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;


    public House(String id, Point position, List<PImage> images, int imageIndex){
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = imageIndex;
    }
    public List<PImage> getImages() {return images; }

    public int getImageIndex(){return imageIndex;}

    public void setPosition(Point position) { this.position = position;}


    public Point getPosition(){return position;}
    @Override
    public String getId() {
        return id;
    }
}
