INSERT INTO roles (name) VALUES
    ('USER'),
    ('ADMIN');

INSERT INTO users (username, password) VALUES
    -- admin - admin
    ('admin', '$2a$12$sZdnAcBTOikdq6F5QA.uwut3aBhosoI6IX4hNgLzr7IzhepRzTysi'),
    -- user - user
    ('user', '$2a$12$CPAsrCxxVujfFF/srDsfBeeGR6xYcYVtNbwiTzuZBUy2o1E5eXoSe');

INSERT INTO users_roles(user_id, role_id) VALUES
    (1, 2),
    (2, 1);