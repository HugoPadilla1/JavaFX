package ICs.IC3;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class: Library
 * This class describes library used to contain the media item library.
 * It also contains the main method which starts the program.
 * 
 * Purpose: Methods and attributes needed to create a library of
 * Media class items. Print them, read them from a file,search for a
 * particular id and add a new item.
 */
public class Library
{

   // actual library data
   private ArrayList<Media> libraryItems = new ArrayList<Media>();
   private Scanner keyboard;
   @FXML
   TextArea ta;


   /**
    * Method:printLibraryItems()
    * 
    * This method returns a String of the library items contained in the libraryItems Array
    * list. It relies on the toString method of the various Media types to print
    * the items in a user friendly format.
    */
   private String printLibraryItems()
   {
      String libraryItemString = "";
      for (int i = 0; i < libraryItems.size(); i++)
      {
         Media temp = libraryItems.get(i);
         libraryItemString += temp.toString() + "\n";
      }
      return libraryItemString;
   }


   /**
    * Method:addItem()
    * This method adds the parameter m to the libraryItems array list
    * 
    * @param media
    */
   private void addItem(Media media)
   {
      libraryItems.add(media);
   }

   /**
    * Method:readFile()
    * This method reads the text file to get the information from the text file.
    *
    * This method must be modified to read the binary file.
    *
    * @return boolean
    */
   @FXML
   public void readFile() throws IOException {
      libraryItems.clear();
      ta.clear();
      boolean valid = true;
      // open text file
      File mediaFile = new File("src/ICs/IC3/media.txt");
      // open a Scanner to read data from File
      Scanner mediaReader = null;
         mediaReader = new Scanner(mediaFile);


      // read one Media at a time
      while (mediaReader != null && mediaReader.hasNext())
      {
         String category = mediaReader.nextLine();
         String name = mediaReader.nextLine();
         String idString = mediaReader.nextLine();
         int id = Integer.parseInt(idString);

         if (category.equalsIgnoreCase("music"))
         {
            String artist = mediaReader.nextLine();
            String format = mediaReader.nextLine();
            Music tp = new Music(id, name, category, format, artist);
            addItem(tp);

         } else if (category.equalsIgnoreCase("video"))
         {
            String def = mediaReader.nextLine();
            String format = mediaReader.nextLine();
            String director = mediaReader.nextLine();
            String genre = mediaReader.nextLine();

            String rating = mediaReader.nextLine();
            Video tp = new Video(id, name, category, def, format, director,
                    rating, genre);
            addItem(tp);

         } else if (category.equalsIgnoreCase("book"))
         {
            String author = mediaReader.nextLine();
            String format = mediaReader.nextLine();
            Book tp = new Book(id, name, category, format, author);
            addItem(tp);
         } else
         {
            throw new IOException("Unknown media type " + category);
         }
      }
      for (Media media : libraryItems) {
         ta.appendText(media.toString() + "\n");
      }

   }

   @FXML
   public void writeBinaryFile() throws IOException {
      ObjectOutputStream output = null;
      // Create an output stream for file temp.dat
         output = new ObjectOutputStream(new FileOutputStream(
                 "src/ICs.IC3/BinaryMedia.dat"));

      // write the six items to the file
         /*
          * first the video, video Star Wars 1 high definition DVD George
          * Lucas science fiction PG
          */
         output.writeUTF("video");
         output.writeUTF("Star Wars");
         output.writeInt(1);
         output.writeUTF("High Definition");
         output.writeUTF("DVD");
         output.writeUTF("George Lucas");
         output.writeUTF("science fiction");
         output.writeUTF("PG");

         /*
          * second video video Empire Strikes Back 4 high definition DVD
          * George Lucas science fiction PG
          */

         Video vid = new Video(4, "Empire Strikes Back", "video", "High Definition",
                 "DVD", "George Lucas", "PG", "Science fiction");
         output.writeObject(vid);

         /*
          * now the books book Object-Oriented Programming 2 Dr. Java Expert
          * digital
          *
          * book Object-Oriented Requirements Analysis 5 Dr. Java Expert
          * print
          */
         output.writeUTF("book");
         output.writeUTF("Object-Oriented Programming");
         output.writeInt(2);
         output.writeUTF("Dr. Java Expert");
         output.writeUTF("digital");

         Book bk = new Book(5, "Object-Oriented Requirements Analysis", "book",
                 "print", "Dr. Java Expert");
         output.writeObject(bk);

         /*
          * Now the music music Star Wars Soundtrack 3 John Williams CD
          *
          * music Empire Soundtrack 6 John Williams digital
          */
         Music music = new Music(3, "Star Wars Soundtrack", "music", "CD",
                 "John Williams");
         output.writeObject(music);

         output.writeUTF("music");
         output.writeUTF("Empire Soundtrack");
         output.writeInt(5);
         output.writeUTF("John Williams");
         output.writeUTF("digital");

         output.close();
   }

   @FXML
   public void readBinaryFile() throws IOException, ClassNotFoundException {
      libraryItems.clear();
      ta.clear();

      ObjectInputStream input = new ObjectInputStream(new FileInputStream("src/ICS.IC3/BinaryMedia.dat"));

      try{
         while(true){
            Object obj = input.readObject();
            if (obj instanceof Video){
               Video video = (Video) obj;
               addItem(video);
            } else if(obj instanceof Book){
               Book book = (Book) obj;
               addItem(book);
            } else if(obj instanceof Music){
               Music music = (Music) obj;
               addItem(music);
            }
         }
      } catch (IOException e){
         System.out.println(e);
      } finally {
         input.close();
      }

      for (Media media : libraryItems){
         ta.appendText(media.toString() + "\n");
      }
   }

   @FXML
   public void search() {
      try {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("search.fxml"));
         Stage diaStage = new Stage();
         GridPane grid = loader.load();
         diaStage.setTitle("Search");
         Scene scene = new Scene(grid);
         diaStage.setScene(scene);
         Search search = loader.getController();
         search.init(libraryItems);
         diaStage.showAndWait();
      } catch(IOException e) {

      }
   }

   @FXML
   public void exit() {
      Stage stage = (Stage) ta.getScene().getWindow();
      stage.close();
      Platform.exit();
   }
}
