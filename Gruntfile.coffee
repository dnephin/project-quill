

module.exports = (grunt) ->

    grunt.initConfig
        coffee:
            compile:
                files: [
                    {
                        src: ['web/src/coffee/*.coffee']
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

    grunt.loadNpmTasks 'grunt-contrib-coffee'
    grunt.loadNpmTasks 'grunt-contrib-copy'

    grunt.registerTask 'default',   ['copy', 'coffee']
