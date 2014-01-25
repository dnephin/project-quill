#!/bin/bash
#
# Bootstrap the development environment with all dependencies
#

set -e

echo "
Checking for https://bugs.launchpad.net/ubuntu/+source/virtualbox/+bug/1252872

If your provision freezes here:

 1. Ctrl-C this setup
 2. Install the latest virtualbox ( >= 4.3.6 ) on the host
 3. Download http://download.virtualbox.org/virtualbox/4.3.6/VBoxGuestAdditions_4.3.6.iso
 4. Mount the image on the guest (sudo mount /vagrant/VBoxGuestAdditions_4.3.6.iso /mnt)
 5. Remove the old guest additions
    (sudo apt-get purge virtualbox-guest-dkms virtualbox-guest-utils virtualbox-guest-x11)
 6. Install the guest additions (sudo /mnt/VBoxLinuxAdditions.run)
 7. Shutdown (vagrant halt) and  re-provision (vagrant up --provision)
"
ls /vagrant > /dev/null


# TODO: add a check for virtualbox shared fs bug

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

# Scala
apt-get install -y libjansi-java
scala_version="scala-2.10.3"
wget -q http://www.scala-lang.org/files/archive/$scala_version.deb
dpkg -i $scala_version.deb

# sbt
apt-add-repository -y "deb http://dl.bintray.com/sbt/debian /"
apt-get update
apt-get install -y --force-yes sbt


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

