

/**
 * This function is used by the grunt-text-replace task to replace !code
 * macros in couch design documents with the contents of the named file.
 */
replaceCode = function(grunt, baseDir) {
    var path = require('path');
    var cache = {};

    return function(_, _, _, filenames) {
        var filename = path.join(baseDir, filenames[0]);
        grunt.log.writeln('Using macro ' + filename);

        if (cache[filename] !== undefined) { return cache[filename]; }

        try {
            return cache[filename] = grunt.file.read(filename);
        } catch(e) {
            grunt.log.error('Failed to read ' + filepath);
            throw grunt.util.error('missing file', e)
        }
    }
}


module.exports = replaceCode
