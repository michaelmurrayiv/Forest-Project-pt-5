import java.util.Objects;

public class AStarNode {
    private Point pos;
    private double f;
    private double g;
    private double h;
    private AStarNode prev;

    public AStarNode(Point currPos, Point target) {
        pos = currPos;
        g = 0;
        h = distance(currPos, target);
        f = g + h;
        prev = null;
    }

    public AStarNode(Point currPos, Point target, AStarNode prevNode) {
        pos = currPos;
        g = prevNode.g + 1;
        h = distance(currPos, target);
        f = g + h;
        prev = prevNode;
    }

    private double distance(Point p1, Point p2) {
        double deltaX = p1.x - p2.x;
        double deltaY = p1.y - p2.y;

        double distanceSquared = deltaX * deltaX + deltaY * deltaY;

        return Math.sqrt(distanceSquared);
    }

    public double getF() {
        return f;
    }

    public Point getPos() {
        return pos;
    }

    public void setPrev(AStarNode newPrev) {
        prev = newPrev;
    }
    public AStarNode getPrev(){return prev;}
    public double getG(){return g;}

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {return false;}
        if (getClass() != obj.getClass()) {return false;}
        AStarNode a = (AStarNode) obj;
        return pos.equals(a.pos);
    }


    @Override
    public int hashCode() {
        return Objects.hash(pos);
    }
}