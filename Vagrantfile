# -*- mode: ruby -*-
# vi: set ft=ruby :

VAGRANTFILE_API_VERSION = "2"

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|

  config.vm.box = "project-quill"
  config.vm.box_url = "http://cloud-images.ubuntu.com/vagrant/saucy/20140101/" +
                      "saucy-server-cloudimg-amd64-vagrant-disk1.box"

  # Install project dependencies
  config.vm.provision "shell", path: "dev/bootstrap.sh"

  # Port forward for grunt livereload server
  config.vm.network "forwarded_port", guest: 35729, host: 8081
  # Port forward for couchdb server
  config.vm.network "forwarded_port", guest: 5984,  host: 8082
  # Port forward for nginx (frontend, play dev server)
  config.vm.network "forwarded_port", guest: 1080,  host: 8083

  config.vm.hostname = "quill-dev"


  # TODO: stalls on first provision because of ubuntu bug
  # See http://askubuntu.com/questions/324574/cannot-ssh-into-new-vagrant-install-of-13-04
  # and https://bugs.launchpad.net/ubuntu/+source/cloud-init/+bug/1217950

  config.vm.provider "virtualbox" do |vbox|
    # TODO: create a debug flag to enable this
    # Debug problems using gui mode
    # vbox.gui = true

    # Set resource limits
    # See http://www.virtualbox.org/manual/ch08.html#vboxmanage-modifyvm
    vbox.customize ["modifyvm", :id, "--cpus", "2"]
    vbox.customize ["modifyvm", :id, "--memory", "2048"]
    # Set DNS host resolver enabled
    vbox.customize ["modifyvm", :id, "--natdnshostresolver1", "on"]

  end

end
