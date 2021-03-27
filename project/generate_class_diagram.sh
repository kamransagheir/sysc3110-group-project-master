#!/bin/sh
./gradlew archidoc
dot -Tpng build/architecture/classdiagram.dot -o build/architecture/classdiagram.png -Gdpi=300
open build/architecture/classdiagram.png
