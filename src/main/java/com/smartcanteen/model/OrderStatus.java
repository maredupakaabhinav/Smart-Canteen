package com.smartcanteen.model;

/**
 * The life of an order. An enum is used so that only these five values are possible.
 * The "progress" number is used by the order tracker on the order details page.
 */
public enum OrderStatus {

    PLACED("Order Placed", 1),
    PREPARING("Preparing", 2),
    READY("Ready for Pickup", 3),
    COMPLETED("Completed", 4),
    CANCELLED("Cancelled", 0);

    private final String label;
    private final int progress;

    OrderStatus(String label, int progress) {
        this.label = label;
        this.progress = progress;
    }

    public String getLabel() {
        return label;
    }

    public int getProgress() {
        return progress;
    }

    /** COMPLETED and CANCELLED are the end of the road: the status can no longer change. */
    public boolean isFinished() {
        return this == COMPLETED || this == CANCELLED;
    }

    /** Safe conversion from text (returns null if the text is not a real status). */
    public static OrderStatus fromText(String text) {
        if (text == null) {
            return null;
        }
        for (OrderStatus status : values()) {
            if (status.name().equalsIgnoreCase(text.trim())) {
                return status;
            }
        }
        return null;
    }
}
