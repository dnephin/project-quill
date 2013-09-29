#!/bin/bash
#
# Bootstrap the development environment with all dependencies
#

set -e

sudo add-apt-repository -y ppa:chris-lea/node.js
sudo apt-get update -qq -y
sudo apt-get install -y nodejs

# Create a link from the users home directory to the shared folder
ln -sfT /vagrant project-quill
cd project-quill

npm cache clean

echo "Installing grunt-cli"
npm install -g -q grunt-cli coffee-script

echo "Installing node packages from package.json"
npm install -q --no-bin-link
# TODO: resolve linking errors, and remove --no-bin-link

