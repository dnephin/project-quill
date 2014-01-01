###
 Common validation functions for validate_doc_update
###

# TODO: when couchdb is upgraded use require to include this

validate = (doc, path, type, msg) ->
    getItem = (obj, key) ->
        if obj? then obj[key] else null

    item = path.split('.').reduce(getItem, doc)
    if typeof item != type
        msg = msg or
            "document requires #{path} to be #{type} it is #{typeof item}"
        throw(forbidden: msg)
