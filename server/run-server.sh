#!/bin/bash

# This is a simple wrapper script to run the server indefinitely - the JVM sometimes crashes.

# LIBPATH should point to a directory with the following files (unless they already reside in system library dirs):
# libMWClosestPoint.so  libNiTE2.jni.so  libNiTE2.so  libOniFile.so  libOpenNI2.jni.so  libOpenNI2.so  libPS1080.so  libPSLink.so

# Define your path here or pass it as a command line option
LIBPATH="$1"
CONFIGFILE="$(readlink -f server.conf)"


[ -z "${LIBPATH}" ] && echo -e "Pass library path as parameter or modify script first.\ne.g. ./run-server.sh /home/user/openni2-nite2/libs/" && exit 1 

cd Screenful-GestureServer

echo "Hold Ctrl-C to exit."
while true
	do java -Djava.library.path="${LIBPATH}" -XX:ErrorFile=/dev/null -jar dist/Screenful-GestureServer.jar "${CONFIGFILE}"
	   sleep 1
done

