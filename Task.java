/*
Java AT2.5
*/
package draganddropfinal;

import java.io.Serializable;

public class Task implements Serializable {
    
    private static final long serialVersionUID = 1L;
 
    private String name = "";
 
    public Task(String name)
    {
        this.name = name;
    }
 
    public String getName()
    {
        return name;
    }
 
    public void setName(String name)
    {
        this.name = name;
    }
 
    @Override
    public String toString()
    {
        return name;
    }
}
    

