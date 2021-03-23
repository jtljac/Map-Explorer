# Map Explorer
Map Explorer is a Spring boot webserver for sorting your collection of battlemaps. The project consists of a web front end, for uploading, searching for, and viewing maps, and a back end REST api for doing the same.

# Setup Instructions
1. Clone the project
2. Setup a mariadb database as specified [here](https://github.com/jtljac/Map-Explorer/wiki/Setting-up-the-database)
3. Create a mysql.properties file as specified [here](https://github.com/jtljac/Map-Explorer/wiki/mysql.properties)
4. Run the project from source with
    - Linux: `./gradlew bootRun`
    - Windows `gradlew.bat bootRun`
  
OR

4. Compile the project to a .war with
    - Linux: `./gradlew bootWar`
    - Windows `gradlew.bat bootWar`
    
  then run the resulting file in `./build/libs/`
