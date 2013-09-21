#!/bin/bash
#
# Bootstrap the development environment with all dependencies
#

set -e

sudo add-apt-repository ppa:chris-lea/node.js
sudo apt-get update
sudo apt-get install -y nodejs


cd /vagrant/
npm install grunt --save-dev
npm install grunt-contrib-coffee --save-dev
npm install jasmine-node --save-dev
npm install grunt-contrib-watch --save-dev
npm install grunt-contrib-less --save-dev
npm install grunt-contrib-copy --save-dev


npm install -g grunt-cli
