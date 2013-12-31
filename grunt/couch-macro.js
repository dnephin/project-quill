

/**
 * This function is used by the grunt-text-replace task to replace !code
 * macros in couch design documents with the contents of the named file.
 */
replaceCode = function(grunt) {
    var cache = {};

    return function(_, _, _, filename) {
        grunt.log.writeln('Using macro ' + filename);
        if (cache[filename] !== undefined) { return cache[filename]; }
        try {
            return cache[filename] = grunt.file.read(filename);
        } catch(e) {
            grunt.log.error('Failed to read ' + filename);
            throw grunt.util.error('missing file', e)
        }
    }
}


module.exports = replaceCode
