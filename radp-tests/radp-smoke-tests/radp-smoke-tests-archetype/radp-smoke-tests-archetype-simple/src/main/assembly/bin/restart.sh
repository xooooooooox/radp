#!/bin/bash

echo restart application
source ./catalina.sh stop
source ./catalina.sh start
exit
