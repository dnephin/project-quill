#!/bin/bash
#
# Bootstrap the development environment with all dependencies
#

set -e

# Update apt cache
apt-get update -qq

# Install english language pack
apt-get install -y language-pack-en
locale-gen en_US.UTF-8

apt-get -y install git tree

# Node
add-apt-repository -y ppa:chris-lea/node.js
apt-get update -qq
apt-get install -y nodejs

# Couch
apt-get install -y couchdb
sed -i -e 's/^bind_address.*/bind_address = 0.0.0.0/' /etc/couchdb/default.ini
service couchdb restart

# nginx
apt-get install -y nginx
rm /etc/nginx/nginx.conf
ln -sf /vagrant/dev/nginx.conf /etc/nginx/nginx.conf
service nginx restart

# memcached
apt-get install -y memcached


# Java
echo debconf shared/accepted-oracle-license-v1-1 select true | \
    debconf-set-selections &&
    echo debconf shared/accepted-oracle-license-v1-1 seen true | \
    debconf-set-selections
add-apt-repository -y ppa:webupd8team/java
apt-get update -qq
apt-get install -y oracle-java6-installer

# Scala and Play
if [[ ! -d scala-2.10.3 ]]; then
    curl http://www.scala-lang.org/files/archive/scala-2.10.3.tgz | \
    sudo -u vagrant tar -xz
fi

apt-get install -y unzip

play_version="play-2.2.1"
if [[ ! -d $play_version ]]; then
    echo "Downloading $play_version"
    wget -q http://downloads.typesafe.com/play/2.2.1/$play_version.zip
    sudo -u vagrant unzip -q $play_version.zip
fi

# TODO: install sbt

echo "Installing global npm packages"
npm install -g -q grunt-cli coffee-script bower

# Create a link from the users home directory to the shared folder
ln -sfT /vagrant project-quill
cd project-quill

echo "Installing node packages from package.json"
npm install -q --no-bin-link

cd web_frontend
sudo -u vagrant -H bower install

echo "source ~/project-quill/dev/bash_env.sh" >> /home/vagrant/.bashrc

