# -*- mode: ruby -*-
# vi: set ft=ruby :

VAGRANTFILE_API_VERSION = "2"

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|

  config.vm.box = "project-quill"
  config.vm.box_url = "http://cloud-images.ubuntu.com/vagrant/raring/current" +
                      "/raring-server-cloudimg-amd64-vagrant-disk1.box"

  config.vm.provision "shell", path: "dev/bootstrap.sh"

  # TODO: stalls on first provision

  # Debug problems with gui mode
  #config.vm.provider "virtualbox" do |vbox|
  #    vbox.gui = true
  #end

  # Create a forwarded port mapping which allows access to a specific port
  # within the machine from a port on the host machine. In the example below,
  # accessing "localhost:8080" will access port 80 on the guest machine.
  # config.vm.network :forwarded_port, guest: 80, host: 8080

  # Create a private network, which allows host-only access to the machine
  # using a specific IP.
  # config.vm.network :private_network, ip: "192.168.33.10"

  # Create a public network, which generally matched to bridged network.
  # Bridged networks make the machine appear as another physical device on
  # your network.
  # config.vm.network :public_network

  # If true, then any SSH connections made will enable agent forwarding.
  # Default value: false
  # config.ssh.forward_agent = true

end
