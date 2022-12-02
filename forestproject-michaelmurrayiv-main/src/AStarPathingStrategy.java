import java.lang.reflect.Array;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy
{


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        AStarNode finalNode = null;
        List<Point> path = new LinkedList<>();
        PriorityQueue<AStarNode> openList = new PriorityQueue<>(Comparator.comparingDouble(AStarNode::getF));
        HashMap<Integer, AStarNode> openHash = new HashMap<>();
        HashMap<Integer, AStarNode> closedList = new HashMap<>();

//            Choose/know starting and ending points of the path
//            Add start node to the open list and mark it as the current node
        AStarNode startNode = new AStarNode(start, end);
        openList.add(startNode);
        openHash.put(startNode.hashCode(), startNode);
        boolean found = false;

        while (!openList.isEmpty() && !found) {
            AStarNode currNode = openList.remove();
            if (withinReach.test(currNode.getPos(), end)) {
                found = true;
                finalNode = currNode;
            } else {

//      Analyze all valid adjacent nodes that are not on the closed list.  For each valid neighbor:
                List<Point> validNeighbors = potentialNeighbors.apply(currNode.getPos())
                        .filter(canPassThrough)
                        .filter(p -> !p.equals(start) && !p.equals(end))
                        .collect(Collectors.toList());

                for (Point point : validNeighbors) {
                    AStarNode newNode = new AStarNode(point, end, currNode);
                    if (!closedList.containsValue(newNode)) {


//            If the node is already on the open list:
                        if (openHash.containsValue(newNode)) {

//      If the calculated g value is better than a previously calculated g value, replace the old g value
//      with the new one and proceed to c.  Otherwise, skip to step a for the next node.
                            if (newNode.getG() <= openHash.get(newNode.hashCode()).getG()){
                                openList.remove(newNode);
                                openList.add(newNode);
                            }

                        } else {
                            openList.add(newNode);
                            openHash.put(newNode.hashCode(), newNode);

                        }


                        //            Move the current node to the closed list
                        closedList.put(currNode.hashCode(), currNode);
                    }
                }
            }
        }
        while (finalNode != null && finalNode.getPrev() != null){
            path.add(0, finalNode.getPos());
            finalNode = finalNode.getPrev();
        }

/**
 *  The algorithm is just switching between the first and second grid locations (2,2) and (2,3)
 * **/
        return path;
    }
}
