import json, cymysql

file = open("results.json", "r")
data = json.load(file)
file.close()

conn = cymysql.connect(host="192.168.0.40", user=input("Enter DB Username: "), passwd=input("Enter DB Password: "), db=input("Enter DB database: "))
cur = conn.cursor()


def pushDupe(id1, id2, pct):
    sql = "INSERT INTO duplicates (map1, map2, pct) VALUES(%s, %s, %s)"
    cur.execute(sql, [id1, id2, pct])

for item in data:
    pushDupe(item, data[item][0], data[item][1])
conn.commit()
cur.close()

