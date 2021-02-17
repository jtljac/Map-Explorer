import cymysql, os
from PIL import Image

MAX_SIZE = (1000, 1000)


def makeThumbnail(cursor, image):
    with Image.open(mapsDir + image[1]) as theImage:
        theImage.thumbnail(MAX_SIZE)
        filename, extension = os.path.splitext(image[1])
        dest = open(mapsDir + filename + "thumbnail" + extension, "wb")
        theImage.save(dest)
        dest.close()
        cursor.execute("UPDATE maps SET thumbnail=1 WHERE id=%s", image[0])


mapsDir = input("Enter map Directory: ")

if mapsDir[-1] != "/":
    mapsDir += "/"

conn = cymysql.connect(host="localhost", user=input("Enter DB Username: "), passwd=input("Enter DB Password: "), db=input("Enter DB database: "))
cur = conn.cursor()

cur.execute('SELECT id, filePath FROM maps')
temp = cur.fetchall()

count = 0
for image in temp:
    count = (count + 1) % 10
    makeThumbnail(cur, image)
    if count == 0:
        conn.commit()
conn.commit()
cur.close()
conn.close()
