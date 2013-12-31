Database README
===============

This directory contains couchDB design documents. Grunt is used to build and
upload these documents in dev (see `../Gruntfile.coffee <../Gruntfile.coffee>`_).

Common commands:

====================    ==============
Command                 Description
====================    ==============
grunt buildDatabase     Create the couchdb databases and load the design documents
grunt watch:database    Start a file watcher which will rebuild, lint and test
                        database design documents
====================    ==============
