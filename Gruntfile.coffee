#
# Gruntfile for web_frontend and database src
#


# TODO: better way to alias all the paths, there is lots of duplication
# web_frontend coffeescript src is referenced in a few places
coffee_src = 'web_frontend/src/coffee/**/*.coffee'

web_dist = 'dist/web'

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
    #response: buildCouch('response')


module.exports = (grunt) ->

    grunt.option 'stack', true

    grunt.initConfig
        coffee:
            compile:
                files: [
                    src: coffee_src
                    dest: "#{web_dist}/js/quill.js"
                ]
            compileSpec:
                options:
                    bare: true
                files: [
                    src: 'web_frontend/spec/coffee/*.coffee'
                    dest: 'web_frontend/spec/js/quillSpec.js'
                ]
            database:
                options:
                    bare: true
                files: [
                    cwd: 'database/src/'
                    src: ['**']
                    dest: 'dist/database/'
                    filter: 'isFile'
                    expand: true
                    ext: '.js'
                ]
            databaseSpec:
                options:
                    bare: true
                files: [
                    src: 'database/spec/coffee/**/*.coffee'
                    dest: 'database/spec/js/databaseSpec.js'
                ]

        coffeelint:
            options:
                arrow_spacing: 'error'
                cyclomatic_complexity:
                    level: 'error'
                    value: 9 # This may need to increase to 11
                line_endings: 'error'
                space_operators: 'error'
                indentation:
                    level: 'error'
                    value: 4

            app: [coffee_src, 'web_frontend/spec/coffee/*.coffee']
            build: 'Gruntfile.coffee'
            database: [
                'database/src/**/*.coffee'
                'database/spec/coffee/**/*.coffee'
            ]

        jasmine:
            coffeeTest:
                src: "#{web_dist}/js/quill/js"
                options:
                    specs: 'web_frontend/spec/js/*.js'
            databaseTest:
                src: 'dist/database/**/*.js'
                options:
                    specs: 'database/spec/js/*.js'

        copy:
            static:
                files: [
                    cwd: 'web_frontend/static/'
                    src: ['**']
                    dest: web_dist
                    filter: 'isFile'
                    expand: true
                ]
            bower:
                files: [
                    { src: [
                        'web_frontend/bower/bootstrap/dist/js/bootstrap.js'
                        'web_frontend/bower/ember/ember.js'
                        'web_frontend/bower/ember-data-shim/ember-data.js'
                        'web_frontend/bower/jquery/jquery.js'
                        'web_frontend/bower/handlebars/handlebars.js'
                    ]
                    dest: "#{web_dist}/js/"
                    expand: true
                    flatten: true }
                    { src: [
                        'web_frontend/bower/bootstrap/dist/css/bootstrap.css'
                        'web_frontend/bower/bootstrap/dist/css/' +
                            'bootstrap-theme.css'
                    ]
                    dest: "#{web_dist}/css/"
                    expand: true
                    flatten: true }
                ]
        less:
            dev:
                options:
                    paths: ['web_frontend/src/less']
                files: [
                    src: 'web_frontend/src/less/*.less'
                    dest: "#{web_dist}/css/quill.css"
                ]

        # TODO: compare this to grunt-ember-handlebars
        emberTemplates:
            compile:
                options:
                    templateBasePath: 'web_frontend/src/handlebars/'
                files: [
                    src: 'web_frontend/src/handlebars/**/*.hbs'
                    dest: "#{web_dist}/js/templates.js"
                ]

        mkcouchdb: databases

        couchapp: databases

        watch:
            options:
                spawn: false
                livereload: true

            coffee:
                files: coffee_src
                tasks: [
                    'coffeelint:app'
                    'coffee:compile'
                    'coffee:compileSpec'
                    'jasmine:coffeeTest'
                ]

            self:
                files: 'Gruntfile.coffee'
                tasks: ['coffeelint:build']

            database:
                files: [
                    'database/src/**/*.coffee'
                    'database/spec/**/*.coffee'
                ]
                tasks: [
                    'coffeelint:database'
                    'coffee:database'
                    'coffee:databaseSpec'
                    'jasmine:databaseTest'
                ]

            less:
                files: 'web_frontend/src/less/*.less'
                tasks: ['less']

            static:
                files: 'web_frontend/static/**'
                tasks: ['copy']

            handlebars:
                files: ['web_frontend/src/handlebars/**/*.hbs']
                tasks: ['emberTemplates']

        clean:
            dist: ['dist/*']
            spec: ['database/spec/js/*', 'web_frontend/spec/js/*']


    grunt.loadNpmTasks 'grunt-contrib-clean'
    grunt.loadNpmTasks 'grunt-contrib-coffee'
    grunt.loadNpmTasks 'grunt-contrib-copy'
    grunt.loadNpmTasks 'grunt-contrib-jasmine'
    grunt.loadNpmTasks 'grunt-contrib-less'
    grunt.loadNpmTasks 'grunt-contrib-watch'
    grunt.loadNpmTasks 'grunt-ember-templates'
    grunt.loadNpmTasks 'grunt-coffeelint'
    grunt.loadNpmTasks 'grunt-couchapp'

    grunt.registerTask 'buildFrontend', [
        'copy'
        'coffeelint:app'
        'coffee:compile'
        'less'
        'emberTemplates'
    ]

    grunt.registerTask 'test', [
        'coffeelint'
        'coffee'
        'jasmine'
    ]

    grunt.registerTask 'buildCouchdb', [
        'coffeelint:database'
        'coffee:database'
        'coffee:databaseSpec'
        'jasmine:databaseTest'
        'mkcouchdb'
        'couchapp'
    ]

    grunt.registerTask 'default', ['buildFrontend']

    grunt.registerTask 'build', [
        'buildFrontend'
        'buildCouchdb'
    ]
