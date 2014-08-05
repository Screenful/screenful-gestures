### Screenful gesture UI WebSocket server for NiTE 2.2

**(Note: the data files for NiTE are not included, you must get them elsewhere. Why? NiTE's license.)**

#### Requirements for usage

- 64-bit OpenNI 2.2 and NiTE 2.2 must be installed
    - see [tools/kinect-ubuntu-install](https://github.com/Screenful/screenful-gestures/tree/master/tools/kinect-ubuntu-install)
- Copy NiTE2 data files (`h.dat`, `s.dat` etc.) into `server/Screenful-GestureServer/NiTE2/`
- Edit `server.conf` and Run `./run-server.sh <library dir with OpenNI and NiTE .so files>`
    - If OpenNI and NiTE libraries have been installed system-wide, you can probably remove `-Djava.library.path="${LIBPATH}"` from the command line.
- Open a web page in a browser that establishes a WebSocket connection to the server (port 8887 by default), for a sample page see [screenful-ui-test.html](https://github.com/Screenful/screenful-gestures/tree/master/server/Screenful-GestureServer/html/screenful-ui-test.html).
- Connect the Xtion if it's not already connected and wave your hand to the sensor to start tracking the hand and pass commands to the browser.
    - _Note that while installing the libraries above should add support for the Kinect, for some reason NiTE hand tracker doesn't seem to work with it. The skeleton tracking works fine. For this reason the software in its current state **is not usable with the Kinect**._

#### Using in a NetBeans project

**(TODO)**

1. Create a new Java project
2. Add org.openni.jar, com.primesense.nite.jar, java_websocket.jar to project libraries
3. (If needed) Adjust project properties, Run section: add VM parameter: `-Djava.library.path=<absolute path of the directory with all the .so files>`
4. Copy `<NiTE2 extraction directory>/Redist/NiTE2` directory inside the project root directory (`Screenful-GestureServer/`) to get required data files
5. See and modify [GestureServer.java](https://github.com/Screenful/screenful-gestures/blob/master/server/Screenful-GestureServer/src/screenful/server/GestureServer.java).
