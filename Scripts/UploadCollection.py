import requests, json
from requests_toolbelt.multipart.encoder import MultipartEncoder

def uploadFile(collectionID, map, basepath, uploader, author, tags):
    theFields = [
        ('image', (map["name"], open(filePath + basepath + map["path"], 'rb'), 'image/jpg')),
        # plain text fields
        ('name', uploader),
        ('author', author),
        ('squareWidth', str(map["width"])),
        ('squareHeight', str(map["height"])),
    ]

    for tag in tags:
        theFields.append(("tags", tag))
    for tag in map["extratags"]:
        theFields.append(("tags", tag))
    multipart_data = MultipartEncoder(fields=theFields)
    response = requests.post('http://' + address + '/api/images', data=multipart_data,
                             headers={'Content-Type': multipart_data.content_type})

    if response.status_code == 200:
        response = requests.post('http://' + address + '/api/collection/' + str(collectionID) + '/maps/' + str(int(response.content)))


def uploadCollection(collection):
    response = requests.post('http://' + address + '/api/collection',
                             data=json.dumps({
                                 "name": collection["name"],
                                 "creator": collection["uploader"],
                                 "description": collection["description"]
                             }),
                             headers={'Content-Type': "application/JSON"}
                             )
    if response.status_code == 200:
        for map in collection["maps"]:
            uploadFile(str(int(response.content)), map, collection["basepath"], collection["uploader"], collection["author"], collection["tags"])


filePath = input("Enter File Path: ")
address = input("Enter Map Server Address: ")

with open(filePath + "maps.json", "r", encoding="utf8") as w:
    for collection in json.load(w):
        uploadCollection(collection)
