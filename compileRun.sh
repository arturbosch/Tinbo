#!/bin/bash
gradle build
java -jar -Djava.awt.headless=false build/libs/Tinbo-1.0.RC1.jar
