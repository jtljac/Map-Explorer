#!/bin/sh

# Get old and new tag
oldVersion=$(git describe --tags "$(git rev-list --tags --max-count=1)")
git fetch --tags
newVersion=$(git describe --tags "$(git rev-list --tags --max-count=1)")

if [ "$oldVersion" != "$newVersion" ]
then
        # Get Changes
        git checkout "$newVersion" -b master
        git reset --hard origin/master
        git clean -fd
        git pull

        chmod +x ./gradlew
fi

./gradlew bootRun