

module.exports = (grunt) ->

    grunt.initConfig
        coffee:
            compile:
                files: [
                    {
                        src: [
                            'web/src/coffee/*.coffee'
                            'web/src/coffee/models/*.coffee'
                        ]
                        dest: 'dist/web/js/quill.js'
                    }
                ]
        copy:
            main:
                files: [
                    {
                        cwd: 'web/static/'
                        src: ['**']
                        dest: 'dist/web/'
                        filter: 'isFile'
                        expand: true
                    }
                ]
        less:
            dev:
                options:
                    paths: ["web/src/less"]
                files: [
                    {
                        src: 'web/src/less/quill.less'
                        dest: 'dist/web/css/quill.css'
                    }
                ]

        watch:
            options:
                spawn: false
                livereload: true
            coffee:
                files: 'web/src/coffee/**'
                tasks: ['coffee']

            less:
                files: 'web/src/less/*.less'
                tasks: ['less']

            static:
                files: 'web/static/**'
                tasks: ['copy']


    grunt.loadNpmTasks 'grunt-contrib-coffee'
    grunt.loadNpmTasks 'grunt-contrib-copy'
    grunt.loadNpmTasks 'grunt-contrib-less'
    grunt.loadNpmTasks 'grunt-contrib-watch'

    grunt.registerTask 'default',   [
                                     'copy'
                                     'coffee'
                                     'less'
                                    ]
