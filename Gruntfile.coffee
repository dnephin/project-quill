#
# Gruntfile for web_frontend and database
#

#
# Constants for file paths
#
paths =
    gruntfile:  'Gruntfile.coffee'

    web_frontend:

        coffee:
            src:    'web_frontend/src/coffee/**/*.coffee'
            dest:   'dist/web/js/quill.js'

        specs:
            src:    'web_frontend/spec/coffee/*.coffee'
            dest:   'web_frontend/spec/js/quillSpec.js'

        less:
            src:    'web_frontend/src/less/*.less'
            dest:   'dist/web/css/quill.css'

        static:
            cwd:    'web_frontend/static/'
            src:    ['**']
            dest:   'dist/web'
            filter: 'isFile'
            expand: true

        handlebars:
            basePath:   'web_frontend/src/handlebars/'
            files:
                src:    'web_frontend/src/handlebars/**/*.hbs'
                dest:   'dist/web/js/templates.js'

        bower:
            js:
                src: [
                    'web_frontend/bower/jquery/jquery.js'
                    'web_frontend/bower/bootstrap/dist/js/bootstrap.js'
                    'web_frontend/bower/handlebars/handlebars.js'
                    'web_frontend/bower/ember/ember.js'
                    'web_frontend/bower/ember-data-shim/ember-data.js'
                ]
                dest: "dist/web/js/"
                expand: true
                flatten: true
            css:
                src: [
                    'web_frontend/bower/bootstrap/dist/css/bootstrap.css'
                    'web_frontend/bower/bootstrap/dist/css/' +
                        'bootstrap-theme.css'
                ]
                dest: "dist/web/css/"
                expand: true
                flatten: true

    database:
        files:
            cwd:        'database/src/'
            src:        ['**/*.coffee']
            dest:       'dist/database/'
            expand:     true
            ext:        '.js'

        specs:
            cwd:        'database/spec/coffee/'
            src:        ['**/*.coffee']
            dest:       'database/spec/js/'
            expand:     true
            ext:        '.js'


#
# Return a list of file paths from a file options mapping
#
pathsFromOpts = (opts) ->
    opts.cwd + src for src in opts.src


# TODO: clean this up
buildCouch = (name) ->
    couchdb_url = 'http://localhost:5984'

    db: "#{couchdb_url}/#{name}"
    app: "dist/database/#{name}/app.js"
    options:
        okay_if_exists: true
        okay_is_missing: true


databases =
    statement: buildCouch('statement')
    label: buildCouch('label')
    user: buildCouch('user')
    feedback: buildCouch('feedback')


module.exports = (grunt) ->

    require('jit-grunt') grunt,
        emberTemplates: 'grunt-ember-templates'
        mkcouchdb:      'grunt-couchapp'
        replace:        'grunt-text-replace'
        jasmine_node:   'grunt-jasmine-node'

    grunt.option 'stack', true

    grunt.initConfig
        coffee:
            app:
                files: [paths.web_frontend.coffee]
            appSpec:
                options:
                    bare: true
                files: [paths.web_frontend.specs]
            database:
                options:
                    bare: true
                files: [paths.database.files]
            databaseSpec:
                options:
                    bare: true
                files: [paths.database.specs]

        coffeelint:
            options:
                arrow_spacing: 'error'
                cyclomatic_complexity:
                    level: 'error'
                    value: 6 # This may need to increase to 10
                line_endings: 'error'
                space_operators: 'error'
                indentation:
                    level: 'error'
                    value: 4

            app: [
                paths.web_frontend.coffee.src
                paths.web_frontend.specs.src
            ]
            build: paths.gruntfile
            database: [].concat(
                pathsFromOpts(paths.database.files),
                pathsFromOpts(paths.database.specs))

        jasmine:
            appTest:
                src: paths.web_frontend.coffee.dest
                options:
                    vendor: paths.web_frontend.bower.js.src
                    specs: paths.web_frontend.specs.dest

        jasmine_node:
            projectRoot: paths.database.files.dest
            specFolders: [
                paths.database.specs.dest
            ]
            colors: true

        copy:
            static:
                files: [paths.web_frontend.static]
            bower:
                files: [
                    paths.web_frontend.bower.js
                    paths.web_frontend.bower.css
                ]

        less:
            app:
                files: [paths.web_frontend.less]

        emberTemplates:
            compile:
                options:
                    templateBasePath: paths.web_frontend.handlebars.basePath
                files: [paths.web_frontend.handlebars.files]

        mkcouchdb: databases

        couchapp: databases

        watch:
            options:
                # TODO: disable again once grunt-jasmine-node #9 is fixed
                spawn: true
                livereload: true

            appCoffee:
                files: paths.web_frontend.coffee.src
                tasks: [
                    'coffeelint:app'
                    'coffee:app'
                    'coffee:appSpec'
                    'jasmine:appTest'
                ]

            gruntfile:
                files: paths.gruntfile
                tasks: ['coffeelint:build']

            database:
                files: [].concat(
                    pathsFromOpts(paths.database.files),
                    pathsFromOpts(paths.database.specs))
                tasks: [
                    'coffeelint:database'
                    'coffee:database'
                    'coffee:databaseSpec'
                    'couchMacro:database'
                    'jasmine_node'
                    'couchapp'
                ]

            less:
                files: paths.web_frontend.less.src
                tasks: ['less']

            static:
                files: pathsFromOpts(paths.web_frontend.static)
                tasks: ['copy:static']

            handlebars:
                files: [paths.web_frontend.handlebars.files.src]
                tasks: ['emberTemplates']

        couchMacro:
            database:
                src: [paths.database.files.dest + '**/*.js']
                baseDir: paths.database.files.dest

        clean:
            dist: ['dist/*']
            spec: [
                paths.database.specs.dest + '*'
                paths.web_frontend.specs.dest
            ]

    #
    # A task which uses grunt-text-replace to include common code in couch
    # design documents.
    #
    grunt.registerMultiTask 'couchMacro', ->
        path        = require 'path'
        textReplace = require 'grunt-text-replace/lib/grunt-text-replace'

        baseDir     = @data.baseDir
        cache       = {}

        replacer = (_word, _idx, _text, matches) ->
            filename = path.join(baseDir, matches[0])
            grunt.log.writeln("Using macro #{filename}")

            if cache[filename]?
                cache[filename]
            else
                try
                    cache[filename] = grunt.file.read(filename)
                catch error
                    grunt.log.error "Failed to read #{filename}"
                    throw grunt.util.error "Missing file", error

        textReplace.replace
            src:            @data.src
            overwrite:      true
            replacements:   [
                from: /^\s+\/\* \!code (.+?\.js)\*\/\s*$/mg
                to:   replacer
            ]

    grunt.registerTask 'buildFrontend', [
        'copy'
        'coffeelint:app'
        'coffee:app'
        'less'
        'emberTemplates'
    ]

    grunt.registerTask 'test', [
        'coffeelint'
        'coffee'
        'jasmine'
    ]

    grunt.registerTask 'buildDatabase', [
        'coffeelint:database'
        'coffee:database'
        'coffee:databaseSpec'
        'couchMacro:database'
        'jasmine_node'
        'mkcouchdb'
        'couchapp'
    ]

    grunt.registerTask 'default', ['buildFrontend']

    grunt.registerTask 'build', [
        'buildFrontend'
        'buildDatabase'
    ]
