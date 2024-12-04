package Midterm;
/**
 * Class: Midterm.VideoGame.java
 * This class is the object class for question 1 of the mid-term. It stores information for a Person
 */
import java.io.Serializable;
public class VideoGame implements Comparable<VideoGame>, Serializable {
    private String name;
    private String genre;
    private char rating;
    private int recAge;

    /**    Method : Midterm.VideoGame
     *      No argument constructor
     */
    public VideoGame() {
        name = "Not set";
        genre = "Not set";
        rating = 'N';
        recAge = 0;
    }

    /** Method: Midterm.VideoGame
     *  Multi-argument constructor
     * @param name
     * @param genre
     * @param rating
     * @param recAge
     */
    public VideoGame(String name, String genre, char rating, int recAge) {
        this.name = name;
        this.genre = genre;
        this.rating = rating;
        this.recAge = recAge;
    }

    /** Method: getName
    * @return name
    */
    public String getName() {
        return name;
    }

    /** Method: setName
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /** Method: getGenre
     * @return genre
     */
    public String getGenre() {
        return genre;
    }

    /** Method: setGenre
     * @param genre
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /** Method: getRating
     * @return rating
     */
    public char getRating() {
        return rating;
    }

    /** Method: setRating
     * @param rating
     */
    public void setRating(char rating) {
        this.rating = rating;
    }

    /** Method: getRecAge
     * @return recAge
     */
    public int getRecAge() {
        return recAge;
    }

    /** Method: setRecAge
     * @param recAge
     */
    public void setRecAge(int recAge) {
        this.recAge = recAge;
    }

    /** Method: toString
     */
    @Override
    public String toString() {
        return String.format(
                "Midterm.VideoGame: name %-15s genre: %-20s last name: %-4s recommended age: %-4s",
                name, genre, rating, recAge);
    }

    /** Method: compareTo
        I noticed on regular sorts that T was prioritized over M, while this may be true for alphabetical order,
        when referring to game ratings Teen is before Mature, so I wanted to make sure that was accounted for.
     */
    @Override
    public int compareTo(VideoGame other) {
        // Custom comparison for rating to prioritize 'T' over 'M'
        if (this.rating == 'T' && other.rating == 'M') {
            return -1;  // 'T' comes before 'M'
        } else if (this.rating == 'M' && other.rating == 'T') {
            return 1;   // 'M' comes after 'T'
        } else if (this.rating != other.rating) {
            return Character.compare(this.rating, other.rating);  // Compare the ratings as usual for other cases
        }

        // Compare by genre if ratings are the same
        int genreCompare = this.genre.compareTo(other.genre);
        if (genreCompare != 0) return genreCompare;

        // Compare by name if ratings and genre are the same
        return this.name.compareTo(other.name);
    }
}
