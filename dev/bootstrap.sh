#!/bin/bash
#
# Bootstrap the development environment with all dependencies
#

set -e

# Install english language pack
sudo locale-gen en_US.UTF-8

# Node
sudo add-apt-repository -y ppa:chris-lea/node.js
sudo apt-get update -qq -y
sudo apt-get install -y nodejs

# Couch
sudo apt-get install -y couchdb
sudo sed -i -e 's/^bind_address.*/bind_address = 0.0.0.0/' /etc/couchdb/default.ini
sudo /etc/init.d/couchdb restart
# TODO: store with design documents
curl -X PUT http://localhost:5984/statement

# TODO: install jre instead
# Java
sudo add-apt-repository ppa:webupd8team/java
sudo apt-get update
sudo apt-get install oracle-java6-installer

# Scala and Play
curl http://www.scala-lang.org/files/archive/scala-2.10.3.tgz | tar -xz

sudo apt-get install unzip
wget http://downloads.typesafe.com/play/2.2.0/play-2.2.0.zip
unzip -q play-2.2.0.zip


# Create a link from the users home directory to the shared folder
ln -sfT /vagrant project-quill
cd project-quill

npm cache clean

echo "Installing grunt-cli"
npm install -g -q grunt-cli coffee-script

echo "Installing node packages from package.json"
npm install -q --no-bin-link
# TODO: resolve linking errors, and remove --no-bin-link

