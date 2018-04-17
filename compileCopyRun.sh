#!/bin/bash
sh copyPlugins.sh
java -jar -Djava.awt.headless=false tinbo-platform/build/libs/tinbo-platform-1.0.0.jar
