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
    - Browser with WebSocket support
    - Page with javascript to handle UI effects of gesture messages
- **Server side:**
    - GestureServer
        - Main server program, listens for WebSocket connections
        - Initializes the tracker
        - Detected gestures are sent to browsers connected via WebSocket
    - NiTETracker
        - Main tracker object, keeps track of hands and skeletons in the scene and listeners can attach to it
        - Manages the sensor and attempts to detect and handle USB disconnects
    - Gesture
        - Defines a gesture using a user-defined Detector. A gesture is defined to be great enough movement of the hand in the same general direction for a period of frames
        - In the GestureServer, a DirectionDetector is used for frame comparison, allowing for sensitivity adjustable left/right/up/down/in/out gestures
    - DirectionDetector
        - Uses NiTETracker to supply hand tracker frames and compares the relative movement of two consecutive frames to determine which direction the hand is currently moving

