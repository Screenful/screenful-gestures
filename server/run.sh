#!/bin/bash
cd "$(dirname $(readlink -f "$0"))"
wrapper/bin/gestureserver console
