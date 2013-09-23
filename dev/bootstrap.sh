#!/bin/bash
#
# Bootstrap the development environment with all dependencies
#

set -e

sudo add-apt-repository ppa:chris-lea/node.js
sudo apt-get update
sudo apt-get install -y nodejs

# Create a link from the users home directory to the shared folder
ln -s /vagrant project-quill

cd /vagrant/
npm install grunt
npm install grunt-contrib-coffee
npm install jasmine-node
npm install grunt-contrib-watch
npm install grunt-contrib-less
npm install grunt-contrib-copy
npm install grunt-ember-handlebars

npm install -g grunt-cli
