#Dynamic Load Apk Framework

##dynamic load apk framework can load local apk file, start target apk activity, use resource and assets

##Thanks for [singwhatiwanna's dynamic-load-apk](https://github.com/singwhatiwanna/dynamic-load-apk)
##base of this:
##I add apk md5 check(if runtime change apk file, will cause ClassCastException even Same Class File)


#How To
##1.in your application, use PluginManager to load your apk file
##2.then use PluginManager startActivity to invoke dummy activity in apk file


#Know Issues
##1.in some devices, framework will do dexOpt action, this will cause class files different between your application and target apk libs fold.
## so, if appear this issue, do not compile with lib module, instead of add dynamic-load.jar in target apk libs
##2.now only support Activity, not support Service, ContentProvider(later will)