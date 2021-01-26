import json
from io import BytesIO
from multiprocessing import Pool
import requests
import os
from PIL import Image

cache = {}

def HistoCompare(im1, im2, mode="pct", alpha=.01):
    if im1.size == im2.size and im1.mode == im2.mode:
        h1 = im1.histogram()
        h2 = im2.histogram()
        SumIm1 = 0.0
        SumIm2 = 0.0
        diff = 0.0
        for i in range(len(h1)):
            SumIm1 += h1[i]
            SumIm2 += h2[i]
            diff += abs(h1[i] - h2[i])
        maxSum = max(SumIm1, SumIm2)
        if mode == "pct":
            return diff / (2 * maxSum)
        if diff > alpha * maxSum:
            return 1
        return 0
    return 1


def compareImages(id1, id2):
    global cache
    if id1 in cache:
        im1 = Image.open(open("./cache/" + cache[id1], "rb"))
    else:
        image1 = json.loads(requests.get("http://192.168.0.40/getImage?id=" + str(id1)).content)
        request = requests.get("http://192.168.0.40/maps/" + image1["filePath"]).content
        theBytes = BytesIO(request)
        im1 = Image.open(theBytes)
        with open("./cache/" + str(id1) + ".tmp", 'wb') as out:
            out.write(request)

    if id2 in cache:
        im2 = Image.open(open("./cache/" + cache[id2], "rb"))
    else:
        image2 = json.loads(requests.get("http://192.168.0.40/getImage?id=" + str(id2)).content)
        request = requests.get("http://192.168.0.40/maps/" + image2["filePath"]).content
        theBytes = BytesIO(request)
        im2 = Image.open(theBytes)
        with open("./cache/" + str(id2) + ".tmp", 'wb') as out:
            out.write(request)
            cache[id2] = str(id2) + ".tmp"
    return HistoCompare(im1, im2)


offset = 0
shouldContinue = True
ids = []
if not os.path.exists("./cache"):
    os.mkdir("./cache")
else:
    for file in os.scandir("./cache"):
        if file.is_file():
            name = file.name.split(".")
            cache[int(name[0])] = name[0] + ".tmp"


while shouldContinue:
    response = requests.get("http://192.168.0.40/getImages?numPerPage=30&offset=" + str(offset))
    if response.status_code == 200:
        for thing in json.loads(response.content):
            ids.append(thing["id"])
        offset += 30
    else:
        shouldContinue = False

matches = {}
for id1 in ids:
    for id2 in ids:
        if id1 != id2:
            pct = compareImages(id1, id2)
            if float(pct) < 0.1:
                print(str(id1) + " possibly matches with " + str(id2) + " by " + str(1 - pct))
                matches[id1] = (id2, 1 - pct,)

with open("results.json", "w") as f:
    json.dump(matches, f, indent=4)