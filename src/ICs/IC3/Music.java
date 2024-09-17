package ICs.IC3;

import java.io.Serial;
import java.io.Serializable;

/**
 * Class: Music
 * This class describes a subclass of Media called Music
 * 
 * Purpose: Contains the attributes specific to a type of Media called Music.
 */
public class Music extends Media implements Serializable
{
   private String format;
   private String artist;

   /**
    * Method:Music()
    * Constructor method that accepts values for all the attributes and sets them.
    * 
    * @param idNumber
    * @param itemName
    * @param type
    * @param format
    * @param artist
    */
   public Music(int idNumber, String itemName, String type, String format,
         String artist)
   {
      super(idNumber, itemName, type);
      this.format = format;
      this.artist = artist;
   }

   /**
    * Method:toString() Converts the attributes of the class to a text readable
    * format.
    */
   @Override
   public String toString()
   {
      return "Music:\t" + super.toString() + " Music format = " + format + ", artist = " + artist;
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

   /**
    * Method:getArtist() Getter method for the artist attribute
    * 
    * @return the artist
    */
   public String getArtist()
   {
      return artist;
   }

}
