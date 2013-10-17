#
# Gruntfile for web_frontend and database src
#


# web_frontend coffeescript src is referenced in a few places
coffee_src = 'web_frontend/src/coffee/**/*.coffee'


buildCouch = (name) ->
    couchdb_url = 'http://localhost:5984'

    db: "#{couchdb_url}/#{name}"
    app: "dist/database/#{name}/app.js"
    options:
        okay_if_exists: true
        okay_is_missing: true


databases =
    statement: buildCouch('statement')
    #label: buildCouch('label')
    #response: buildCouch('response')


module.exports = (grunt) ->

    grunt.initConfig
        coffee:
            compile:
                files: [
                    src: coffee_src
                    dest: 'dist/web/js/quill.js'
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
                src: 'dist/web/js/quill/js'
                options:
                    specs: 'web_frontend/spec/js/*.js'
            databaseTest:
                src: 'dist/database/**/*.js'
                options:
                    specs: 'database/spec/js/*.js'

        copy:
            main:
                files: [
                    cwd: 'web_frontend/static/'
                    src: ['**']
                    dest: 'dist/web/'
                    filter: 'isFile'
                    expand: true
                ]
        less:
            dev:
                options:
                    paths: ['web_frontend/src/less']
                files: [
                    src: 'web_frontend/src/less/quill.less'
                    dest: 'dist/web/css/quill.css'
                ]

        # TODO: compare this to grunt-ember-handlebars
        emberTemplates:
            compile:
                options:
                    templateBasePath: 'web_frontend/src/handlebars/'
                files: [
                    src: 'web_frontend/src/handlebars/**/*.hbs'
                    dest: 'dist/web/js/templates.js'
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
                    #'mkcouchdb',
                    #'couchapp'
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

    grunt.registerTask 'default',   [
                                     'copy'
                                     'coffeelint:app'
                                     'coffee:compile'
                                     'less'
                                     'emberTemplates'
                                    ]

    grunt.registerTask 'test',      [
                                     'coffeelint'
                                     'coffee'
                                     'jasmine'
                                    ]
