package org.example;

import org.Models.Movie;
import org.Models.User;

import java.util.ArrayList;


public class RecommendationEngine {


    public void GetRecommendations(User user, ArrayList<Movie> movies)
    {
        ArrayList<Movie> likedMovies = user.getLikedMovies();
        ArrayList<String> RecMovies = new ArrayList<>();

        for(Movie lm:likedMovies)
        {
            for(Movie m:movies)
            {
                if (likedMovies.contains(m)) continue;

                for(String lgenre:lm.getGenres())
                {
                    for(String genre:m.getGenres())
                    {
                        if (lgenre.equals(genre))
                        {
                            if (!likedMovies.contains(m)) {
                                RecMovies.add(m.getTitle());
                            }
                            break;
                        }

                    }
                    if (RecMovies.contains(m)) break;
                }


            }
        }
        user.setRecMovies(RecMovies);

    }

}
