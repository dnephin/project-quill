#!/bin/bash
#
# Bootstrap the development environment with all dependencies
#

set -e

sudo add-apt-repository ppa:chris-lea/node.js
sudo apt-get update
sudo apt-get install -y nodejs

# Create a link from the users home directory to the shared folder
ln -sfT /vagrant project-quill

cd project-quill

# Install node packages from package.json
npm install

npm install -g grunt-cli
