/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package afra_sound_cloud;

import static afra_sound_cloud.Song_Page.showSongPage;
import static afra_sound_cloud.test.showUploadMenu;
import static afra_sound_cloud.test.unshowUploadMenu;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 *
 * @author Afrasiyab
 */
public class Afra_Sound_Cloud {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException, FileNotFoundException {
        
       
        
        
       
        // TODO code application logic here
        
        showUploadMenu();
        
        // jdbc d = new jdbc();
        
       // DBConnection d = new DBConnection();
        /*Song s= new Song("a","b","c","d","e","f","g");
        User u = new User("a","b","c","d");
        PlayList p = new PlayList("MyPLAYLIST");
        p.addSong(s);
        PlayList p1 = new PlayList("YOURPLAYLIST");
        p1.addSong(s);
        u.addPlayList(p);
        u.addPlayList(p1);
        
        d.add_Song(s);
        showSongPage(s,u);*/
        
        //showUploadMenu(args);
        
        /*FileInputStream fis = new FileInputStream("Music\\despacito.mp3");
        Player playMP3;
        try {
            playMP3 = new Player(fis);
            playMP3.play();
        } catch (JavaLayerException ex) {
            Logger.getLogger(Afra_Sound_Cloud.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
        
        //java.awt.Desktop.getDesktop().browse(java.net.URI.create("https://soundcloud.com/cokestudio/ya-qurban-khumariyaan-coke-studio-season-11-episode-7"));
        
        /*Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection selection = new StringSelection("yesyesyes");
        clipboard.setContents(selection, selection);*/
        
    
    }  
}
