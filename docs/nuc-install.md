### Installing the gesture server in Intel NUC

**A preconfigured appliance image was created for this project, it is likely to become available at some point but is too big for pushing to this repository.**

#### Hardware notes

- **Loosen the four screws holding the bottom plate and lift the drive bay**
    - Note the orientation: there is a bar code sticker on the bottom and this should be positioned towards the Ethernet port, otherwise the plate may bend when it's put back together.

- **Insert RAM in memory slot**

- **Insert 2.5" SSD in disk slot (secure with included two tiny screws)**

- **Put the drive bay back and fasten screws**

- **Connect NUC to a display, turn it on and enter BIOS setup by pressing F2**
    - **Disable "Wake on USB" from Power options and save the settings**
        - Without this setting it seems the Xtion wakes up the NUC from a powered down state.

- **You may want to update the BIOS**
    - You can use the option within BIOS (didn't work for me, it complained about not being able to read some product ID) or by copying the BIOS update from Intel's site to a USB stick, inserting the stick to the NUC and pressing F7 at bootup to select the file.
    - **However, HDMI output seems broken in versions below 0037!**
    - If you don't get an image after flashing the BIOS and rebooting, don't despair, just put another version (at the time of writing **0038** was the latest) to the USB stick and follow the update procedure blind. That is, boot with the F7 option, select the device and file, wait, and hopefully when it reboots you will get a picture.
        - **It is a good idea to rehearse the procedure or record a video of it to know how to do it blind.**

#### OS installation

- **Install Ubuntu Server 14.04 (64-bit).**
    - The server flavour is optional but the kiosk instructions below are for an Ubuntu **without the default desktop environment**.
    - Server installation process asks more questions than the desktop version, but most can be answered with a default or blank line.
        - Do not choose to encrypt the disk.
    - Create a user called **screenful**.

- **Install kernel 3.14.1**
    - Needed for Xtion to work with USB 3.0 controller.
        - You may also need to update the Xtion firmware!

      ```bash
      wget http://kernel.ubuntu.com/~kernel-ppa/mainline/v3.14.1-trusty/linux-headers-3.14.1-031401_3.14.1-031401.201404141220_all.deb
      wget http://kernel.ubuntu.com/~kernel-ppa/mainline/v3.14.1-trusty/linux-headers-3.14.1-031401-generic_3.14.1-031401.201404141220_amd64.deb
      wget http://kernel.ubuntu.com/~kernel-ppa/mainline/v3.14.1-trusty/linux-image-3.14.1-031401-generic_3.14.1-031401.201404141220_amd64.deb
      sudo dpkg -i linux-headers-3.14.1-*.deb linux-image-3.14.1-*.deb
      ```

    - Reboot.

- **Install some needed packages**
    - This assumes server installation.

       ```bash
       sudo add-apt-repository ppa:webupd8team/java
       sudo apt-get update
       sudo apt-get install xserver-xorg xinit x11-apps chromium-browser screen elinks unclutter oracle-java8-installer git-core nodm aosd-cat
       ```

    - screen, elinks, unclutter, nodm and aosd-cat are not required for the server but in the actual appliance image are utilities for the admin and allow automatic login, hiding the mouse cursor and showing messages in the GUI.

- **Add user to group 'video'**

      ```bash 
      sudo gpasswd -a screenful video
      ```

- **Add udev rules for the sensor**
    - Copy the file ```55-primesense.rules``` to ```/etc/udev/rules.d/``` to allow the 'video' group to access the sensor.

#### Gesture server installation

- **Create a library directory**

      ```bash
      mkdir /home/screenful/libs
      sudo echo '/home/screenful/libs' > /etc/ld.so.conf.d/openni2.conf
      ```

- **Extract the required files**
    - **You need to acquire the NiTE library files from somewhere, they are not included.**
        - Place ```libNiTE2.jni.so``` and ```libNiTE2.so``` in ```/home/screenful/libs``` (the directory created above).
        - Place the ```NiTE2``` directory as a subdirectory in ```screenful-gestures/server/Screenful-GestureServer```.
    - Copy all OpenNI2 and NiTE2 .so files in the library directory.
    - Run ```sudo ldconfig``` to update shared library cache.

- **Edit libpaths.conf**
    - Edit ```wrapper.java.library.path.2=/home/screenful/libs``` to point to the library directory above.

- **The resulting library directory should look like this:**

    ```
    libs
    ├── libMWClosestPoint.so
    ├── libNiTE2.jni.so
    ├── libNiTE2.so
    ├── libOniFile.so
    ├── libOpenNI2.jni.so
    ├── libOpenNI2.so
    ├── libPS1080.so
    ├── libPSLink.so
    └── OpenNI2
        └── Drivers
            ├── libDummyDevice.so
            ├── libFreenectDriver.so
            ├── libOniFile.so
            ├── libPS1080.so
            └── libPSLink.so
    ```


#### Configuring the server

- **Edit ```server.conf``` to change tracking parameters and to specify gesture events.**

#### Running the server

- **Use ```run.sh``` to start the server in 'console' mode (foreground).**
- **To install the server as a system service, run ```sudo wrapper/bin/gestureserver install```**
    - More information: http://wrapper.tanukisoftware.com/doc/english/launch-nix.html#boot
    - After installing the service, you can use it as a normal system service in Ubuntu, using ```sudo service gestureserver start/stop/restart``` to control its state.

