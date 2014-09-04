##### Websocket server protocol

The gesture server sends notifications of the detected motion events to the
browser(s) connected to it as configured in the configuration file.

### Event configuration

The file ```server.conf``` has three related lines:

- ```exitdirections=<direction,direction,... | none>```
    - When these directions are detected, a 'user-exit' event is sent
    - Example: ```exitdirections=out```
- ```enableddirections=<direction,direction,... | none>```
    - Specify which directions send events such as 'left' and 'down'
    - Example: ```enableddirections=left,right,down```
- ```startgestures=<wave | click | wave,click | none>```
    - Specify which gestures to use for starting hand tracking and send a 'hands-start' event
    - Choices are 'wave', 'click', a combination of these or 'none'
    - When hand tracking stops, either after 'user-exit' or when the hand is lost, a 'hands-stop' event is sent
