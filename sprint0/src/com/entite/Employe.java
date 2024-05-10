package com.entite;

public class Employe {
    String nom;
    String genre;
    int age;

    public String getGenre() {
        return this.genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    public void setAge(String age) {
        setAge(Integer.parseInt(age));
    }
   
    public Employe(String nom, String genre, String age){
        setNom(nom);
        setGenre(genre);
        setAge(age);
    }


}
