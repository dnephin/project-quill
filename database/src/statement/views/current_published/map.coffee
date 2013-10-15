
(doc) ->
    # TODO: document and restrict each version part to 999
    getVersion = (version) ->
        version.major * 1000 * 1000 + version.minor * 1000 + version.patch

    if doc.version.active and doc.version.published
        doc.version.value = getVersion(doc.version)
        emit(doc.label, doc)
