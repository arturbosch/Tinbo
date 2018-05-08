#!/bin/bash
./gradlew build
java -jar -Djava.awt.headless=false tinbo-platform/build/libs/tinbo-platform-1.0.1.jar
