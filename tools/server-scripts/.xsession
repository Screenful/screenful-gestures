#!/usr/bin/env bash

# Set a fancy background image
feh --no-fehbg --bg-scale /home/screenful/pic/screenful-background.png

# Get current IP address
getip () {
	ifconfig  | grep 'inet addr:'| grep -v '127.0.0.1' | cut -d: -f2 | awk '{ print $1}'
}

# Show configuration menu tip on screen
menu_help () {
	DELAY=3000
	[ -n "$1" ] && DELAY="$1"
	echo -e "Press Ctrl-Alt-F1 for system configuration" | aosd_cat -l 2 -R "black" -n "Sans Bold 20" -S "Dim Grey" -u "$DELAY" -p 6 -x 24 -y "-24" -e 2 &
}

# Disable screen blanking
xset -dpms s off
xset s noblank

# Hide mouse cursor after two seconds of inactivity
unclutter -root -idle 2 &

# Find resolution from xrandr string and store it in an array eg. ("1920", "x", "1080")
read -a RESOLUTION <<< "$(xrandr|grep current|sed 's/.*current \(.*\), maximum.*/\1/')"
WIDTH="${RESOLUTION[0]}"
HEIGHT="${RESOLUTION[2]}"

# Wait for network connection to come up before starting URL
# (Wireless may take some time to connect)
osdmsg 99999999 "Waiting for network connection..." &
menu_help 99999999
wait-for-net
IPADDR="$(getip)"
while [[ -z "${IPADDR}" ]]
	do IPADDR="$(getip)"
	   sleep 0.5
done
pkill aosd_cat
menu_help
# Show the message again
osdmsg 5000 "Connected (IP: ${IPADDR})" &

APPURL=$(</home/screenful/conf/app-url.conf)
# Start chromium in app and kiosk mode and specify window size according to resolution.
# Add 13 pixels to width and height to push possible scrollbars out of visible area.
chromium-browser --app="${APPURL}" --window-size="$[WIDTH + 13]","$[HEIGHT + 13]" --kiosk --incognito
