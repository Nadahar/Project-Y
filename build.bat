@echo on

rem System dependend paths to the JDK
set JAVA_HOME=C:\Programme\jdk14205
set PATH=%JAVA_HOME%\bin

del ProjectX.jar

mkdir build
cd build

del *.gif
del *.properties
del *.bin
del *.tbl
del edu\stanford\ejalbert\*.class
del net\sourceforge\dvb\projectx\*.class

cd..

javac.exe -O -classpath src -d build @sources.lst

pause

copy src\resources\*.* build

jar.exe cfvm ProjectX.jar MANIFEST.MF -C build .

pause
