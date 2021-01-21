DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS maps;

CREATE TABLE `maps` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `filePath` varchar(512) DEFAULT NULL,
    `width` int(11) DEFAULT NULL,
    `height` int(11) DEFAULT NULL,
    `squareWidth` int(11) DEFAULT NULL,
    `squareHeight` int(11) DEFAULT NULL,
    `uploader` varchar(25) DEFAULT NULL,
    `uploadDate` datetime DEFAULT current_timestamp(),
    `imageHash` varchar(32) DEFAULT NULL,
    PRIMARY KEY (`id`)
);


CREATE TABLE `tags` (
    `mapID` int(11) NOT NULL,
    `tag` varchar(30) NOT NULL,
    PRIMARY KEY (`mapID`,`tag`),
    FOREIGN KEY (`mapID`) REFERENCES `maps` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
)
