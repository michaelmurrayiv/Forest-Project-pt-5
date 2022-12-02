import processing.core.PImage;

import java.util.List;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public interface Entity {
    default PImage getCurrentImage() {
        return getImages().get(getImageIndex() % getImages().size());
    }
    default void tryAddEntity(WorldModel world) {
        if (world.isOccupied(getPosition())) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }
        world.addEntity(this);
    }
    default String log(){
        return this.getId().isEmpty() ? null :
                String.format("%s %d %d %d", this.getId(), this.getPosition().x, this.getPosition().y, this.getImageIndex());
    }
    void setPosition(Point position);

    Point getPosition();
    String getId();
    int getImageIndex();
    List<PImage> getImages();


}