import cymysql
import os
from PIL import Image
from jproperties import Properties

MAX_SIZE = (1000, 1000)


def makeThumbnail(cursor, mapsDir, image):
    with Image.open(mapsDir + image[1]) as theImage:
        theImage.thumbnail(MAX_SIZE)
        filename, extension = os.path.splitext(image[1])
        dest = open(mapsDir + filename + "thumbnail" + extension, "wb")
        theImage.save(dest)
        dest.close()
        cursor.execute("UPDATE maps SET thumbnail=1 WHERE id=%s", image[0])


f = open("../mysql.properties", "r")
p = Properties.load(f)
f.close()

try:
    username = p["dbUsername"]
except KeyError:
    username = "mapWatcher"

try:
    password = p["dbPassword"]
except KeyError:
    password = "password"

try:
    address = p["address"]
except KeyError:
    address = "localhost"

try:
    port = p["port"]
except KeyError:
    port = "3306"

try:
    schema = p["schema"]
except KeyError:
    schema = "battlemap"

conn = cymysql.connect(host=address, user=username, passwd=password, db=schema)
cur = conn.cursor()
try:
    cur.execute('SELECT id, filePath FROM maps WHERE thumbnail=0')
    temp = cur.fetchall()

    print("Making thumbnails for " + str(len(temp)) + " images")

    count = 0
    for image in temp:
        count = (count + 1) % 10
        makeThumbnail(cur, "../maps/", image)
        if count == 0:
            conn.commit()
finally:
    conn.commit()
    cur.close()
    conn.close()
    print("Finished")
