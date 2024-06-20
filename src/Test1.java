package controller;
import java.time.LocalDate;
import annotation.Controller;
import annotation.Get;
import annotation.Param;
import util.ModelView;
import java.time.LocalDate;
@Controller
public class Test1 {
    public Test1 () {

    }

    @Get(urlMapping = "/hello")
    public String testAgain (@Param(name="name") String name, @Param(name="firstName") String firstName,@Param(name="date") LocalDate date,@Param(name="test") String test) {
        return new String("Bonjour "+name+" "+firstName+" né le "+date+" / "+test);
    }

    @Get(urlMapping = "/call")
    public String callMe () {
        return "Ceci est une réussite";
    }

    @Get(urlMapping = "/calling")
    public void callMe2 () {
        return;
    }

    @Get(urlMapping = "/callAgain")
    public ModelView call () {
        ModelView modelView = new ModelView("/login.jsp");
        modelView.addObject("date", LocalDate.of(2004, 6, 23));
        return modelView;
    }

    // @Get(urlMapping = "/calls")
    public LocalDate calls () {
        ModelView modelView = new ModelView("/login.jsp");
        return LocalDate.of(2004, 6, 23);
    }

    public void type (int id) {
        System.out.println("Hello "+id);
    }
}
