import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.random.*;

import processing.core.*;

public final class VirtualWorld extends PApplet {
    private static String[] ARGS;

    private static final int VIEW_WIDTH = 640;
    private static final int VIEW_HEIGHT = 480;
    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;

    private static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
    private static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;

    private static final String IMAGE_LIST_FILE_NAME = "imagelist";
    private static final String DEFAULT_IMAGE_NAME = "background_default";
    private static final int DEFAULT_IMAGE_COLOR = 0x808080;

    private static final String FAST_FLAG = "-fast";
    private static final String FASTER_FLAG = "-faster";
    private static final String FASTEST_FLAG = "-fastest";
    private static final double FAST_SCALE = 0.5;
    private static final double FASTER_SCALE = 0.25;
    private static final double FASTEST_SCALE = 0.10;

    private String loadFile = "world.sav";
    private long startTimeMillis = 0;
    private double timeScale = 1.0;

    private ImageStore imageStore;
    private WorldModel world;
    private WorldView view;
    private EventScheduler scheduler;

    private int timesPressed = 0;

    public void settings() {
        size(VIEW_WIDTH, VIEW_HEIGHT);
    }

    /*
       Processing entry point for "sketch" setup.
    */
    public void setup() {
        parseCommandLine(ARGS);
        loadImages(IMAGE_LIST_FILE_NAME);
        loadWorld(loadFile, this.imageStore);

        this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world, TILE_WIDTH, TILE_HEIGHT);
        this.scheduler = new EventScheduler();
        this.startTimeMillis = System.currentTimeMillis();
        this.scheduleActions(world, scheduler, imageStore);
    }

    public void draw() {
        double appTime = (System.currentTimeMillis() - startTimeMillis) * 0.001;
        double frameTime = (appTime - scheduler.getCurrentTime())/timeScale;
        this.update(frameTime);
        view.drawViewport();
    }

    public void update(double frameTime){
        scheduler.updateOnTime(frameTime);
    }

    // Just for debugging and for P5
    // Be sure to refactor this method as appropriate
    public void mousePressed() {
        Point pressed = mouseToPoint();
        System.out.println("CLICK! " + pressed.x + ", " + pressed.y);

        Optional<Entity> entityOptional = world.getOccupant(pressed);
        if (entityOptional.isPresent()) {
            Entity entity = entityOptional.get();
            System.out.println(entity.getId() + ": " + entity.getClass());
        }


        //world.setBackgroundCell(pressed, new Background("tombstone", imageStore.getImageList("tombstone")));
        if (timesPressed % 2 == 0)
        {

        int col = pressed.x;
        int row = pressed.y;

        List<Point> possiblePoints = new ArrayList<>();

        for (int i = col - 2; i <= col + 2; i++) {
            for (int j = row - 2; j <= row + 2; j++) {
                possiblePoints.add(new Point(i, j));
                }
            }

        possiblePoints.stream()
                .filter(p -> world.withinBounds(p))
                .filter(p -> !world.isOccupied(p))
                .filter(p -> Math.random() < .75)
                .forEach(p -> world.setBackgroundCell(p, new Background("lava", imageStore.getImageList("lava"))));
        
        Zombie zombie = new Zombie("zombie", new Point(pressed.x, pressed.y), imageStore.getImageList("zombie"), 1, .2);
        world.addEntity(zombie);
        ((HasAnimation)zombie).scheduleActions(scheduler, world, imageStore);

        List<Class> myList = new ArrayList<>();
        myList.add(Tree.class);
        Optional<Entity> nearestTree = world.findNearest(new Point(pressed.x, pressed.y), myList);
        Point fighterPos = nearestTree.get().getPosition();
        world.removeEntity(nearestTree.get(), scheduler);

        Fighter fighter = new Fighter("fighter", fighterPos, imageStore.getImageList("fighter"), .5, .2);
        world.addEntity(fighter);
        ((HasAnimation)fighter).scheduleActions(scheduler, world, imageStore);
        }
        else
        {
            Bomb bomb = new Bomb("bomb", new Point(pressed.x, pressed.y), imageStore.getImageList("bomb"),2.6, .2);
            world.addEntity(bomb);
            ((HasAnimation)bomb).scheduleActions(scheduler, world, imageStore);

        }
        timesPressed += 1;
    }

    public void scheduleActions(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        for (Entity entity : world.getEntities()) {
            if (!(entity instanceof Stump) && !(entity instanceof House)){
            ((HasAnimation)entity).scheduleActions(scheduler, world, imageStore);}
        }
    }

    private Point mouseToPoint() {
        return view.getViewport().viewportToWorld(mouseX / TILE_WIDTH, mouseY / TILE_HEIGHT);
    }

    public void keyPressed() {
        if (key == CODED) {
            int dx = 0;
            int dy = 0;

            switch (keyCode) {
                case UP -> dy -= 1;
                case DOWN -> dy += 1;
                case LEFT -> dx -= 1;
                case RIGHT -> dx += 1;
            }
            view.shiftView(dx, dy);
        }
    }

    public static Background createDefaultBackground(ImageStore imageStore) {
        return new Background(DEFAULT_IMAGE_NAME, imageStore.getImageList(DEFAULT_IMAGE_NAME));
    }

    public static PImage createImageColored(int width, int height, int color) {
        PImage img = new PImage(width, height, RGB);
        img.loadPixels();
        Arrays.fill(img.pixels, color);
        img.updatePixels();
        return img;
    }

    public void loadImages(String filename) {
        this.imageStore = new ImageStore(createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
        try {
            Scanner in = new Scanner(new File(filename));
            imageStore.loadImages(in,this);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public void loadWorld(String file, ImageStore imageStore) {
        this.world = new WorldModel();
        try {
            Scanner in = new Scanner(new File(file));
            world.load(in, imageStore, createDefaultBackground(imageStore));
        } catch (FileNotFoundException e) {
            Scanner in = new Scanner(file);
            world.load(in, imageStore, createDefaultBackground(imageStore));
        }
    }

    public void parseCommandLine(String[] args) {
        for (String arg : args) {
            switch (arg) {
                case FAST_FLAG -> timeScale = Math.min(FAST_SCALE, timeScale);
                case FASTER_FLAG -> timeScale = Math.min(FASTER_SCALE, timeScale);
                case FASTEST_FLAG -> timeScale = Math.min(FASTEST_SCALE, timeScale);
                default -> loadFile = arg;
            }
        }
    }

    public static void main(String[] args) {
        VirtualWorld.ARGS = args;
        PApplet.main(VirtualWorld.class);
    }

    public static List<String> headlessMain(String[] args, double lifetime){
        VirtualWorld.ARGS = args;

        VirtualWorld virtualWorld = new VirtualWorld();
        virtualWorld.setup();
        virtualWorld.update(lifetime);

        return virtualWorld.world.log();

    }

}
