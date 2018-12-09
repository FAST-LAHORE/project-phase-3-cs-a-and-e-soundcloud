/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soundcloud;

/**
 *
 * @author Ibrahim
 */
public class Song 
{
    int song_id;
    int added_by;
    String title;
    String Singer;
    boolean premium;
    String path_to_file;

    public Song(int song_id, int added_by, String title, String Singer, boolean premium, String path_to_file) {
        this.song_id = song_id;
        this.added_by = added_by;
        this.title = title;
        this.Singer = Singer;
        this.premium = premium;
        this.path_to_file = path_to_file;
    }

    
    public String getTitle() {
        return title;
    }

    public int getAdded_by() {
        return added_by;
    }

    
    public int getSong_id() {
        return song_id;
    }

    public String getSinger() {
        return Singer;
    }

    public boolean is_premium() {
        return premium;
    }
    
}
