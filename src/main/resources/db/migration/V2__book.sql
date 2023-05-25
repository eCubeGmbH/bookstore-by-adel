CREATE TABLE `book`
(
    `id`           bigint                           NOT NULL AUTO_INCREMENT,
    `author_id`    varchar(36) COLLATE utf8mb4_bin  NOT NULL,
    `name`         varchar(100) COLLATE utf8mb4_bin NOT NULL,
    `publish_date` date                             NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;