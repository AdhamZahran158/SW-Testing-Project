package org.example;
import org.Models.Movie;
import org.Models.User;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileWriteHandler {
    String filePath = "D:\\JavaTest\\TextFiles\\Output.txt";
    String content = "";
    ArrayList<User> users;
    public  FileWriteHandler(ArrayList<User> users)
    {
        this.users = users;
        for(User u : users)
        {
            content += u.getName()+", "+u.getId()+"\n";
            ArrayList<String> recMovies = u.getRecMovies();
            if(!recMovies.isEmpty())
            {
                for(String rec : u.getRecMovies())
                {
                    content += rec +", ";
                }
                content = content.substring(0, content.length()-2);
            }
            content+="\n";
        }
    }
    public FileWriteHandler(String error)
    {
        this.content = error;
    }

    public void write()
    {
        try(FileWriter writer = new FileWriter(filePath)){
            writer.write(content);
            System.out.println("Done sucessfully");
        }
        catch(IOException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}
