package org.example;
import org.Models.Movie;
import org.Models.User;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileWriteHandler {
    String filePath = "Outputs/recommendations.txt";
    String content = "";
    ArrayList<User> users;
    public  FileWriteHandler( ArrayList<User> users)
    {
        this.users = users;
        for(User u : users)
        {
            content += u.getName()+", "+u.getId()+"\n";
            for(Movie m : u.getLikedMovies())
            {
                content += m.getTitle()+", ";
            }
        }
    }

    void wrtie()
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
