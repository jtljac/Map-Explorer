import requests
from requests_toolbelt.multipart.encoder import MultipartEncoder

filePath = input("Enter File Path: ")

multipart_data = MultipartEncoder(
    fields=[
        ('image', ('test.jpg', open(filePath, 'rb'), 'image/jpg')),
        # plain text fields
        ('name', 'submitter'),
        ('squareWidth', 20),
        ('squareHeight', 25),
        ('tags', "test"),
        ('tags', 'test3')
    ]
)

response = requests.post('http://localhost:8080/imageUpload', data=multipart_data,
                  headers={'Content-Type': multipart_data.content_type})

print(str(response) + "\n" + str(response.content))