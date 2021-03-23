import cymysql

collectionID = 1

conn = cymysql.connect(host="192.168.0.40", user=input("Enter DB Username: "), passwd=input("Enter DB Password: "), db=input("Enter DB database: "))
cur = conn.cursor()

cur.execute("SELECT id FROM maps WHERE uploader='czepeku'")
result = [item[0] for item in cur.fetchall()]

data = list((x, collectionID) for x in result)

cur.executemany("INSERT INTO mapCollection VALUES (%s, %s)", data)

conn.commit()

cur.close()
conn.close()