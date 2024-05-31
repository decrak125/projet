#!/bin/bash

#Emplacement de mon webapps dans tomcat
WEBAPPS_DIR="C:\Program Files\Apache Software Foundation\Tomcat 10.1\webapps"
FRAMEWORK_DIR="myFrame"
WEBINF_DIR="test/WEB-INF"
#Creation dossier de test
    if [ -d "$WEBAPPS_DIR\test" ]; then
        rm -rf "$WEBAPPS_DIR/test"
        echo "Répertoire test existant supprimé."
    fi
    mkdir -p "$WEBAPPS_DIR/test"
    echo "Répertoire test créé."

#Copie de la librairie de mon framework dans mon fichier de test
    rm -r "$WEBINF_DIR/lib/*"
    cp -r "$FRAMEWORK_DIR/lib" "$WEBINF_DIR"
    echo "copie lib framework terminee"

#Copie de mon WEB-INF dans le dossier test
    cp -r $WEBINF_DIR "$WEBAPPS_DIR/test/"
    echo "copie WEB-INF terminee"