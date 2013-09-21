#!/bin/bash
#
# Bootstrap the development environment with all dependencies
#

set -e


wget http://nodejs.org/dist/v0.10.18/node-v0.10.18.tar.gz
tar -xf node-v0.10.18.tar.gz
cd  node-v0.10.18; ./configure; make; sudo make install

npm install grunt-cli grunt-contrib-coffee jasmine-node
npm install grunt-contrib-watch grunt-contrib-less






