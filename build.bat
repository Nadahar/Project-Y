@echo off

set JAVA_HOME=C:\j2sdk1.4.2

del ProjectX.jar
javac.exe -O -classpath src -d build src\*.java src\edu\stanford\ejalbert\*.java
copy src\*.properties build
copy src\*.gif build
jar.exe cfvm ProjectX.jar MANIFEST.MF -C build .

pause
