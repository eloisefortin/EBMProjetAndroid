#! /bin/bash

cd ~/workspace/Android/Producthunt-Demo/app/src/main/java/fr/ec/producthunt
npx keppler "JAVA" --host 151.80.146.71 --port 80 &
cd ~/workspace/Android/Producthunt-Demo/app/src/main/res/
npx keppler "RES" --port 80 --host 151.80.146.71
