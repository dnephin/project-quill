
#
# Build a single version string from version parts
#
buildVersion = (version) ->
    # TODO: document and restrict each version part to 999
    version.major * 1000 * 1000 + version.minor * 1000 + version.patch

