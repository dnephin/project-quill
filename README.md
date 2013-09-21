project-quill
=============


## Development

One of the many goals of Project Quill is to allow a new developer to get up
and running in just a few minutes. To accomplish this
[vangrant](http://www.vagrantup.com/) is used to construct a development
environment from a configuration.

To get started follow these steps:

1. [Install vagrant](http://docs.vagrantup.com/v2/installation/index.html)

2. Start vagrant which will add a VirtualBox image of Ubuntu 13.04 64 bit.
   See (VagrantFile)[./VagrantFile]

    vagrant up

3. Log into the box

    vagrant ssh

# TODO: debugging problems here, /etc/hosts problem

4. Build + Start

# TODO: cd to /vagrant and start grunt/web server
