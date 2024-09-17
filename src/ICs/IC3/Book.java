package ICs.IC3;

import java.io.Serializable;

/**
 * Class: Book
 * This class describes a subclass of Media called Book
 * 
 * Purpose: Contains the attributes specific to a type of Media called Book.
 */
public class Book extends Media implements Serializable
{
   private String format;
   private String author;

   /**
    * Method:Book()
    * Constructor method that accepts values for all the attributes and sets
    * them.
    * 
    * @param idNumber
    * @param itemName
    * @param type
    * @param format
    * @param author
    */
   public Book(int idNumber, String itemName, String type, String format,
         String author)
   {
      super(idNumber, itemName, type);
      this.format = format;
      this.author = author;
   }

   /**
    * Method:toString() Converts the attributes of the class to a text readable
    * format.
    */
   @Override
   public String toString()
   {
      return "Book:\t" + super.toString() + " Book format = " + format + ", author = " + author;
   }
}
