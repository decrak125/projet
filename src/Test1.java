package controller;
import java.time.LocalDate;
import annotation.Controller;
import annotation.Get;
import util.ModelView;
@Controller
public class Test1 {
    public Test1 () {

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
}
