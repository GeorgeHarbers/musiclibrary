#get started
- have docker setup with the use of docker-compose
- navigate to the root folder of the project with your terminal 
- run "docker-compose up" . This will create a database container with the content described in  docker-compose.yaml
    (the database should be up now and is active on port 3308)
- run the Java application using the MusicLibraryApplication.class
_ the application will be initialized via Initializer.class and the REST client should be active now. 
- the client is on the default port: 8080
- api documentation can be found after launching the application at: 
    http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config
