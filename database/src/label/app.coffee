#
# app.js design document for label database
#


# TODO: is there a better way to support testing here?
module = if window?
    window.label = {}
else
    module
module.exports = ddoc =
    _id: '_design/app'
    views: {}
    updates: {}
    lists: {}
    shows: {}



