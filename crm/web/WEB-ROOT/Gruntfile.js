module.exports = function(grunt) {

    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        //合并文件
        concat: {
            options: {
                // 定义一个用于插入合并输出文件之间的字符
                separator: ''
            },

            //合并JS文件
            js: {
                // 将要被合并的文件
                src: ['js/*.js', '!js/bootstrap.min.js'],
                // 合并后的JS文件的存放位置
                dest: 'release/<%= pkg.name %>.one.js'
            },

            //合并css文件
            css: {
                // 将要被合并的文件
                src: ['!css/bootstrap.min.css', '!css/bootstrap-theme.min.css', 'css/*.css'],
                // 合并后的JS文件的存放位置
                dest: 'release/app.one.css'
            }
        },

        //压缩（minify）JavaScript文件
        uglify: {
            options: {
                banner: '/*! <%= pkg.name %>.min.js <%= grunt.template.today("dd-mm-yyyy") %> */\n'
            },

            //测试版本
            debug: {
                files: {
                    'release/<%= pkg.name %>.one.js': ['<%= concat.js.dest %>']
                }
            },

            //发布版本
            release: {
                files: {
                    'release/app.min.js': ['<%= concat.js.dest %>']
                }
            }
        },

        // 压缩指定的css的文件到指定目录
        // npm install grunt-contrib-cssmin --save-dev
        cssmin: {
            options: {
                banner: '/*! <%= pkg.name %>.min.css <%= grunt.template.today("dd-mm-yyyy") %> */\n'
            },

            debug: {
                files: {
                    'release/one.css': ['<%= concat.css.dest %>']
                }
            },

            release: {
                files: {
                    'release/app.min.css': ['<%= concat.css.dest %>']
                }
            }
        },

        qunit: {
            files: ['test/**/*.html']
        },

        //css样式和css语法检测
        jshint: {
            //files: ['Gruntfile.js', 'js/*.js', 'css/*.css'],
            files: ['js/*.js', 'css/*.css', '!css/bootstrap.min.css', '!css/bootstrap-theme.min.css', '!js/bootstrap.min.js'],
            options: {
                // options here to override JSHint defaults
                // '-W015'         : true,

                jshintrc        : '.jshintrc',
                reporterOutput  : '',
                smarttabs       : true,
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
            // tasks: ['jshint', 'qunit']
            tasks: ['jshint']
        }
    });

    grunt.loadNpmTasks('grunt-contrib-cssmin');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-qunit');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-concat');

    grunt.registerTask('test', ['jshint', 'qunit']);

    grunt.registerTask('default', ['jshint', 'qunit', 'concat', 'uglify', 'cssmin']);

};