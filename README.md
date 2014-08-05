### Screenful gesture project

Enables natural user interaction for the Screenful dashboard software. The user can change the active slide and get more detail by making simple hand gestures.

Implemented using OpenNI 2.2 and NiTE 2.2 for 64-bit Linux.

#### High-level overview

##### Hardware

- Xtion Pro depth sensor
- 64-bit Linux computer
    - There is no NiTE 2.x for the ARM architecture

##### Main software component breakdown

- **Client side:**
    - [Browser with WebSocket support](http://caniuse.com/websockets)
    - [Page with javascript](https://github.com/Screenful/screenful-gestures/blob/master/server/Screenful-GestureServer/html/screenful-ui-test.html#L93-L116) to handle UI effects of gesture messages
- **Server side:**
    - [run-server.sh](https://github.com/Screenful/screenful-gestures/blob/master/server/run-server.sh)
        - crude script to run and restart the GestureServer indefinitely if it dies
	- [server.conf](https://github.com/Screenful/screenful-gestures/blob/master/server/server.conf) contains server settings
    - [GestureServer](https://github.com/Screenful/screenful-gestures/blob/master/server/Screenful-GestureServer/src/screenful/server/GestureServer.java#L33-L63)
        - Main server program, listens for WebSocket connections
        - Initializes the tracker
        - Detected gestures are sent to browsers connected via WebSocket
    - [NiTETracker](https://github.com/Screenful/screenful-gestures/blob/master/server/Screenful-GestureServer/src/screenful/basic/NiTETracker.java)
        - Main tracker object, keeps track of hands and skeletons in the scene and listeners can attach to it
        - As a listener to NiTE user and hand tracker classes, receives hand and user tracker frames as they're processed by the library
        - Manages the sensor and attempts to detect and handle USB disconnects
    - [Gesture](https://github.com/Screenful/screenful-gestures/blob/master/server/Screenful-GestureServer/src/screenful/gestures/Gesture.java)
        - Defines a gesture using a user-defined Detector. A gesture is defined to be great enough movement of the hand that satisfies the condition checked by the Detector for a period of frames
        - In the GestureServer, a DirectionDetector is used for frame comparison, allowing for sensitivity adjustable left/right/up/down/in/out gestures
    - [DirectionDetector](https://github.com/Screenful/screenful-gestures/blob/master/server/Screenful-GestureServer/src/screenful/gestures/detectors/DirectionDetector.java)
        - Uses NiTETracker to supply hand tracker frames and compares the relative movement of two consecutive frames to determine which direction the hand is currently moving

