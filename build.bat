@echo on

rem System dependend paths to the JDK
set JAVA_HOME=C:\Programme\jdk14205
set PATH=%JAVA_HOME%\bin

del ProjectX.jar

mkdir build

javac.exe -O -classpath src -d build @sources.lst

pause

copy resources\*.* build

jar.exe cfvm ProjectX.jar MANIFEST.MF -C build .
