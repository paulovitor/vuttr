#!/bin/sh

export SPRING_DATA_MONGODB_URI=mongodb://root:example@localhost:27017
export SPRING_DATA_MONGODB_DATABASE=vuttr
export SPRING_PROFILES_ACTIVE=default

echo "SPRING_DATA_MONGODB_URI: $SPRING_DATA_MONGODB_URI"
echo "SPRING_DATA_MONGODB_DATABASE: $SPRING_DATA_MONGODB_DATABASE"
echo "SPRING_PROFILES_ACTIVE: $SPRING_PROFILES_ACTIVE"