@echo off

@REM rem definition du chemin
set TEMP_DIR=H:/S4WEB/Mr Naina/hello-spring/temp
set WEB_DIR=H:/S4WEB/Mr Naina/hello-spring/web
set LIB_DIR=H:/S4WEB/Mr Naina/hello-spring/lib
set XML_FILE=H:/S4WEB/Mr Naina/hello-spring/web.xml
set XML_FILE2=H:/S4WEB/Mr Naina/hello-spring/dispatcher-servlet.xml
set SRC_DIR=H:/S4WEB/Mr Naina/hello-spring/src
set APPLICATION=hello-spring
set DEPLOYMENT_DIR=G:/Ilaina/apache-tomcat-10/apache-tomcat-10/webapps
set TEMP_SRC=H:/S4WEB/Mr Naina/hello-spring/temp-src

@REM suppression du dossier temp
rmdir /s /q "%TEMP_DIR%"

@REM creation du dossier temp
mkdir "%TEMP_DIR%"
mkdir "%TEMP_SRC%"
mkdir "%TEMP_DIR%/WEB-INF"
mkdir "%TEMP_DIR%/WEB-INF/classes"
mkdir "%TEMP_DIR%/WEB-INF/lib"


@REM copie des dossier
xcopy /E "%WEB_DIR%" "%TEMP_DIR%"  
copy "%XML_FILE%" "%TEMP_DIR%/WEB-INF/"
copy "%XML_FILE2%" "%TEMP_DIR%/WEB-INF/"
xcopy /E "%LIB_DIR%" "%TEMP_DIR%/WEB-INF/lib"

@REM compilation de java

FOR /R "%SRC_DIR%" %%F IN (*.java) DO (
    copy "%%F" "%TEMP_SRC%"
)

cd "%TEMP_SRC%"
javac -sourcepath "%TEMP_SRC%" -d "%TEMP_DIR%/WEB-INF/classes" -cp "%TEMP_DIR%/WEB-INF/lib/*" *.java


@REM transformation en .war
jar -cvf "%APPLICATION%.war" -C "%TEMP_DIR%" .

@REM deployemnt vers le site web
copy "%APPLICATION%.war"  "%DEPLOYMENT_DIR%"


rem Arrêt de Tomcat s'il est en cours d'exécution
@REM call "C:\Program Files\Apache Software Foundation\Tomcat 10.1\bin\shutdown.bat"

rem Attente de quelques secondes pour permettre à Tomcat de s'arrêter
@REM timeout /t 5 /nobreak > nul

rem Démarrage de Tomcat
@REM "C:\Program Files\Apache Software Foundation\Tomcat 10.1\bin\startup.bat"

echo Tomcat a été redémarré.
