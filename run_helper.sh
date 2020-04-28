#!/bin/bash

echo "Creating database"
createdb -U postgres -h localhost casino

echo "Building application"
./gradlew clean build

echo "run the server"
java -Dspring.config.location=src/main/resources/application.properties  -jar build/libs/casino-0.0.1-SNAPSHOT.jar