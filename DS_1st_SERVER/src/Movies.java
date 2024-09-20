/*Papadakis Fragkiskos
321/2017147
*/
import java.io.Serializable;
//klasi Movies tou client idia me to server
public class Movies implements Serializable {
//ulopoiei  tin diepafi Serializable  gia na mporei na perastei ws orisma stin methodo writeObject()
    private String movieName;
    private String director;
    private String genre;
    private int movieTime;
    private String description;

    public Movies(String movieName, String director, String genre, int movieTime, String description) { //constructor klasis movies
        this.movieName = movieName;
        this.director = director;
        this.genre = genre;
        this.movieTime = movieTime;
        this.description = description;
    }

    public String getDirector() {
        return this.director;
    }

    public String toString() {
        return "\n\nMovie: " + movieName + "\nDirector: " + director + "\nGenre: " + genre
                + "\nMovie Time: " + movieTime;
    }
}
