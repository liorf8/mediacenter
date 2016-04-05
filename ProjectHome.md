the MediaCenter application was developped for a student research project at the DHBW Mannheim

## Dependencys / Thanks to ##
  * HSQLDB http://hsqldb.org/
  * SIMON http://dev.root1.de/projects/2/wiki
  * vlcj https://github.com/caprica/vlcj


## Requirements ##
  * Developed and compiled in Java 1.7 -> only works with Java 1.7 or higher.
  * VLC (http://www.videolan.org/vlc/) needs to be installed:
    * If you run the application with a 64bit VM, a 64bit VLC installation has to be present.
    * If you run the application with a 32bit VM, a 32bit VLC installation has to be present.
    * You can simply install both 32bit VLC and 64bit VLC


## Try it ##
  * The server jar has no GUI, so run it from your shell (e.g. for Windows: cmd.exe) via `java -jar <path_to_jar>` to view standard output and interact
  * The client jar has a GUI, so simply open it with your JRE
  * The server will create a directory named "mediacenter" in your HOME\_DIR, which contains the database (HSQLDB) and the user files


## Screenshots ##
http://code.google.com/a/eclipselabs.org/p/mediacenter/wiki/Screenshots