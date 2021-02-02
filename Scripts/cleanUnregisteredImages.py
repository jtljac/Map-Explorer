import cymysql, os


def parseDir(directory):
    for file in os.scandir(directory):
        if file.is_file():
            path = file.path.replace("\\", "/")[len(startingDirectory):]
            if path not in result:
                os.remove(file.path)

        elif file.is_dir():
            parseDir(file.path)


conn = cymysql.connect(host="localhost", user="mapWatcher", passwd="mapPassword1", db='battlemap')
cursor = conn.cursor()

sql = "SELECT filePath FROM `maps`"
cursor.execute(sql)
result = [item[0] for item in cursor.fetchall()]
conn.close()

startingDirectory = input("Enter Starting Directory: ").replace("\\", "/")
if startingDirectory[-1] != "/":
    startingDirectory += "/"
parseDir(startingDirectory)
