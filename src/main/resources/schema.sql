CREATE TABLE IF NOT EXISTS SAVEGAME(
                                       id uuid PRIMARY KEY,
                                       lastplayer VARCHAR(25),
    gameboard VARCHAR(255),
    size INT
    );