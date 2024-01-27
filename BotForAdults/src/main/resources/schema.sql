create table IF NOT EXISTS pose
(
    pose_id         SERIAL PRIMARY KEY,
    pose_name       VARCHAR(255)  not null,
    DESCRIPTION     VARCHAR(1012) not null,
    pose_url        VARCHAR(1012) not null
    );