module.exports = function(grunt) {

    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        //合并文件
        concat: {
            options: {
                // 定义一个用于插入合并输出文件之间的字符
                separator: ';'
            },

            //合并JS文件
            jsconcat: {
                // 将要被合并的文件
                src: ['js/*.js'],
                // 合并后的JS文件的存放位置
                dest: 'release/<%= pkg.name %>.one.js'
            },

            //合并css文件
            cssconcat: {
                // 将要被合并的文件
                src: ['css/*.css'],
                // 合并后的JS文件的存放位置
                dest: 'release/<%= pkg.name %>.one.css'
            }
        },

        //压缩（minify）JavaScript文件
        uglify: {
            options: {
                banner: '/*! <%= pkg.name %> <%= grunt.template.today("dd-mm-yyyy") %> */\n'
            },

            jsconcat: {
                files: {
                    'release/<%= pkg.name %>.min.js': ['<%= jsconcat.dist.dest %>']
                }
            },

            cssconcat: {
                files: {
                    'release/<%= pkg.name %>.min.css': ['<%= cssconcat.dist.dest %>']
                }
            }
        },
        qunit: {
            files: ['test/**/*.html']
        },

        jshint: {
            files: ['Gruntfile.js', 'js/*.js', 'css/*.css'],
            options: {
                // options here to override JSHint defaults
                jshintrc        : '.jshintrc',
                reporterOutput  : '',
                globals: {
                    jQuery      : true,
                    console     : true,
                    module      : true,
                    document    : true
                }
            }
        },
        watch: {
            files: ['<%= jshint.files %>'],
            tasks: ['jshint', 'qunit']
        }
    });

    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-qunit');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-concat');

    grunt.registerTask('test', ['jshint', 'qunit']);

    grunt.registerTask('default', ['jshint', 'qunit', 'concat', 'uglify']);

};