#!/bin/bash

# Exit on nonzero command return values
set -e
[ "$UID" != "0" ] && echo "Run this script with sudo" && exit 1

function msg () { echo -e "\n\n----[$STEP] $1"; ((STEP++)); }
STEP=1

# Destination directory in current directory, either 
# first command line argument or default "OpenNI-libs"
DESTDIR="${PWD}/OpenNI-libs"
[ -n "$1" ] && DESTDIR="$(readlink -f $1)"
NICLONEDIR="OpenNI2"
FREENECTCLONEDIR="libfreenect"
OPENNI_DIR="${DESTDIR}/${NICLONEDIR}"
NITE_ARCHIVE="NiTE-Linux-x64-2.2.tar.bz2"
NITE_DIR="${DESTDIR}/${NITE_ARCHIVE}"

# uncomment to check out the versions this script was written for
#NICOMMIT="7bef8f639e4d64a85a794e85fe3049dbb2acd32e"
#FREENECTCOMMIT="cb0254a10dbeae8bdb8095d390b4ff69a2becc6e"


function confirm-begin () {
	for clonedir in "${DESTDIR}/${NICLONEDIR}" "${DESTDIR}/${FREENECTCLONEDIR}"
		do 
			if [ -d "${clonedir}" ]
				then echo "--- * Existing ${clonedir} directory, (re)move manually and rerun. Aborting."
				exit 1
			fi
	done
	
	echo -e "\nPress Enter to begin installing to:\n${DESTDIR}\n${OPENNI_DIR}\n${DESTDIR}/${FREENECTCLONEDIR}\n\n... or Ctrl-C to abort.\nPass directory name as first argument to change the default."
	read
}

function create-destination () {
	msg "Creating destination directory ${DESTDIR}"
	[ -d "${DESTDIR}" ] || mkdir -p "${DESTDIR}" && true
}

function install-prereqs () {
	msg "Installing prerequisites"
	apt-get update
	apt-get -y install git-core g++ cmake libudev-dev libxi-dev libxmu-dev python libusb-1.0-0-dev libudev-dev freeglut3-dev doxygen graphviz
}

function install-java8 () {
	msg "Installing Oracle Java 8"
	# Add Oracle Java installer repository
	add-apt-repository ppa:webupd8team/java
	apt-get update
	apt-get -y install oracle-java8-installer
}

function clone-fix-openni2 () {
	cd "${DESTDIR}"
	msg "Cloning OpenNI2"
	git clone https://github.com/OpenNI/OpenNI2.git "${NICLONEDIR}"
	if [ -n "${NICOMMIT}" ]
		then cd "${NICLONEDIR}"
			 git checkout "${NICOMMIT}"
	fi
	msg "Fixing compilation flags"
	sed -i '/-Werror/ s/^/#/' "${OPENNI_DIR}/ThirdParty/PSCommon/BuildSystem/CommonCppMakefile"
	echo "LDFLAGS += -lpthread" >> "${OPENNI_DIR}"/Source/Tools/NiViewer/Makefile
}

function build-openni2 () {
	cd "${OPENNI_DIR}"
	msg "Building OpenNI2"
	make	
}

function generate-kinect-rules () {
	echo "# Rules for Kinect & Kinect for Windows' Motor, Audio and Camera"
	for idProduct in 02b0 02ad 02ae 02c2 02be 02bf
		do echo "SUBSYSTEM==\"usb\", ATTR{idVendor}==\"045e\", ATTR{idProduct}==\"${idProduct}\", MODE:=\"0666\", OWNER:=\"root\", GROUP:=\"video\""
	done 
}

function add-udev-rules () {
	msg "Adding udev rules for Primesense sensors"
	chmod +x "${OPENNI_DIR}"/Packaging/Linux/install.sh 
	"${OPENNI_DIR}"/Packaging/Linux/install.sh 
	msg "Adding udev rules for Kinect"
	generate-kinect-rules > /etc/udev/rules.d/51-kinect.rules
	#cp "${DESTDIR}/${FREENECTCLONEDIR}/platform/linux/udev/51-kinect.rules" /etc/udev/rules.d/
}

function add-user-to-video () {
	msg "Adding user to video group"
	gpasswd -a "${SUDO_USER}" video 
}

function build-freenect () {
	msg "Cloning libfreenect"
	cd "${DESTDIR}"
	git clone https://github.com/OpenKinect/libfreenect.git "${FREENECTCLONEDIR}"
	cd "${FREENECTCLONEDIR}"
	if [ -n "${FREENECTCOMMIT}" ]
		then git checkout "${FREENECTCOMMIT}"
	fi
	
	mkdir build; cd build 

	msg "Building libfreenect"
	cmake .. -DBUILD_OPENNI2_DRIVER=ON 
	make

	msg "Copying Kinect driver to OpenNI2 Drivers directory"
	cp -L lib/OpenNI2-FreenectDriver/libFreenectDriver.so "${OPENNI_DIR}"/Bin/x64-Release/OpenNI2/Drivers/
}

function fix-owner () {
	msg "Giving destination directory ownership to ${SUDO_USER}"
	chown -R "${SUDO_USER}":"${SUDO_USER}" "${DESTDIR}"
}

function add-ld-confs () {
	msg "Adding library directories to /etc/ld.so.conf.d/"
	echo "${OPENNI_DIR}/Bin/x64-Release/" > /etc/ld.so.conf.d/openni2.conf
	echo "${NITE_DIR}/Redist" > /etc/ld.so.conf.d/nite2.conf
	ldconfig
}

function ready () {
	msg "All done, plug in Kinect (wait a few seconds) and press Enter to run NiViewer as $(whoami) for testing, or Ctrl-C to exit at this point."
	echo "--- * Important: to use as normal user, login again or type 'newgrp video'"
	echo "--- * In NiViewer, press '?' to get help."
	read

	msg "Run NiViewer"
	cd "${OPENNI_DIR}"/Bin/x64-Release/
	./NiViewer
}

function install-nite () {
	msg "Installing NiTE library"
	echo "--- * Note: NiTE library is not publicly available online anymore. If you have the file NiTE-Linux-x64-2.2.tar.bz2, put it in ${DESTDIR}/ now. This is the last and optional step and will extract the archive and set the environment variables to the user's .bashrc."
	echo "--- * Press Ctrl-C to cancel or Enter to continue."
	read
	cd "${DESTDIR}"
	if [ -f "${NITE_ARCHIVE}" ]
		then tar xjf "${NITE_ARCHIVE}"
		     cd "$(basename ${NITE_ARCHIVE} .tar.bz2)"
		     ./install.sh
		     cat NiTEDevEnvironment >> ~"${SUDO_USER}"/.bashrc
		else echo "--- * No ${DESTDIR}/NiTE-Linux-x64-2.2.tar.bz2 found, finishing."
	fi
}


# Install sequence.
# If you don't want to install java8 for example, comment
# out install-java8

confirm-begin
create-destination
install-prereqs
install-java8

clone-fix-openni2
build-openni2
build-freenect

add-udev-rules
add-user-to-video
fix-owner
add-ld-confs

ready

# optional step for installing the NiTE library, if the archive is available
install-nite

# Finished
