

module.exports = (grunt) ->

    grunt.initConfig
        coffee:
            compile:
                files: [
                    {
                        src: [
                            'web_frontend/src/coffee/*.coffee'
                            'web_frontend/src/coffee/models/*.coffee'
                        ]
                        dest: 'dist/web/js/quill.js'
                    }
                ]
            compileSpec:
               files: [
                    {
                        src: [ 'web_frontend/spec/coffee/*.coffee' ]
                        dest: 'web_frontend/spec/js/quillSpec.js'
                    }
               ]

        jasmine:
            coffee_test:
                src: 'dist/web/js/quill/js'
                options:
                    specs: 'web_frontend/spec/js/*.js'

        copy:
            main:
                files: [
                    {
                        cwd: 'web_frontend/static/'
                        src: ['**']
                        dest: 'dist/web/'
                        filter: 'isFile'
                        expand: true
                    }
                ]
        less:
            dev:
                options:
                    paths: ["web_frontend/src/less"]
                files: [
                    {
                        src: 'web_frontend/src/less/quill.less'
                        dest: 'dist/web/css/quill.css'
                    }
                ]

        # TODO: compare this to grunt-ember-handlebars
        emberTemplates:
            compile:
                options:
                    templateBasePath: "web_frontend/src/handlebars/"
                files: [
                    {
                        src: [
                            'web_frontend/src/handlebars/**/*.hbs'
                        ]
                        dest: 'dist/web/js/templates.js'
                    }
                ]

        watch:
            options:
                spawn: false
                livereload: true

            coffee:
                files: 'web_frontend/src/coffee/**'
                tasks: ['coffee', 'jasmine']

            less:
                files: 'web_frontend/src/less/*.less'
                tasks: ['less']

            static:
                files: 'web_frontend/static/**'
                tasks: ['copy']

            handlebars:
                files: [
                        'web_frontend/src/handlebars/**/*.hbs'
                ]
                tasks: ['emberTemplates']


    grunt.loadNpmTasks 'grunt-contrib-coffee'
    grunt.loadNpmTasks 'grunt-contrib-copy'
    grunt.loadNpmTasks 'grunt-contrib-less'
    grunt.loadNpmTasks 'grunt-contrib-watch'
    grunt.loadNpmTasks 'grunt-ember-templates'
    grunt.loadNpmTasks 'grunt-contrib-jasmine'

    grunt.registerTask 'default',   [
                                     'copy'
                                     'coffee:compile'
                                     'less'
                                     'emberTemplates'
                                    ]

    grunt.registerTask 'test',      [
                                     'coffee'
                                     'jasmine'
                                    ]
