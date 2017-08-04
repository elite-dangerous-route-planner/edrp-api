#!/bin/bash
cat systems.csv | awk -F',' '{print $1,"|",$2,"|",$3,"|",$4,"|",$5,"|",$6,"|",$8}' > systemcoords.csv