@echo off

set JAVA_HOME=C:\Programme\j2sdk1.4.2_02

del ProjectX.jar
javac.exe -O -classpath src;lib\commons-net-1.1.0\commons-net-1.1.0.jar src\*.java
jar.exe cfvm ProjectX.jar MANIFEST.MF -C src .

REM cd src
REM javac.exe -O *.java
REM jar.exe cfvm ..\ProjectX.jar MANIFEST.MF *.class
REM cd ..

pause
