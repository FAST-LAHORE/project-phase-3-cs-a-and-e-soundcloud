/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soundcloud;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;


import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.*;
import javax.mail.internet.*;

class DBConnection
{ 
    Connection c= null;
    ResultSet rs=null;
    String sql=null;
    Statement s=null;
    PreparedStatement prestmt;
    private static DBConnection database_connection = null;
    private DBConnection(){}
    public static DBConnection getInstance() 
    { 
        if (DBConnection.database_connection == null) 
            database_connection = new DBConnection();
        return database_connection; 
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
    User getUser(int id)
    {
        this.runCommand();
        User u = null;
        try
        {
            sql = "select * from Users where UserID=" + id ;
            prestmt = c.prepareStatement(sql);
            rs = prestmt.executeQuery();
            rs.next();
            System.out.println(rs.getRow());
            
            int account_id = rs.getInt("account_id");
            
            sql = "SELECT * from Account where ID = " + account_id ;
            prestmt = c.prepareStatement(sql);
            rs = prestmt.executeQuery();
            rs.next();
            String name = rs.getString("name");
            String email = rs.getString("email");
            String mobile = rs.getString("mobile_number");
            String passwd = rs.getString("password");
            String blocked_until = rs.getString("blocked_until");
            u = new User(name, email, mobile, passwd, blocked_until);    
        }
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
        if (u !=null )
        {
            try
            {
                sql = "SELECT * from UserPlaylist where user_id = " + id;
                prestmt = c.prepareStatement(sql);
                rs = prestmt.executeQuery();

                ResultSet x = null;
                ResultSet ss = null;
                ArrayList<PlayList> user_playlist = new ArrayList<PlayList>();

                int song_id=0;
                int playlist_id=0;

                if (!rs.next()) 
                {                            //if rs.next() returns false
                    System.out.println("No playlists found");
                }
                else 
                {

                    do 
                    {
                        //for each playlist, do the next step 
                        String playlist_name = rs.getString("playlist_name");
                        PlayList a = new PlayList(playlist_name);
                        playlist_id = rs.getInt("playlist_id");
                        sql = "SELECT * from Playlist where playlist_id = " + playlist_id;
                        prestmt = c.prepareStatement(sql);
                        x = prestmt.executeQuery();

                        if (!x.next()) 
                        {                           
                            System.out.println("No songs found for playlist");
                        }
                        else 
                        {
                            do 
                            {
                                song_id = x.getInt("song_id");
                                sql = "SELECT * from Songs where song_id = " + song_id;
                                prestmt = c.prepareStatement(sql);
                                ss = prestmt.executeQuery();
                                ss.next();
                                int added_by = ss.getInt("added_by");
                                String title = ss.getString("title");
                                String Singer = ss.getString("singer");
                                boolean premium = ss.getBoolean("premium");
                                String path_to_file = ss.getString("path_to_file");
                                Song s = new Song(song_id, added_by, title, Singer, premium, path_to_file);

                                //added the song to playlist
                                a.addSong(s);
                            } while (x.next());
                        }
                        user_playlist.add(a);
                    } while (rs.next());
                }
                u.setUser_playlists(user_playlist);
            }
            catch (SQLException ex) 
            {
                ex.printStackTrace();
            }
        }
        Subscription s = this.getSubscription(id);
        u.updateTheSubscription(s);
        return u;
    }
    boolean updateUser(User u)
    {
        this.runCommand();
        String name = u.getName();
        String email = u.getEmail();
        String mob = u.getMobile_number();
        String pass = u.getPassword();
        String blocked_until = u.getBlocked_until();
        try
        {
            sql = "update Account set password='" + pass + "' , blocked_until = '" + blocked_until + "' where email = '" + email + "'";
            prestmt = c.prepareStatement(sql);
            prestmt.executeUpdate();
            return true;
        }
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
        return false;
    }
    boolean permanentlyDelete(int user_id)
    {
        User u = this.getUser(user_id);
        int account_id=0;
        this.runCommand();
        try
        {
            //get account id
            //delete playlists
            sql = "select * from Users where userid=" + user_id ;
            prestmt = c.prepareStatement(sql);
            rs = prestmt.executeQuery();
            rs.next();
            account_id = rs.getInt("account_id");
        }
        catch (SQLException ex) 
        {
            ex.printStackTrace();
            return false;
        }
        try
        {
            int playlist_id;
            String added_by;
            ResultSet x, y, z = null;
            sql = "select * from UserPlaylist where user_id=" + user_id ;
            prestmt = c.prepareStatement(sql);
            x = prestmt.executeQuery();
            if (!x.next()) 
            {                            //if rs.next() returns false
                System.out.println("No playlists found for user_id = " + user_id);
            }
            else 
            {
                do
                {
                    int song_id;
                    playlist_id = x.getInt("playlist_id");
                    sql = "select * from Playlist where playlist_id=" + playlist_id ;
                    prestmt = c.prepareStatement(sql);
                    y = prestmt.executeQuery();
                    if (!y.next()) 
                    {                            //if rs.next() returns false
                        System.out.println("No songs found in playlist");
                    }
                    else 
                    {
                        do
                        {
                            song_id = y.getInt("song_id");
                            sql = "select * from Songs where song_id=" + song_id ;
                            prestmt = c.prepareStatement(sql);
                            z = prestmt.executeQuery();
                            if (!z.next()) 
                            {
                                System.out.println("Song not found in database against id = " + song_id);
                            }
                            else 
                            {
                                do
                                {        
                                    added_by = z.getString("added_by");
                                    if (u.getEmail()==added_by)
                                    {
                                        sql = "delete from Songs where song_id = " + song_id ;
                                        prestmt = c.prepareStatement(sql);
                                        prestmt.executeUpdate();                                        
                                    }
                                } while (z.next());
                            }            
                        } while (y.next());
                    }
                    
                    sql = "delete from UserPlaylist where playlist_id = " + playlist_id ;
                    prestmt = c.prepareStatement(sql);
                    prestmt.executeUpdate();                                        

                } while (x.next());
            }
        }
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
        try
        {
            sql = "delete from Users where userid = " + user_id;
            prestmt = c.prepareStatement(sql);
            prestmt.executeUpdate();                                        

            sql = "delete from Account where id = " + account_id;
            prestmt = c.prepareStatement(sql);
            prestmt.executeUpdate();                                        
        
        }
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
        return true;
    }
    void deletePlayList(User u, String playlist_name)
    {
        this.runCommand();
        try
        {
            sql = "select * from UserPlaylist where playlist_name='" + playlist_name +"'";
            prestmt = c.prepareStatement(sql);
            rs = prestmt.executeQuery();
            rs.next();
            int playlist_id = rs.getInt("playlist_id");
        
            sql = "delete from Playlist where playlist_id =" + playlist_id ;
            prestmt = c.prepareStatement(sql);
            prestmt.executeUpdate();        
        }
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
        try
        {
            sql = "delete from UserPlaylist where playlist_name = '" + playlist_name + "'" ;
            prestmt = c.prepareStatement(sql);
            prestmt.executeUpdate();
        }
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
    }
    public void deleteSong(User u, Song s)
    {
        this.runCommand();
        try
        {
            sql = "delete from Playlist where song_id = " + s.getSong_id();
            prestmt = c.prepareStatement(sql);
            prestmt.executeUpdate();
    
            sql = "delete from Songs where added_by = " + s.added_by + " and added_by = '"+ u.getEmail() +"'";
            prestmt = c.prepareStatement(sql);
            prestmt.executeUpdate();
        }
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
    }
    public void addSubscription(){}
    public ArrayList<SubscriptionPackages> getSubPackages()
    {
        ArrayList<SubscriptionPackages> subpackages = new ArrayList<SubscriptionPackages>();
        this.runCommand();
        try
        {
            sql = "select * from SubscriptionPackages";
            prestmt = c.prepareStatement(sql);
            rs = prestmt.executeQuery();
            String package_name;
            double price;
            if (!rs.next()) 
            {                            //if rs.next() returns false
                System.out.println("No subscription packages exist");
            }
            else 
            {
                do
                {
                    package_name = rs.getString("package_name");
                    price = rs.getDouble("price");
                    SubscriptionPackages sp = new SubscriptionPackages(package_name, price);
                    subpackages.add(sp);
                }while(rs.next());
            }
        }        
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
        return subpackages;
    }
    public ResultSet getSubscriptionPackages()
    {
        this.runCommand();
        try
        {
            sql = "select * from SubscriptionPackages";
            prestmt = c.prepareStatement(sql);
            rs = prestmt.executeQuery();
        }
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
        return rs;
    }
    public int getUserID(User u)
    {
        int userid = 0;
        try
        {
            sql = "select * from Account where email='" + u.getEmail() + "'";
            prestmt = c.prepareStatement(sql);
            rs = prestmt.executeQuery();
            rs.next();
            int account_id = rs.getInt("id");
            
            sql = "select * from Users where account_id=" + account_id;
            prestmt = c.prepareStatement(sql);
            rs = prestmt.executeQuery();
            rs.next();
            userid = rs.getInt("userid");
            
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        return userid;
    }
    public Subscription getPastUserSubscriptions(User u)
    {
        Subscription sub = null;
        int user_id = this.getUserID(u);
        try
        {
            sql = "select * from UserSubscriptions where user_id = " + user_id;
            prestmt = c.prepareStatement(sql);
            rs = prestmt.executeQuery();
            rs.next();
            String package_name = rs.getString("package_name");
            String subscription_expiry = rs.getString("subscription_expiry");
            
            sql = "select * from SubscriptionPackages where package_name = '" + package_name + "'";
            prestmt = c.prepareStatement(sql);
            rs = prestmt.executeQuery();
            rs.next();
            double price = rs.getDouble("package_name");
            SubscriptionPackages subpackage = new SubscriptionPackages(package_name,price);
            sub = new Subscription(user_id, subpackage, subscription_expiry);
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        return sub;
    }
    public boolean setUserSubscription(User u, SubscriptionPackages sp)
    {
        int user_id = this.getUserID(u);
        try
        {
            sql = "update UserSubscriptions "
                    + "set package_name='" + sp.getName() + "' , subscription_expiry = '" + u.sub.getSubscription_expiry() + 
                    "' where user_id = " + user_id;
            prestmt = c.prepareStatement(sql);
            prestmt.executeUpdate();
            System.out.println("try");   
            return true;
        }
        catch (SQLException ex) 
        {
            try
            {
                sql = "insert into UserSubscriptions(user_id, package_name, subscription_expiry) values("+ user_id +",'"+sp.getName()+"','"+u.sub.getSubscription_expiry()+"')" ;
                prestmt = c.prepareStatement(sql);
                prestmt.executeUpdate();
                System.out.println("catch");
                return true;
            }
            catch (SQLException sss) 
            {
                sss.printStackTrace();
            }
        }
        return false;       
    }
    public ResultSet getAllUsers()
    {
        rs = null;
        this.runCommand();
        User u = null;
        try
        {
            sql = "select users.userid, account.name, account.email, account.mobile_number, account.blocked_until "
                + "from Users "
                + "join Account "
                + "on Users.account_id = Account.id";                    
            //System.out.println(sql);
            prestmt = c.prepareStatement(sql);
            rs = prestmt.executeQuery();
        }
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
        
        return rs;
    }
    public Subscription getSubscription(int user_id)
    {
        this.runCommand();
        Subscription s = null;
        sql = "select UserSubscriptions.user_id, UserSubscriptions.package_name, UserSubscriptions.subscription_expiry, subscriptionpackages.price "
                + "from UserSubscriptions "
                + "join subscriptionpackages "
                + "on UserSubscriptions.package_name = subscriptionpackages.package_name";
        try
        {
            prestmt = c.prepareStatement(sql);
            rs = prestmt.executeQuery();
            if (rs.next())
            {
                String name = rs.getString("package_name");
                double price = rs.getDouble("price");
                String subscription_expiry = rs.getString("subscription_expiry");

                SubscriptionPackages sp = new SubscriptionPackages(name, price);
                Subscription sub = new Subscription(user_id, sp, subscription_expiry);
            }
        }
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
        return s;
    }
    public static DefaultTableModel buildTableModel(ResultSet x) 
    {
        try
        {
            ResultSetMetaData metaData = x.getMetaData();
            // names of columns
            Vector<String> columnNames = new Vector<String>();
            int columnCount = metaData.getColumnCount();
            for (int column = 1; column <= columnCount; column++) 
            {
                columnNames.add(metaData.getColumnName(column));
            }

            // data of the table
            Vector<Vector<Object>> data = new Vector<Vector<Object>>();
            while (x.next()) {
                Vector<Object> vector = new Vector<Object>();
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    vector.add(x.getObject(columnIndex));
                }
                data.add(vector);
            }
            return new DefaultTableModel(data, columnNames);
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }
    public void sendEmail(String from, String pass, String[] to, String subject, String body)
    {
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
//            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            // To get the array of addresses
            for( int i = 0; i < to.length; i++ ) 
            {
                toAddress[i] = new InternetAddress(to[i]);
            }
            for( int i = 0; i < toAddress.length; i++) 
            {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            message.setSubject(subject);
            message.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
    }
    public Song retrieveSong(int songID)
    {
        rs = null;
        this.runCommand();
        Song s = null;
        try
        {
            sql = "select * "
                + "from Songs "
                + "where song_id = " + songID;
            prestmt = c.prepareStatement(sql);
            rs = prestmt.executeQuery();
            rs.next();
            String singer = rs.getString("singer");
            String title = rs.getString("title");
            int added_by = rs.getInt("added_by");
            boolean premium = rs.getBoolean("premium");
            String path_to_file = rs.getString("path_to_file");
            
            s = new Song(songID, added_by, title, singer, premium, path_to_file);
        }
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
        
        return s;
    
    }
    public void deleteSong(int songID)
    {
        this.runCommand();
        try
        {
            sql = "delete from Playlist where song_id = " + songID;
            prestmt = c.prepareStatement(sql);
            prestmt.executeUpdate();
        
            sql = "delete from Songs where song_id = " + songID;
            prestmt = c.prepareStatement(sql);
            prestmt.executeUpdate();
        }
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
    }
    public void removeFeedback(Feedback fb)
    {
        this.runCommand();
        try
        {
            sql = "delete from Feedback where feedbackID = " + fb.getFeedback_id();
            prestmt = c.prepareStatement(sql);
            prestmt.executeUpdate();
        }
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        } 
    }
    public void addtoResolvedFeedback(Feedback fb, String date)
    {
        this.runCommand();
        try
        {
            String sql = "insert into ResolvedFeedback (feedback_details, song_id, feedback_date, resolve_date) values(?,?,?,?)";
            System.out.println(sql);
            PreparedStatement prestmt=c.prepareStatement(sql);
            prestmt.setString(1, fb.getFeedback());
            prestmt.setInt(2, fb.getSong_id());
            prestmt.setString(3, fb.getFeedback_date());
            prestmt.setString(4, date);
            prestmt.executeUpdate();
        }
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
    
    }
    public Feedback getFeedback(int songID)
    {
        Feedback fb = null;
        this.runCommand();
        try
        {
            sql = "select * from Feedback where song_id = " + songID;
            prestmt = c.prepareStatement(sql);
            rs = prestmt.executeQuery();
            rs.next();
            String feedback = rs.getString("feedback_details");
            int feedback_id = rs.getInt("feedbackid");
            int song_id = rs.getInt("song_id");
            int user_id = rs.getInt("userid");
            String feedback_date = rs.getString("feedback_date");
            fb = new Feedback(user_id, feedback_id, song_id, feedback_date, feedback);
        
        }
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
        
        return fb;
    
    }
    public ResultSet getAllFeedback()
    {
        rs = null;
        this.runCommand();
        try
        {
            sql = "select feedbackid, feedback_details, songs.title as Song, feedback_date, account.name as Submitted_by"
                    + " from Feedback "
                    + "join Songs on feedback.song_id = songs.song_id "
                    + "join Users on users.userid = feedback.userid "
                    + "join Account on users.account_id = account.id";
            
            prestmt = c.prepareStatement(sql);
            rs = prestmt.executeQuery();
        }
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }        
        return rs;
    }
    public int getSongID(String name)
    {
        int song_id=-1;
        this.runCommand();
        try
        {
            sql = "select * from Songs where title = '" + name + "'";
            prestmt = c.prepareStatement(sql);
            rs = prestmt.executeQuery();
            rs.next();
            song_id = rs.getInt("song_id");
        }
        catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
        return song_id;
    }
}    
