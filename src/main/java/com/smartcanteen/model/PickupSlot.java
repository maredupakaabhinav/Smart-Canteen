package com.smartcanteen.model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A pickup time the canteen offers (every 30 minutes from 10:30 AM to 2:00 PM).
 */
public class PickupSlot {

    private static final DateTimeFormatter LABEL_FORMAT = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);
    private static final DateTimeFormatter VALUE_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    private final LocalTime time;

    public PickupSlot(LocalTime time) {
        this.time = time;
    }

    public LocalTime getTime() {
        return time;
    }

    /** Text shown to the user, e.g. 12:30 PM */
    public String getLabel() {
        return time.format(LABEL_FORMAT);
    }

    /** Value sent by the form, e.g. 12:30 */
    public String getValue() {
        return time.format(VALUE_FORMAT);
    }

    /** Minutes since midnight - used by JavaScript to disable slots that have already passed. */
    public int getMinutesOfDay() {
        return time.getHour() * 60 + time.getMinute();
    }

    public static List<PickupSlot> getAllSlots() {
        List<PickupSlot> slots = new ArrayList<>();
        LocalTime time = LocalTime.of(10, 30);
        while (!time.isAfter(LocalTime.of(14, 0))) {
            slots.add(new PickupSlot(time));
            time = time.plusMinutes(30);
        }
        return slots;
    }

    /** Finds the slot for a form value like "12:30" (returns null if there is no such slot). */
    public static PickupSlot findByValue(String value) {
        for (PickupSlot slot : getAllSlots()) {
            if (slot.getValue().equals(value)) {
                return slot;
            }
        }
        return null;
    }
}
