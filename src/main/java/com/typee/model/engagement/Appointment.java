package com.typee.model.engagement;

import java.time.LocalDateTime;

/**
 * Represents an {@code Appointment}.
 */
public class Appointment extends Engagement {
    protected Appointment(LocalDateTime start, LocalDateTime end,
                      AttendeeList attendees, Location location, String description, Priority priority) {
        super(start, end, attendees, location, description, priority);
        this.startTime = start;
        this.endTime = end;
        this.attendees = attendees;
        this.location = location;
        this.description = description;
        this.priority = priority;
    }

    @Override
    public EngagementType getType() {
        return EngagementType.APPOINTMENT;
    }

    @Override
    public String toString() {
        return String.format("Appointment of %s priority from %s to %s at %s.", priority.toString(),
                startTime.toString(), endTime.toString(), location.toString());
    }
}
