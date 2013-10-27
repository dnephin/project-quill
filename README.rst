project-quill
=============

*Project Status:* Early development

.. contents:: Contents
    :local:


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

2. Start vagrant. It will add a VirtualBox image of Ubuntu 13.04 64 bit.
   See `Vagrantfile <./Vagrantfile>`_)

    .. code-block:: bash

        vagrant up

    TODO: debugging problems with vagrant up, and /etc/hosts problem

3. Login to the box

    .. code-block:: bash

        vagrant ssh

4. Setup couchdb environment. See `<database/README.rst>`_

    .. code-block:: bash

        # Ensure that couchdb is running
        sudo service couchdb start
        # Setup databases
        grunt buildCouchdb

5. Build frontend sources

    .. code-block:: bash

        grunt buildFrontend

6. Build backend sources and start webserver

    .. code-block:: bash

        cd web_backend
        play run



Directory Structure
~~~~~~~~~~~~~~~~~~~

The project repository has this structure:

==============    ======================================================
database          couchdb design documents and resources
dev               tools and resources for development
dist              target directory for distribution packages
docs              project documentation
web_backend       scala back-end source
web_frontend      web front-end source (coffeescript, less, handlebars)
==============    ======================================================


Coding Standards
~~~~~~~~~~~~~~~~

TODO

