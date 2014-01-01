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
                dest: 'dist/web/js/'
                expand: true
                flatten: true
            css:
                src: [
                    'web_frontend/bower/bootstrap/dist/css/bootstrap.css'
                    'web_frontend/bower/bootstrap/dist/css/' +
                        'bootstrap-theme.css'
                ]
                dest: 'dist/web/css/'
                expand: true
                flatten: true

    database:
        host: 'http://localhost:5984'
        names: [
            'statement'
            'label'
            'user'
            'feedback'
        ]

        files:
            cwd:        'database/src/'
            src:        ['**/*.coffee']
            dest:       'dist/database/'
            expand:     true
            ext:        '.js'

        specs:
            cwd:        'database/spec/'
            src:        ['**/*.coffee']
            dest:       'dist/database/spec/'
            expand:     true
            ext:        '.js'


#
# Return a list of file paths from a file options mapping
#
pathsFromOpts = (opts) ->
    opts.cwd + src for src in opts.src

#
# Build a mapping for couch-compile from a list of databases
#
buildCouchCompile = (databases) ->
    _ = require('grunt').util._

    buildFiles = (db) ->
        files: [
            src:  "#{paths.database.files.dest}#{db}/app.js"
            dest: "#{paths.database.files.dest}#{db}/app.json"
        ]

    _.object([db, buildFiles(db)] for db in databases)

#
# Return the url for a database
#
databaseUrl = (database) ->
    "#{paths.database.host}/#{database}"

#
# Build a mapping for couch-push from a list of databases
#
buildCouchPushFiles = (databases) ->

    buildFiles = (db) ->
        src:  "#{paths.database.files.dest}#{db}/app.json"
        dest: databaseUrl(db)

    buildFiles(db) for db in databases


module.exports = (grunt) ->

    require('jit-grunt') grunt,
        "couch-compile":    'grunt-couch'
        "couch-push":       'grunt-couch'
        emberTemplates:     'grunt-ember-templates'
        jasmine_node:       'grunt-jasmine-node'
        replace:            'grunt-text-replace'

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
                    value: 8 # This may need to increase to 10
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
            databaseSpec:
                projectRoot: '/dev/null'
                specFolders: [paths.database.specs.dest]
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

        "couch-compile": buildCouchCompile(paths.database.names)

        "couch-push":
            localhost:
                files: buildCouchPushFiles(paths.database.names)

        couchMacro:
            database:
                src: [paths.database.files.dest + '**/*.js']
                baseDir: paths.database.files.dest

        watch:
            options:
                # TODO: why does jasmine run the wrong source when this is true
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
                tasks: ['buildDatabase']

            less:
                files: paths.web_frontend.less.src
                tasks: ['less']

            static:
                files: pathsFromOpts(paths.web_frontend.static)
                tasks: ['copy:static']

            handlebars:
                files: [paths.web_frontend.handlebars.files.src]
                tasks: ['emberTemplates']

        clean:
            dist: ['dist/*']
            spec: [paths.web_frontend.specs.dest]


    # TODO: publish as own repo
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
        'couch'
    ]

    grunt.registerTask 'default', ['buildFrontend']

    grunt.registerTask 'build', [
        'buildFrontend'
        'buildDatabase'
    ]
