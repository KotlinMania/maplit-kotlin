#!/bin/bash
sed -i '' '1s/^/@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)\n\n/' src/commonMain/kotlin/io/github/kotlinmania/maplit/Lib.kt
sed -i '' 's/^fun /@kotlin.native.HiddenFromObjC\nfun /g' src/commonMain/kotlin/io/github/kotlinmania/maplit/Lib.kt
