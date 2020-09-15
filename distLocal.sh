what=$1
test "$what" = "" && what="distLocal"

./gradlew --no-daemon $what "-Dhttp.nonProxyHosts=*.dev.flatex.com|localhost" --info
