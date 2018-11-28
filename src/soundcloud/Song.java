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
    String title;
    String Singer;
    boolean premium;

    public Song(int song_id, String title, String Singer, boolean is_premium) {
        this.song_id = song_id;
        this.title = title;
        this.Singer = Singer;
        this.premium = is_premium;
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
