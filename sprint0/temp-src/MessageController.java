package com.controller;

// import java.util.ArrayList;
import java.util.Vector;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

// import com.entite.Employe;
import com.entite.Etudiant;


@Controller
public class MessageController {

    @GetMapping("/hello")
    public ModelAndView message(){
        ModelAndView mv= new ModelAndView("message");
        mv.addObject("greeting", "Hello world");

        return mv;
    }

    // @GetMapping("/save")
    // public ModelAndView sauvegarde(){
    //     ModelAndView mv= new ModelAndView("/emp/page2");

    //     return mv;
    // }   
    @PostMapping("emp/save")
    public ModelAndView sauvegarde(@RequestParam("nom") String nom, 
                                @RequestParam("prenom") String prenom, 
                                @RequestParam("age") int age) {
        // Faire quelque chose avec nom, prenom et age
        ModelAndView mv = new ModelAndView("/emp/page2");
        
        // Vous pouvez également ajouter des objets à l'objet ModelAndView si vous en avez besoin dans votre vue.
        mv.addObject("nom", nom);
        mv.addObject("prenom", prenom);
        mv.addObject("age", age);
        
        return mv;
    }

    @GetMapping("/emp/list")
    public ModelAndView liste(){
        ModelAndView mv= new ModelAndView("/emp/list");

        // ArrayList<Employe> employes=new ArrayList<>();

        Vector<Etudiant> etudiant = new Vector<>();

        etudiant.add(new Etudiant("Rabanoson", "Tokinirina", 20));
        etudiant.add(new Etudiant("Rabe", "koto", 21));
        etudiant.add(new Etudiant("Rasoa", "Jeanne", 22));
        etudiant.add(new Etudiant("Ragidro", "singe", 19));

        mv.addObject("etudiant", etudiant);

        return mv;
    }
}
