#!/bin/bash
echo "Building projects and plugins"
./gradlew build
echo "Deleting old plugins"
rm -R ~/Tinbo/plugins/
mkdir -p ~/Tinbo/plugins/
find $PWD/tinbo-*/build/libs/ -type f | awk '!/tinbo-plugin-api/ && !/tinbo-platform/' | while read file ; do
cp ${file} ~/Tinbo/plugins/ ; done
echo "Copied plugins into tinbo directory."
