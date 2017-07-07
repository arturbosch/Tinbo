#!/bin/bash
gradle build
find $PWD/tinbo-*/build/libs/ -type f | awk '!/tinbo-plugin-api/ && !/tinbo-platform/' | while read file ; do
cp ${file} ~/Tinbo/plugins/ ; done
echo "Copied plugins into tinbo directory."
