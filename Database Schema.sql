DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS maps;

CREATE TABLE maps (
  id int NOT NULL AUTO_INCREMENT,
  filePath varchar(512) DEFAULT NULL,
  width int DEFAULT NULL,
  height int DEFAULT NULL,
  squareWidth int DEFAULT NULL,
  squareHeight int DEFAULT NULL,
  uploader varchar(25) DEFAULT NULL,
  uploadDate datetime DEFAULT CURRENT_TIMESTAMP,
  imageHash varchar(32) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE tags (
	mapID INT,
    tag VARCHAR(30),
    PRIMARY KEY(mapID, tag),
    FOREIGN KEY(mapID) REFERENCES maps(id)
);