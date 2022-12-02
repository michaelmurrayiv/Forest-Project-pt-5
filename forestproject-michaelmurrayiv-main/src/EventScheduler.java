import java.util.*;

/**
 * Keeps track of events that have been scheduled.
 */
public final class EventScheduler {
    private final PriorityQueue<Event> eventQueue;
    private final Map<Entity, List<Event>> pendingEvents;
    private double currentTime;

    public EventScheduler() {
        this.eventQueue = new PriorityQueue<>(new EventComparator());
        this.pendingEvents = new HashMap<>();
        this.currentTime = 0;
    }
    public void unscheduleAllEvents(Entity entity) {
        List<Event> pending = pendingEvents.remove(entity);

        if (pending != null) {
            for (Event event : pending) {
                eventQueue.remove(event);
            }
        }
    }
    public void scheduleEvent(Entity entity, Action action, double afterPeriod) {
        double time = getCurrentTime() + afterPeriod;

        Event event = new Event(action, time, entity);

        eventQueue.add(event);

        // update list of pending events for the given entity
        List<Event> pending = pendingEvents.getOrDefault(entity, new LinkedList<>());
        pending.add(event);
        pendingEvents.put(entity, pending);
    }

    private void removePendingEvent(Event event) {
        List<Event> pending = pendingEvents.get(event.getEntity());

        if (pending != null) {
            pending.remove(event);
        }
    }
    public void updateOnTime(double time) {
        double stopTime = getCurrentTime() + time;
        while (!eventQueue.isEmpty() && eventQueue.peek().getTime() <= stopTime) {
            Event next = eventQueue.poll();
            removePendingEvent(next);
            currentTime = next.getTime();
            next.getAction().executeAction(this);
        }
        currentTime = stopTime;
    }

    public double getCurrentTime() {
        return currentTime;
    }
}
