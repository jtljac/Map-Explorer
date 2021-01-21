#!/bin/sh

# Get old and new tag
oldVersion=$(git describe --tags "$(git rev-list --tags --max-count=1)")
git fetch --tags
newVersion=$(git describe --tags "$(git rev-list --tags --max-count=1)")

if [ "$oldVersion" != "$newVersion" ] || [ ! -f "MapExplorer.war" ]
then
        # Get Changes
        git checkout "$newVersion" -b master
        git pull

        chmod +x ./gradlew
fi

./gradlew bootRun