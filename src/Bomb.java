import processing.core.PImage;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Bomb extends Person {
//TODO: two blocks up, down, right, left, and one diagonal. Percent chance that a block gets hit. If it hits water, turn to stone, else turn to fire. 
    private int countdown;
    private List<Point> listOfPoints;
    public Bomb(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
        countdown = 13;
    }
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        countdown --;
        if (countdown == 0){
            world.removeEntity(this, scheduler);
            int col = this.getPosition().x;
            int row = this.getPosition().y;
            for (int i = col-1; i <= col+1; i++){
                for (int j = row-2; j <= row+2; j = j+4) {
                    Point p = new Point(i, j);
                    if (world.withinBounds(p) && !world.isOccupied(p)) {
                        world.setBackgroundCell(p, new Background("lava", imageStore.getImageList("lava")));
                    }
                }
            }

            for (int i = col-2; i <= col+2; i++){
                for (int j = row-1; j <= row+1; j++) {
                    Point p = new Point(i, j);
                    if (world.withinBounds(p) && !world.isOccupied(p)) {
                        world.setBackgroundCell(p, new Background("lava", imageStore.getImageList("lava")));
                    }
                }
            }
        }

        }


    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        return false;
    }

    public Point nextPosition(WorldModel world, Point destPos) {
        return null;
    }
}
