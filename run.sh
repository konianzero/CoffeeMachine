#!/bin/bash

# Use when Java 17 is not default, replace with your Java 17 directory
export JAVA_HOME="/opt/java/amazon_jdk/amazon-corretto-17.0.1.12.1-linux-x64"
echo JAVA_HOME=\"$JAVA_HOME\"

# Run App
./mvnw clean
./mvnw jaxws:wsimport
./mvnw spring-boot:run
#./mvnw spring-boot:run -Dspring-boot.run.profiles=prod