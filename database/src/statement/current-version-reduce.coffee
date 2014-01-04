
#
# Return the [id, version)] pair with the max version
#
maxVersion = (values) ->
    max = (prev, next) ->
        if prev[1] > next[1] then prev else next
    values.reduce(max)

