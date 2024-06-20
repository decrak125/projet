@echo off

rem Variable fixe
set webapps="C:\Program Files\Apache Software Foundation\Tomcat 11.0\webapps"
set temp="temp"
set src="src"
set lib="lib"
set web="web"
set xml="xml"

rem Paramètres
set "appName=%1"

if "%appName%"=="" (
    echo Erreur: Veuillez entrer le nom du projet 
    goto :fin
)

if exist "%temp%" (
    rmdir /s /q "%temp%"
)

mkdir "%temp%/WEB-INF/lib"
mkdir "%temp%/WEB-INF/classes"

javac -d bin src/*.java
copy "%xml%" "%temp%/WEB-INF/"
copy "%lib%" "%temp%/WEB-INF/lib"
copy "%web%" "%temp%/"
Xcopy bin "%temp%/WEB-INF/classes" /E /H /C /I /Y

jar cvf "%temp%/WEB-INF/lib/"%appName%.jar -C bin .

Xcopy %temp% %webapps%\%appName%\ /E /H /C /I /Y