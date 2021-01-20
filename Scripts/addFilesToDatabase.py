import cymysql, os, re
import hashlib, io
from PIL import Image

conn = cymysql.connect(host="localhost", user=input("Enter DB Username: "), passwd=input("Enter DB Password: "), db=input("Enter DB database: "))
cur = conn.cursor()
existing = []

reString = "(.*?)(([0-9]+)x([0-9]+))?\.(png|jpg)"


def pushImage(directory, width, height, xSquares, ySquares, md5):
    sql = "INSERT INTO maps (filePath, width, height, squareWidth, squareHeight, uploader, imageHash) VALUES(%s, %s, %s, %s, %s, %s, %s)"
    cur.execute(sql, [directory, width, height, xSquares, ySquares, "initial", md5])


def parseDir(directory):
    for file in os.scandir(directory):
        if file.is_file():
            path = file.path.replace("\\", "/")[len(startingDirectory):]
            if path not in existing:
                reResult = re.search(reString, file.name, re.IGNORECASE)
                if reResult:
                    im = Image.open(file.path)
                    width, height = im.size

                    m = hashlib.md5()
                    with open(file.path, "rb") as f:
                        for chunk in iter(lambda: f.read(4096), b""):
                            m.update(chunk)

                    pushImage(path, width, height, reResult.group(3), reResult.group(4), m.hexdigest())
        elif file.is_dir():
            parseDir(file.path)


cur.execute('SELECT filePath FROM maps')
temp = cur.fetchall()

for item in temp:
    existing.append(item[0])

startingDirectory = input("Enter Starting directory: ").replace("\\", "/")

if startingDirectory[-1] != "/":
    startingDirectory += "/"

parseDir(startingDirectory)
conn.commit()
cur.close()
conn.close()

