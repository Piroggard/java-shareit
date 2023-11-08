CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) ,
    email VARCHAR(255)
);


CREATE TABLE IF NOT EXISTS requests
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(255),
    requester_id INT,
    created TIMESTAMP,
    CONSTRAINT requests_fr_user FOREIGN KEY (requester_id) REFERENCES users(id)
);




CREATE TABLE IF NOT EXISTS items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    available BOOL,
    owner INT,
    request_id INT
);

CREATE TABLE IF NOT EXISTS booking (
    id INT AUTO_INCREMENT PRIMARY KEY,
    start_of_booking TIMESTAMP,
    end_of_booking TIMESTAMP,
    item_id INT,
    booker_id INT,
    status VARCHAR(255),
    CONSTRAINT booking_fr_items FOREIGN KEY (item_id) REFERENCES items(id),
    CONSTRAINT booking_fr_user FOREIGN KEY (booker_id) REFERENCES users(id)
);


CREATE TABLE IF NOT EXISTS comment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    text VARCHAR(255) NOT NULL,
    item_id INT,
    author_id INT,
    created TIMESTAMP,
    CONSTRAINT comment_fr_items FOREIGN KEY (item_id) REFERENCES items(id),
    CONSTRAINT comment_fr_user FOREIGN KEY (author_id) REFERENCES users(id)
);