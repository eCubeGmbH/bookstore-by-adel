DROP TABLE if exists book;
DROP TABLE if exists author;


CREATE TABLE author
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    name          VARCHAR(100) COLLATE utf8mb4_bin    NOT NULL,
    country       VARCHAR(100) COLLATE utf8mb4_bin    NOT NULL,
    birth_date    DATE                                NOT NULL,
    date_created  TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE book
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    author_id    BIGINT                           NOT NULL,
    name         VARCHAR(100) COLLATE utf8mb4_bin NOT NULL,
    publish_date DATE                             NOT NULL,
    date_created  TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    date_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (id),
    index AuthorId (author_id),
    FOREIGN KEY (author_id) REFERENCES author(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
