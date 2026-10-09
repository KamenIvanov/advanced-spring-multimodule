CREATE TABLE `categories`
(
    `id`            BINARY(16)    NOT NULL,
    `created_at`    DATETIME(3)   NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `updated_at`    DATETIME(3)   NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    `created_by_id` BINARY(16)    NOT NULL,
    `updated_by_id` BINARY(16)    NOT NULL,
    `version`       INTEGER       NOT NULL DEFAULT 0,
    `name`          VARCHAR(255)  NOT NULL,
    `description`   VARCHAR(1000)          DEFAULT NULL,
    `status`        VARCHAR(32)   NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `uk_categories_name` UNIQUE (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;

CREATE TABLE `product_specifications`
(
    `id`            BINARY(16)  NOT NULL,
    `created_at`    DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `updated_at`    DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    `created_by_id` BINARY(16)  NOT NULL,
    `updated_by_id` BINARY(16)  NOT NULL,
    `version`       INTEGER     NOT NULL DEFAULT 0,
    `dimension`     VARCHAR(32) NOT NULL,
    `weight`        INTEGER     NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;

CREATE TABLE `products`
(
    `id`               BINARY(16)     NOT NULL,
    `created_at`       DATETIME(3)    NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `updated_at`       DATETIME(3)    NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    `created_by_id`    BINARY(16)     NOT NULL,
    `updated_by_id`    BINARY(16)     NOT NULL,
    `version`          INTEGER        NOT NULL DEFAULT 0,
    `name`             VARCHAR(255)   NOT NULL,
    `sku`              VARCHAR(64)    NOT NULL,
    `price`            DECIMAL(12, 2) NOT NULL,
    `status`           VARCHAR(32)    NOT NULL,
    `specification_id` BINARY(16)     NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `uk_products_sku` UNIQUE (`sku`),
    CONSTRAINT `uk_products_specification_id` UNIQUE (`specification_id`),
    CONSTRAINT `fk_products_specification` FOREIGN KEY (`specification_id`) REFERENCES `product_specifications` (`id`),
    INDEX `idx_products_created_at` (`created_at`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;

CREATE TABLE `category_assignments`
(
    `id`            BINARY(16)  NOT NULL,
    `created_at`    DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `created_by_id` BINARY(16)  NOT NULL,
    `category_id`   BINARY(16)  NOT NULL,
    `product_id`    BINARY(16)  NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `uk_assignments_category_product` UNIQUE (`category_id`, `product_id`),
    CONSTRAINT `fk_assignments_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`),
    CONSTRAINT `fk_assignments_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;