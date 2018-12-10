/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package afra_sound_cloud;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;


class DBConnection
{ 
    Connection c= null;
    ResultSet rs=null;
    String sql=null;
    Statement s=null;
    PreparedStatement prestmt;
    private static DBConnection database_connection = null;
   
    public DBConnection(){
        String URL = "jdbc:derby://localhost:1527/soundcloud";
        String user = "ibra";
        String pass = "123";
        try
        {
            c = DriverManager.getConnection(URL, user, pass);
            s = c.createStatement();
            System.out.println("yes");
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();            
        }
    }
    
    public static DBConnection getInstance() 
    { 
        if (DBConnection.database_connection == null) 
            database_connection = new DBConnection();
        return database_connection; 
    } 
    
    void add_Song(Song s) throws SQLException
    {
          String sql = "insert into Song (ID,TITLE,SINGER,GENRE,DESCRIPTION,FILE_NAME,UPLOADER,URL) values(?,?,?,?,?,?,?,?)";
            PreparedStatement prestmt=c.prepareStatement(sql);
            prestmt.setInt(1, s.get_id());
            prestmt.setString(2, s.get_Title());
            prestmt.setString(3, s.get_Singer());
            prestmt.setString(4, s.get_Genre());
            prestmt.setString(5, s.get_Description());
            prestmt.setString(6, s.get_File_name());
            prestmt.setString(7, s.get_uploader());
            prestmt.setString(8, s.get_Url());
            prestmt.executeUpdate();
    }
    
    void add_album(Album a,User u)
    {
        String sql = "insert into Song (ALBUM_ID,SONG_ID,USER_ID) values(?,?,?)";
            PreparedStatement prestmt;
        try {
            prestmt = c.prepareStatement(sql);
            for(int i=0;i<a.get_album().size();i++){
                 prestmt.setInt(1,a.get_id());
                prestmt.setInt(2,a.get_album().get(i).get_id() );
                prestmt.setInt(3, u.get_ID());
                prestmt.executeUpdate();
            }
        } catch (SQLException ex) {
            //JOptionPane(null,ex);
        }
    }
    
    void runCommand()
    {
        String URL = "jdbc:derby://localhost:1527/soundcloud";
        String user = "ibra";
        String pass = "123";
        try
        {
            c = DriverManager.getConnection(URL, user, pass);
            s = c.createStatement();
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();            
        }
    }
    
    void InsertAccount(String nam, String email, String mobile, String pass, String date)
    {
        this.runCommand();
        try
        {
            String sql = "insert into Account (name, email, mobile_number, password, blocked_until) values(?,?,?,?,?)";
            PreparedStatement prestmt=c.prepareStatement(sql);
            prestmt.setString(1, nam);
            prestmt.setString(2, email);
            prestmt.setString(3, mobile);
            prestmt.setString(4, pass);
            prestmt.setString(5, date);
            prestmt.executeUpdate();
        }
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
    }
    
    void addSongToPlayList(int user_id,String playlist_id,int song_id)
    {
        String sql = "insert into Playlist (playlist,user_id,song_id) values(?,?,?)";
            PreparedStatement prestmt;
        try {
            prestmt = c.prepareStatement(sql);
            prestmt.setString(1, playlist_id);
            prestmt.setInt(2, user_id);
            prestmt.setInt(3, song_id);
            prestmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//    User getUser(int id)
//    {
//        this.runCommand();
//        User u = null;
//        try
//        {
//            sql = "select * from Users where UserID=" + id ;
//            prestmt = c.prepareStatement(sql);
//            rs = prestmt.executeQuery();
//            rs.next();
//            System.out.println(rs.getRow());
//            int account_id = rs.getInt("Account_ID");
//            
//            sql = "SELECT * from Account where ID = " + account_id ;
//            prestmt = c.prepareStatement(sql);
//            rs = prestmt.executeQuery();
//            rs.next();
//            String name = rs.getString("name");
//            String email = rs.getString("email");
//            String mobile = rs.getString("mobile_number");
//            String passwd = rs.getString("password");
//            String blocked_until = rs.getString("blocked_until");
//            u = new User(name, email, mobile, passwd, blocked_until);    
//        }
//        catch (SQLException ex) 
//        {
//            ex.printStackTrace();
//        }
//        if (u !=null )
//        {
//            try
//            {
//                sql = "SELECT * from UserPlaylist where user_id = " + id;
//                prestmt = c.prepareStatement(sql);
//                rs = prestmt.executeQuery();
//
//                ResultSet x = null;
//                ResultSet ss = null;
//                ArrayList<PlayList> user_playlist = new ArrayList<PlayList>();
//
//                int song_id=0;
//                int playlist_id=0;
//
//                if (!rs.next()) 
//                {                            //if rs.next() returns false
//                    System.out.println("No playlists found");
//                }
//                else 
//                {
//
//                    do 
//                    {
//                        //for each playlist, do the next step 
//                        String playlist_name = rs.getString("playlist_name");
//                        PlayList a = new PlayList(playlist_name);
//                        playlist_id = rs.getInt("playlist_id");
//                        sql = "SELECT * from Playlist where playlist_id = " + playlist_id;
//                        prestmt = c.prepareStatement(sql);
//                        x = prestmt.executeQuery();
//
//                        if (!x.next()) 
//                        {                           
//                            System.out.println("No songs found for playlist");
//                        }
//                        else 
//                        {
//                            do 
//                            {
//                                song_id = x.getInt("song_id");
//                                sql = "SELECT * from Songs where song_id = " + song_id;
//                                prestmt = c.prepareStatement(sql);
//                                ss = prestmt.executeQuery();
//                                ss.next();
//                                String added_by = ss.getString("added_by");
//                                String title = ss.getString("title");
//                                String Singer = ss.getString("singer");
//                                boolean premium = ss.getBoolean("premium");
//                                String path_to_file = ss.getString("path_to_file");
//                                Song s = new Song(u, song_id, title, Singer, premium, path_to_file);
//
//                                //added the song to playlist
//                                a.addSong(s);
//                            } while (x.next());
//                        }
//                        user_playlist.add(a);
//                    } while (rs.next());
//                }
//                u.setUser_playlists(user_playlist);
//            }
//            catch (SQLException ex) 
//            {
//                ex.printStackTrace();
//            }
//        }        
//        return u;
//    }
//    boolean updateUser(User u)
//    {
//        this.runCommand();
//        String name = u.getName();
//        String email = u.getEmail();
//        String mob = u.getMobile_number();
//        String pass = u.getPassword();
//        String blocked_until = u.getBlocked_until();
//        try
//        {
//            sql = "update Account set password='" + pass + "' , blocked_until = '" + blocked_until + "' where email = '" + email + "'";
//            prestmt = c.prepareStatement(sql);
//            prestmt.executeUpdate();
//            return true;
//        }
//        catch (SQLException ex) 
//        {
//            ex.printStackTrace();
//        }
//        return false;
//    }
//    
//    boolean permanentlyDelete(int user_id)
//    {
//        //working on this function
//        this.runCommand();
//        try
//        {
//            //get account id
//            //delete playlists
//            sql = "select * from Users where userid=" + user_id ;
//            prestmt = c.prepareStatement(sql);
//            rs = prestmt.executeQuery();
//            rs.next();
//            int account_id = rs.getInt("account_id");
//            
//            /*
//            Iterator<PlayList> iterator = u.user_playlists.iterator();
//            PlayList temp = null;
//            for (;iterator.hasNext(); temp = iterator.next())
//            {
//                Iterator<Song> songiterator = temp.songslist.iterator();
//                Song s = null;
//                for (;songiterator.hasNext(); s = songiterator.next())
//                {
//                    this.deleteSong(u, s);
//                    songiterator.remove();
//                }
//                iterator.remove();
//            }
//            */
//
//            
//            //delete account
//            //delete from users        
//            //sql = "delete from Account where email = '" + email + "'";
//            prestmt = c.prepareStatement(sql);
//            prestmt.executeUpdate();
//            return true;
//        }
//        catch (SQLException ex) 
//        {
//            ex.printStackTrace();
//        }
//        return false;
//    }
//    void deletePlayList(User u, String playlist_name)
//    {
//        this.runCommand();
//        try
//        {
//            sql = "select * from UserPlaylist where playlist_name='" + playlist_name +"'";
//            prestmt = c.prepareStatement(sql);
//            rs = prestmt.executeQuery();
//            rs.next();
//            int playlist_id = rs.getInt("playlist_id");
//        
//            sql = "delete from Playlist where playlist_id =" + playlist_id ;
//            prestmt = c.prepareStatement(sql);
//            prestmt.executeUpdate();        
//        }
//        catch (SQLException ex) 
//        {
//            ex.printStackTrace();
//        }
//        try
//        {
//            sql = "delete from UserPlaylist where playlist_name = '" + playlist_name + "'" ;
//            prestmt = c.prepareStatement(sql);
//            prestmt.executeUpdate();
//        }
//        catch (SQLException ex) 
//        {
//            ex.printStackTrace();
//        }
//    }
//    public void deleteSong(User u, Song s)
//    {
//        this.runCommand();
//        try
//        {
//            sql = "delete from Songs where added_by = " + s.added_by + " and added_by = '"+ u.getEmail() +"'";
//            prestmt = c.prepareStatement(sql);
//            prestmt.executeUpdate();
//        }
//        catch (SQLException ex) 
//        {
//            ex.printStackTrace();
//        }
//    }
}