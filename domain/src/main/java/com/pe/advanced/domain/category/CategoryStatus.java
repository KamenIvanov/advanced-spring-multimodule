package com.pe.advanced.domain.category;

public enum CategoryStatus {

    INACTIVE {
        @Override
        public boolean canTransitionTo(CategoryStatus nextStatus) {
            // The inactive can become active or archived
            return nextStatus == ACTIVE || nextStatus == ARCHIVED;
        }
    },
    ACTIVE {
        @Override
        public boolean canTransitionTo(CategoryStatus nextStatus) {
            // The active category can only be archived
            return nextStatus == ARCHIVED;
        }
    },
    ARCHIVED {
        @Override
        public boolean canTransitionTo(CategoryStatus nextStatus) {
            // End transition, cannot go to any other
            return false;
        }
    };

    /**
     * Business rule: Defines whether the transition from the current state to the next state is allowed.
     */
    public abstract boolean canTransitionTo(CategoryStatus nextStatus);
}
