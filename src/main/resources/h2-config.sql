CREATE TABLE TWEETS (
    TWID INT AUTO_INCREMENT PRIMARY KEY,
    TWAUTHOR VARCHAR(80),
    TWTEXT VARCHAR(800) NOT NULL,
    TWLIKES INT DEFAULT 0,
    TWTIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP()
);

INSERT INTO TWEETS(TWAUTHOR, TWTEXT, TWLIKES, TWTIME) VALUES
('Sherlock', 'A cynic is a man who, when he smells flowers, looks around for a coffin.', 11, TIMESTAMP '2023-08-23 14:56:11.0'),
('Mycroft', 'No married man is genuinely happy if he has to drink worse whisky than he used to drink when he was single.', 2, TIMESTAMP '2023-08-24 17:23:45.0'),
('Sherlock', 'Before a man speaks it is always safe to assume that he is a fool. After he speaks, it is seldom necessary to assume it.', 15, TIMESTAMP '2023-08-24 19:07:11.0'),
('Mycroft', 'Adultery is the application of democracy to love.', 33, TIMESTAMP '2023-08-25 10:46:31.0'),
('Mycroft', 'A judge is a law student who marks his own examination papers.', 4, TIMESTAMP '2023-08-26 10:06:11.0'),
('Sherlock', 'For every complex problem there is an answer that is clear, simple, and wrong.', 8, TIMESTAMP '2023-08-26 11:16:01.0');
