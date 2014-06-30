### Rooting MiniX Neo X7 and running Nuidroid examples

* To run Nuidroid on the MiniX one needs to:

1. root the device:
	- (info from here: http://www.freaktab.com/showthread.php?11595-Flashable-stock-Minix-X7-Pre-010-ROM )
	- get http://minix.com.hk/firmware/pre010b.zip
	- get http://www.freak-tab.de/finless/neox7_root_kit.zip
	- `sudo apt-get install android-tools-adb android-tools-adbd`
	- power on MiniX, connect it to (W)LAN and check its ip (from android system info somewhere or router admin page)
	- `adb connect <ip>`
	- `adb push pre010b.zip /mnt/sdcard/update.zip`
	- reboot the MiniX, after booting to desktop it should ask whether to apply update.zip, apply and it'll reboot, then asks whether to delete update.zip, answer yes
	- unzip the rootkit zip
	- `adb push update.zip /mnt/sdcard`
	- reboot the MiniX once again, and again apply the update
2. device is now rooted and you can install supersu or similar program from App store or perhaps using adb to install a .apk manually, this is to allow control over how programs can use root and is required by nuidroid
3. get nuidrod_openni_v1.zip and extract it somewhere
4. `adb connect <ip>; adb install apk/NuidroidInstaller.apk` (you can also install the other packages but they might be included in the main installer anyway)
5. the installer should run and you need to click on some buttons, the license page seemed to require using a wheel mouse to actually scroll to bottom and clicking on the accept tick
6. you should have a few extra apps available now, OpenNISample, UserTracker and SimpleViewer, start one and when a su dialog asks, grant the program root access
