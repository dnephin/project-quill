#!/bin/bash
#
# Bootstrap the development environment with all dependencies
#

set -e

# Install english language pack
sudo locale-gen en_US.UTF-8

sudo add-apt-repository -y ppa:chris-lea/node.js
sudo apt-get update -qq -y
sudo apt-get install -y nodejs

# TODO: set this to run at startup
# TODO: set bind_address to 0
sudo apt-get install -y couchdb
# TODO: store with views
curl -X PUT http://localhost:5984/statement


# TODO: replace with smaller binaries
# Scala and Play
sudo apt-get install unzip
get http://downloads.typesafe.com/typesafe-activator/1.0.0/typesafe-activator-1.0.0.zip
unzip typesafe-activator-1.0.0.zip
cd activator-1.0.0

curl http://www.scala-lang.org/files/archive/scala-2.10.2.tgz | tar -x && \
    cd scala-2.10.2


# Create a link from the users home directory to the shared folder
ln -sfT /vagrant project-quill
cd project-quill

npm cache clean

echo "Installing grunt-cli"
npm install -g -q grunt-cli coffee-script

echo "Installing node packages from package.json"
npm install -q --no-bin-link
# TODO: resolve linking errors, and remove --no-bin-link

