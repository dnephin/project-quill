#!/bin/bash
#
# Bootstrap the development environment with all dependencies
#

set -e

# Install english language pack
sudo apt-get install -y language-pack-en
sudo locale-gen en_US.UTF-8

# Node
sudo add-apt-repository -y ppa:chris-lea/node.js
sudo apt-get update -qq -y
sudo apt-get install -y nodejs

# Couch
sudo apt-get install -y couchdb
sudo sed -i -e 's/^bind_address.*/bind_address = 0.0.0.0/' /etc/couchdb/default.ini
sudo service couchdb restart

# TODO: install jre instead
# Java
echo debconf shared/accepted-oracle-license-v1-1 select true | \
    sudo debconf-set-selections &&
    echo debconf shared/accepted-oracle-license-v1-1 seen true | \
    sudo debconf-set-selections
sudo add-apt-repository -y ppa:webupd8team/java
sudo apt-get update -qq -y
sudo apt-get install -y oracle-java6-installer

# Scala and Play
if [[ ! -d scala-2.10.3 ]]; then
    curl http://www.scala-lang.org/files/archive/scala-2.10.3.tgz | \
    sudo -u vagrant tar -xz
fi

sudo apt-get install unzip

if [[ ! -d play-2.2.0 ]]; then
    echo "Downloading play 2.2.0"
    wget -q http://downloads.typesafe.com/play/2.2.0/play-2.2.0.zip
    sudo -u vagrant unzip -q play-2.2.0.zip
fi


# Create a link from the users home directory to the shared folder
ln -sfT /vagrant project-quill
cd project-quill

npm cache clean

echo "Installing grunt-cli"
npm install -g -q grunt-cli coffee-script couchapp

echo "Installing node packages from package.json"
npm install -q --no-bin-link
# TODO: resolve linking errors, and remove --no-bin-link


echo "source ~/project-quill/dev/bash_env.sh" >> /home/vagrant/.bashrc
