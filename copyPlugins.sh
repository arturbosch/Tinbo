#!/bin/bash
gradle build
cp $PWD/tinbo-finance/build/libs/tinbo-finance.jar ~/Tinbo/plugins/tinbo-finance.jar
cp $PWD/tinbo-time/build/libs/tinbo-time.jar ~/Tinbo/plugins/tinbo-time.jar
cp $PWD/tinbo-notes/build/libs/tinbo-notes.jar ~/Tinbo/plugins/tinbo-notes.jar
cp $PWD/tinbo-tasks/build/libs/tinbo-tasks.jar ~/Tinbo/plugins/tinbo-tasks.jar
cp $PWD/tinbo-projects/build/libs/tinbo-projects.jar ~/Tinbo/plugins/tinbo-projects.jar
cp $PWD/tinbo-ascii/build/libs/tinbo-ascii.jar ~/Tinbo/plugins/tinbo-ascii.jar
cp $PWD/tinbo-charts/build/libs/tinbo-charts.jar ~/Tinbo/plugins/tinbo-charts.jar
cp $PWD/tinbo-lloc/build/libs/tinbo-lloc.jar ~/Tinbo/plugins/tinbo-lloc.jar
