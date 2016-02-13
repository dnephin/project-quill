Project Quill
=============

An open source group decision making tool.

.. contents::
    :backlinks: none

Background
----------

The last few decades have given us many tools for communicating and collaborating
with each other. However, most of those tools are focused on casual interaction
or business.  There is a distinct lack of tools which address one of the
fundamental modes of communication in our society.

From small student groups and communities, national organizations of thousands,
everyone relies on some form of communication to make decisions
as group.

Project Goals
-------------

A tool which is capable of facilitating the process of making a decision 
as a large group. Must support groups of hundreds of people without requiring
significant time from every participant.

The tool should facilitate decisions which are:

* constructive
* actionable
* transparent
* inclusive
* informed


Project Plan
------------

Architecture
~~~~~~~~~~~~

The high level components that make up the Quill system.

* Document editor - https://github.com/substance/substance

* Web application - React + ?

* API backend - Swagger API definition + ?

* Data storage - ?

* Document import

  * support importing documents from other common formats into quill format

* Document export

  * export a document and all feedback to a common format

* User service

  * user profiles consist of a list of links to other sites which provide the information
  * user photos using gravitar
  * auth using oauth from other sites + mozilla persona
  * stores notification address (sms, email, irc, slack)

* Notification service

  * allows users to subscribe to any tree and receive notifications on updates
  * stores user subscriptions with the chosen notification option
  * sends notifications

* Ingestion service

  * accept feedback from email, etc (sms?, slack?, irc?) 


Glossary
~~~~~~~~

Problem statement
    Describes the problem which requires the decision to be made. Provides
    context about why a decision is being made, and what is required to
    consider the decision acceptable or complete.

Document
    A multi-version multimedia (text, diagrams, etc) which describes the
    decision. A document is a collection of content blocks.

Content block
    Each section of a document is a content block: headings, paragraph,
    diagrams, videos, lists, etc

Feedback
    A response to a problem statement, document, or other feedback item.
    Includes a position, and text which elaborates on the position. A response
    is always directly tied to a content block, and a selection of text within
    that block (if the block is text).

Position
    A categorization of a feedback item, by the feedback contributor that
    describes intent:

    * **support** - strong agreement with additional evidence
    * **accept**  - weaker agreement
    * **challenge** - neither agreement or disagreement, the item is incomplete,
      or needs additional facts, or references.
    * **oppose** - disagreement
    * **ignore** - the item is not interesting, or not relevant
    * **violation** - the item is fake, intentionally misleading, sponsored by
      a third-party, fraudulent, offensive, or otherwise breaks protocol
