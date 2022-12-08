import processing.core.PImage;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Bomb extends Person {
    //TODO: two blocks up, down, right, left, and one diagonal. Percent chance that a block gets hit. If it hits water, turn to stone, else turn to fire.
    public Bomb(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
    }
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        List<Point> pointList = new ArrayList<>();
        int col = this.getPosition().x;
        int row = this.getPosition().y;
        for (int i = col - 1; i <= col + 1; i++) {
            for (int j = row - 2; j <= row + 2; j = j + 4) {
                pointList.add(new Point(i, j));
                System.out.println("(" + i + ", " + j + ")");
            }
        }
        for (int i = col - 2; i <= col + 2; i++) {
            for (int j = row - 1; j <= row + 1; j++) {
                pointList.add(new Point(i, j));
            }
        }

        pointList.stream()
                .filter(world::withinBounds)
                .filter(p ->!world.isOccupied(p))
                .filter(p ->Math.random() < .75)
                .forEach(p ->world.setBackgroundCell(p,new Background("stone",imageStore.getImageList("stone"))));

        world.removeEntity(this, scheduler);
}


    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        return false;
    }

    public Point nextPosition(WorldModel world, Point destPos) {
        return null;
    }
}
