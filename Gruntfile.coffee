#
# Gruntfile for web_frontend and database src
#


# web_frontend coffeescript src is referenced in a few places
coffee_src = 'web_frontend/src/coffee/**/*.coffee'


module.exports = (grunt) ->

    grunt.initConfig
        coffee:
            compile:
                files: [
                    src: coffee_src
                    dest: 'dist/web/js/quill.js'
                ]
            compileSpec:
                files: [
                    src: 'web_frontend/spec/coffee/*.coffee'
                    dest: 'web_frontend/spec/js/quillSpec.js'
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

            app: coffee_src
            build: 'Gruntfile.coffee'
            database: 'database/src/**/*.coffee'

        jasmine:
            coffee_test:
                src: 'dist/web/js/quill/js'
                options:
                    specs: 'web_frontend/spec/js/*.js'

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

        watch:
            options:
                spawn: false
                livereload: true

            coffee:
                files: coffee_src
                tasks: ['coffeelint:app', 'coffee', 'jasmine']

            self:
                files: 'Gruntfile.coffee'
                tasks: ['coffeelint:build']

            less:
                files: 'web_frontend/src/less/*.less'
                tasks: ['less']

            static:
                files: 'web_frontend/static/**'
                tasks: ['copy']

            handlebars:
                files: ['web_frontend/src/handlebars/**/*.hbs']
                tasks: ['emberTemplates']


    grunt.loadNpmTasks 'grunt-contrib-coffee'
    grunt.loadNpmTasks 'grunt-contrib-copy'
    grunt.loadNpmTasks 'grunt-contrib-less'
    grunt.loadNpmTasks 'grunt-contrib-watch'
    grunt.loadNpmTasks 'grunt-ember-templates'
    grunt.loadNpmTasks 'grunt-contrib-jasmine'
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
