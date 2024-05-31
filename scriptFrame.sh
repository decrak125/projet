#!/bin/bash

#Chemin vers servlet-api.jar dans mon tomcat
    LIB_SERVLET="/Users/user/Documents/Pilote/servlet-api.jar"

# Vider dossier lib
    rm -r myFrame/lib/*;
    echo "lib vide"

#creation dossier temporaire lib
    TEMP_LIB="myFrame/temp"

#creation dossier lib
    if [ -d $TEMP_LIB ]; then
        rm -rf "$TEMP_LIB"
        echo "Répertoire temporaire existant supprimé."
    fi

    mkdir -p "$TEMP_LIB"
    echo "Repertoire temporaire créé."

#compilation class Framework
    javac -cp "$LIB_SERVLET" -d "$TEMP_LIB" myFrame/classes/*.java
    echo "Compilation terminée"    

#creation fichier .jar
    rm myFrame/lib/myFrame.jar
    jar -cfv myFrame.jar -C "$TEMP_LIB" .

#copie vers lib du fichier jar    
    mv myFrame.jar myFrame/lib/

#suppression du dossier temporaire
    rm -r "$TEMP_LIB" 

# Framework créé