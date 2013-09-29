# -*- mode: ruby -*-
# vi: set ft=ruby :

VAGRANTFILE_API_VERSION = "2"

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|

  config.vm.box = "project-quill"
  config.vm.box_url = "http://cloud-images.ubuntu.com/vagrant/raring/20130928/" +
                      "raring-server-cloudimg-amd64-vagrant-disk1.box"

  # Install project dependencies
  config.vm.provision "shell", path: "dev/bootstrap.sh"

  # Port forwarding for grunt livereload server
  config.vm.network "forwarded_port", guest: 35729, host: 8081

  config.vm.hostname = "quill-dev"


  # TODO: stalls on first provision because of ubuntu bug
  # See http://askubuntu.com/questions/324574/cannot-ssh-into-new-vagrant-install-of-13-04
  # and https://bugs.launchpad.net/ubuntu/+source/cloud-init/+bug/1217950

  config.vm.provider "virtualbox" do |vbox|
    # TODO: create a debug flag to enable this
    # Debug problems using gui mode
    vbox.gui = true
  end

  # Create a private network, which allows host-only access to the machine
  # using a specific IP.
  # config.vm.network :private_network, ip: "192.168.33.10"

  # Create a public network, which generally matched to bridged network.
  # Bridged networks make the machine appear as another physical device on
  # your network.
  # config.vm.network :public_network

end
