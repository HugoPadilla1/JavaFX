package ICs.IC3;

import java.io.Serializable;

/**
 * Class: Video
 * This class describes a subclass of Media called Video
 * 
 * Purpose: Contains the attributes specific to a type of Media called Video.
 */
public class Video extends Media implements Serializable
{
   private String definition;
   private String format;
   private String director;
   private String rating;
   private String genre;

   /**
    * Method:Video() Constructor method that accepts values for all the
    * attributes and sets them.
    *
    * @param idNumber
    * @param itemName
    * @param type
    * @param definition
    * @param format
    * @param director
    * @param rating
    * @param genre
    */
   public Video(int idNumber, String itemName, String type, String definition,
                String format, String director, String rating, String genre)
   {
      super(idNumber, itemName, type);
      this.definition = definition;
      this.format = format;
      this.director = director;
      this.rating = rating;
      this.genre = genre;
   }

   /**
    * Method:getDefinition() Getter method for the definition attribute
    * 
    * @return the definition
    */
   public String getDefinition()
   {
      return definition;
   }

   /**
    * Method:getDirector() Getter method for the director attribute
    * 
    * @return the director
    */
   public String getDirector()
   {
      return director;
   }

   /**
    * Method:getRating() Getter method for the rating attribute
    * 
    * @return the rating
    */
   public String getRating()
   {
      return rating;
   }

   /**
    * Method:getGenre() Getter method for the genre attribute
    * 
    * @return the genre
    */
   public String getGenre()
   {
      return genre;
   }

   /**
    * Method:toString() Converts the attributes of the class to a text readable
    * format.
    */
   @Override
   public String toString()
   {
      return "Video:\t" + super.toString() + " Video definition = " + definition
            + ", format = " + format + ", director = " + director + ", rating = "
            + rating + ", genre = " + genre;
   }

   /**
    * Method:getFormat() Getter method for the format attribute
    * 
    * @return the format
    */
   public String getFormat()
   {
      return format;
   }
}
