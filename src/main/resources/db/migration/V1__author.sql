CREATE TABLE `author`
(
    `id`         varchar(36) COLLATE ascii_bin    NOT NULL,
    `name`       varchar(100) COLLATE utf8mb4_bin NOT NULL,
    `country`    varchar(100) COLLATE utf8mb4_bin NOT NULL,
    `birth_date` date                             NOT null,
    primary key (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;