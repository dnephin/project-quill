
// TODO: use macro to include this from shared
// http://couchdbkit.org/docs/api/couchdbkit.designer.macros-module.html

(keys, values, rereduce) ->
    max = (prev, next) ->
        if prev.version.value > next.version.value then prev else next
        
    values.reduce(max)
