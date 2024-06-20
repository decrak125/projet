package util;
import java.util.HashMap;
public class ModelView {
    String destinationURL;
    HashMap<String, Object> data=new HashMap<String, Object>();

    public String getDestinationURL() {
        return destinationURL;
    }
    public void setDestinationURL(String destinationURL) {
        this.destinationURL = destinationURL;
    }
    public HashMap<String, Object> getData() {
        return data;
    }
    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    public ModelView () 
    {

    }
    public ModelView (String url) 
    {
        this.destinationURL=url;
        HashMap<String, Object> data=new HashMap<String, Object>();
    }

    public void addObject (String key, Object value) 
    {
        this.data.put(key, value);
    }
}
