package com.pe.advanced.domain.product;

public enum ProductStatus {

    DRAFT {
        @Override
        public boolean canTransitionTo(ProductStatus nextStatus) {
            // The draft can become active or archived if the project is canceled
            return nextStatus == ACTIVE || nextStatus == ARCHIVED;
        }
    },
    ACTIVE {
        @Override
        public boolean canTransitionTo(ProductStatus nextStatus) {
            // The active product can only be out of stock or archived
            return nextStatus == OUT_OF_STOCK || nextStatus == ARCHIVED;
        }
    },
    OUT_OF_STOCK {
        @Override
        public boolean canTransitionTo(ProductStatus nextStatus) {
            // The out-of-stock product can be again active (re-stocked) or archived
            return nextStatus == ACTIVE || nextStatus == ARCHIVED;
        }
    },
    ARCHIVED {
        @Override
        public boolean canTransitionTo(ProductStatus nextStatus) {
            // End transition, cannot go to any other
            return false;
        }
    };

    /**
     * Business rule: Defines whether the transition from the current state to the next state is allowed.
     */
    public abstract boolean canTransitionTo(ProductStatus nextStatus);
}
