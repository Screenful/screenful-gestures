### Installing OpenNI2, libfreenect and the Kinect sensor on Ubuntu 14.04

**Note: openni.org referenced in some documentation is not online anymore.**

* Commands are for Ubuntu 14.04 LTS or similar Debian based system.
* The script has been tested with Ubuntu 14.04 LTS booted in live mode.

#### Quick install with `kinect-ubuntu-install.sh`

This script automates all the manual steps in the next section. Note that it needs to be run with sudo. This ensures that also the non-root account gets all the required permissions.

**Note: runs commands as root. Tries to be careful though.**  
**Note: the script installs Oracle Java 8. If you already have javac, you can comment out the `install-java8` line**

1. Make sure you're connected to internet and start a terminal
2. Run:

	```bash
	git clone https://github.com/Screenful/screenful-gestures
	sudo screenful-gestures/tools/kinect-ubuntu-install/kinect-ubuntu-install.sh
	```
3. Wait a while for everything to install and answer **Yes** when the Java installer asks you to accept the license, no other interaction is needed.

#### Manual install

##### Get prerequisites:

1.  Install packages

    ```bash
    sudo apt-get install git-core g++ cmake libudev-dev libxi-dev libxmu-dev python libusb-1.0-0-dev libudev-dev freeglut3-dev doxygen graphviz
    ```  
2. Get OpenNI2 library from https://github.com/OpenNI/OpenNI2

    ```bash
	git clone https://github.com/OpenNI/OpenNI2.git
	cd OpenNI2
	# Save path for further reference
	OPENNI_DIR="${PWD}"
    ```
3. Fix some issues with compiling (in OpenNI2 commit 7bef8f639e4d64a85a794e85fe3049dbb2acd32e)

    ```bash
	## Comment out "treat warnings as errors" option in PSCommon Makefile
	sed -i '/-Werror/ s/^/#/' ${OPENNI_DIR}/ThirdParty/PSCommon/BuildSystem/CommonCppMakefile
	## Fix NiViewer Makefile (missing -lpthread in linker arguments)
	echo "LDFLAGS += -lpthread" >> ${OPENNI_DIR}/Source/Tools/NiViewer/Makefile
	make
    ```
4. Add udev rules to access the sensor as normal user
	```bash
	## Add udev rules for Primesense sensor device IDs
	sudo ${OPENNI_DIR}/Packaging/Linux/install.sh
	```
	- libfreenect ships with 51-kinect.rules file, but it doesn't seem to work with Ubuntu 14.04.
	- The following rules allow video group to access the Kinect components (Motor, Audio, Camera). Save in /etc/udev/rules.d/
        - **eg. /etc/udev/rules.d/51-kinect.rules**

	```shell
	# Rules for Kinect & Kinect for Windows' Motor, Audio and Camera
	SUBSYSTEM=="usb", ATTR{idVendor}=="045e", ATTR{idProduct}=="02b0", MODE:="0666", OWNER:="root", GROUP:="video"
	SUBSYSTEM=="usb", ATTR{idVendor}=="045e", ATTR{idProduct}=="02ad", MODE:="0666", OWNER:="root", GROUP:="video"
	SUBSYSTEM=="usb", ATTR{idVendor}=="045e", ATTR{idProduct}=="02ae", MODE:="0666", OWNER:="root", GROUP:="video"
	SUBSYSTEM=="usb", ATTR{idVendor}=="045e", ATTR{idProduct}=="02c2", MODE:="0666", OWNER:="root", GROUP:="video"
	SUBSYSTEM=="usb", ATTR{idVendor}=="045e", ATTR{idProduct}=="02be", MODE:="0666", OWNER:="root", GROUP:="video"
	SUBSYSTEM=="usb", ATTR{idVendor}=="045e", ATTR{idProduct}=="02bf", MODE:="0666", OWNER:="root", GROUP:="video"
	```

5. Join user to group
	```bash
	## Add current user to video group (specified in udev rules)
	sudo gpasswd -a ${USER} video
	```
6. Install Oracle java8 (optional, but compiling OpenNI2 Java wrappers requires javac):
	```bash
	sudo add-apt-repository ppa:webupd8team/java
	sudo apt-get update
	sudo apt-get install oracle-java8-installer
	```

7. For Kinect support (Freenect bridge driver for Kinect to work under Linux/OSX):
	- https://github.com/OpenKinect/libfreenect/tree/master/OpenNI2-FreenectDriver
	- compile and copy driver to OpenNI2 directory:
	```bash
	git clone https://github.com/OpenKinect/libfreenect.git
	cd libfreenect
	mkdir build; cd build
	cmake .. -DBUILD_OPENNI2_DRIVER=ON
	make
	cp -L lib/OpenNI2-FreenectDriver/libFreenectDriver.so ${OPENNI_DIR}/Bin/x64-Release/OpenNI2/Drivers/
	```

8. If all went okay, you should now be able to plug in a Kinect / Xtion / Primesense sensor and run the example programs:
	```bash
	cd ${OPENNI_DIR}/Bin/x64-Release/
	./NiViewer	
	```
* **Press '?' in NiViewer to get help and try out the various modes.**
* **To start recording sensor data into a file, press 'c'. To stop recording, press 'x'. A .oni file is generated in the working directory.**

#### Troubleshooting

* **Installation failed and I can't remove the leftovers**
    * Use sudo to remove the files - they were copied there as root
* **The sensor is not found**
    * Try unplugging the sensor and plugging it back again, then wait for some seconds (10 to be sure).
    * Check last lines of `dmesg` output to see if the device shows up
    * Try running the program with sudo
        * If it works then, make sure you've either logged out and back in again after you added your user to the `video` group, or type `newgrp video` and see if you can then run it as normal user.
* **Something fails with the compile**
    * The script makes some fixes to build files and these may be very specific to a certain commit. If you uncomment the lines with `NICOMMIT=...` and `FREENECTCOMMIT=...` you can specify which version to checkout for the builds.
        * In `kinect-ubuntu-install.sh`:
        ```bash
        # uncomment to check out the versions this script was written for
        #NICOMMIT="7bef8f639e4d64a85a794e85fe3049dbb2acd32e"
        #FREENECTCOMMIT="cb0254a10dbeae8bdb8095d390b4ff69a2becc6e"
        ```

