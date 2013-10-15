project-quill
=============

*Project Status:* Early development


Development
-----------


Getting Started
~~~~~~~~~~~~~~~

One of the many goals of Project Quill is to allow a new developer to get up
and running in just a few minutes. To accomplish this
`vagrant <http://www.vagrantup.com>`_ is used to construct a development
environment from a configuration.

To get started follow these steps:

1. `Install vagrant <http://docs.vagrantup.com/v2/installation/index.html>`_

2. Start vagrant which will add a VirtualBox image of Ubuntu 13.04 64 bit.
   See `Vagrantfile <./Vagrantfile>`_)

.. code-block:: shell

    vagrant up

3. Log into the box


.. code-block:: shell

    vagrant ssh


4. Build + Start (TODO)


TODO: debugging problems with vagrant up, /etc/hosts problem


Directory Structure
~~~~~~~~~~~~~~~~~

dev
    tools and resources for development
docs
    project documentation
web_backend
    scala backend source
web_frontend
    web front-end source
database
    couchdb design documents and resources


