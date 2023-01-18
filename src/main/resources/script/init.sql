DROP TABLE IF EXISTS `eventjob`;
CREATE TABLE `eventjob`
(
    `id`              INT UNSIGNED AUTO_INCREMENT,
    `params`          text NOT NULL,
    `created_at`      timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`      timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `deleted_at`      timestamp NULL,
    PRIMARY KEY (`id`),
) ENGINE = InnoDB default charset=utf8;