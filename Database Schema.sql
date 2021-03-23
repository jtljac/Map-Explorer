DROP TABLE IF EXISTS duplicates;
DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS maps;

CREATE TABLE maps (
    id INT NOT NULL AUTO_INCREMENT,
    filePath VARCHAR(512) NOT NULL,
    thumbnail BOOLEAN NOT NULL DEFAULT 0,
    width INT NOT NULL,
    height INT NOT NULL,
    squareWidth INT DEFAULT NULL,
    squareHeight INT DEFAULT NULL,
    author VARCHAR(25) DEFAULT NULL,
    uploader VARCHAR(25) NOT NULL,
    uploadDate DATETIME NOT NULL DEFAULT current_timestamp(),
    imageHash VARCHAR(32) DEFAULT NULL,
    toReview BOOLEAN NOT NULL DEFAULT 1,
    PRIMARY KEY (id)
);

CREATE TABLE blacklist (
    imageHash VARCHAR(32) DEFAULT NULL,
    banDate DATETIME NOT NULL DEFAULT current_timestamp(),
    PRIMARY KEY (imageHash)
);

CREATE TABLE collection (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    creator VARCHAR(25) NOT NULL,
    description TEXT DEFAULT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE mapCollection (
    mapID INT NOT NULL,
    collectionID INT NOT NULL,
    PRIMARY KEY (mapID, collectionID),
    foreign key (mapID) REFERENCES maps (id) ON DELETE CASCADE ON UPDATE CASCADE,
    foreign key (collectionID) REFERENCES collection (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tags (
    mapID INT NOT NULL,
    tag varchar(30) NOT NULL,
    PRIMARY KEY (mapID,tag),
    FOREIGN KEY (mapID) REFERENCES maps (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE duplicates (
    map1 INT,
    map2 INT,
    pct FLOAT,
    PRIMARY KEY (map1, map2),
    FOREIGN KEY (map1) REFERENCES maps (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (map2) REFERENCES maps (id) ON DELETE CASCADE ON UPDATE CASCADE
);