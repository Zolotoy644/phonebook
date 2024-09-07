
insert into user_table (password, role, username) VALUES ('$2a$10$gyvd.Mx18xr///WC9tdqcOt/JiS5fWXMG5WGQejPWzlH6Qke/sGcq', 'ADMIN', 'ivan');
insert into user_table (password, role, username) VALUES ('$2a$10$gyvd.Mx18xr///WC9tdqcOt/JiS5fWXMG5WGQejPWzlH6Qke/sGcq', 'USER', 'kolya');
insert into contacts (user_id, email, firstname, lastname, phone, telegram)
VALUES (1, 'zzz@ya.ru', 'Ivan', 'Ivanov', '+79265553417', '@ivan');

insert into contacts (user_id, email, firstname, lastname, phone, telegram)
VALUES (1, 'qqq@ya.ru', 'Petr', 'Petrov', '+792655523115', '@petr');
