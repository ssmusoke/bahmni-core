#!/bin/sh -x
set +e
sudo fuser -k 8050/tcp
set -e
