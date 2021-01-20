import cymysql, os
from PIL import Image

conn = cymysql.connect(host="localhost", user="mapWatcher", passwd="mapPassword1", db='battlemap')
cursor = conn.cursor()

sql = "SELECT id, filePath FROM `maps`"
cursor.execute(sql)
result = cursor.fetchall()
values = []
for theID, path in result:
    im = Image.open("G:\\D&D\\Maps\\"+path)
    im.show()
    theTags = input("Enter Tags: ").split(",")
    for tag in theTags:
        values.append((theID, tag.strip()))

sql = "INSERT INTO Tags VALUES(%s, %s)"
cursor.executemany(sql, values)

conn.commit()
cursor.close()
conn.close()