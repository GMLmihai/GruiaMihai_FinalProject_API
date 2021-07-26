#!/bin/bash

rm -r ./target/
./mvnw package

sudo docker build -t chatroom-gml-image .
sudo docker rm chatroom-gml-container
sudo docker run -p 8080:8080 --name chatroom-gml-container chatroom-gml-image