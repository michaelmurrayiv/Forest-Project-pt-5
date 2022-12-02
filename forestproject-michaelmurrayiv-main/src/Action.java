public interface Action {
    /**
 * An action that can be taken by an entity
 */

    void executeAction(EventScheduler scheduler);

}

