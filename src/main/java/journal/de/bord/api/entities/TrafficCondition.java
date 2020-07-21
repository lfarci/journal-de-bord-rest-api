package journal.de.bord.api.entities;

/**
 * Represents the level of the traffic condition for a drive.
 */
public enum TrafficCondition {

    /**
     * Represents a really calm traffic condition. It means the driver is alone on the streets and
     * can drives at his own pace.
     */
    EXTREMELY_CALM,

    /**
     * Represents a calm traffic condition. It means the driver is almost alone on the streets and
     * can drives at his own pace.
     */
    CALM,

    /**
     * Represents a normal traffic condition. It means the driver experienced no pressure while
     * driving, he has to drive at the pace of the traffic.
     */
    NORMAL,

    /**
     * Represents a slow traffic condition.
     */
    SLOW,

    /**
     * Represents an extremely slow traffic condition.
     */
    EXTREMELY_SLOW;
}
