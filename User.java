/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soundcloud;

import java.util.ArrayList;

/**
 *
 * @author Ibrahim
 */
public class User extends Account
{
    ArrayList<PlayList> user_playlists = new ArrayList<PlayList>();
    Subscription sub;
    SubscriptionManager submanager = SubscriptionManager.getInstance("Subman", "submanager@gmail.com", "034353", "123");
    public User(String name, String email, String mobile_number, String password) 
    {
        super(name, email, mobile_number, password);
    }
    public User(String name, String email, String mobile_number, String password, String blocked_until) {
        super(name, email, mobile_number, password, blocked_until);
    }
    void setPassword(String p)
    {
        this.password=p;
    }
    void setBlockDate(String b)
    {
        this.blocked_until = b;
    }
    String getName()
    {
        return this.name;
    }
    public String getEmail() {
        return email;
    }
    public String getMobile_number() {
        return mobile_number;
    }
    public String getPassword() {
        return password;
    }
    public String getBlocked_until() {
        return blocked_until;
    }
    public ArrayList<PlayList> getUser_playlists() {
        return user_playlists;
    }
    public void setUser_playlists(ArrayList<PlayList> user_playlists) {
        this.user_playlists = user_playlists;
    }
    public void getPlaylists()
    {
        if (this.user_playlists!=null)
        {
            for (int i=0;i<this.user_playlists.size();i++)
            {
                PlayList a = this.user_playlists.get(i);
                System.out.println("Playlist #"+(i+1));
                for (int j=0;j<a.songslist.size();j++)
                {
                    System.out.println(a.songslist.get(j).getTitle());
                }
            }
        }
    }
    public boolean Subscribe(SubscriptionPackages sp)
    {
        submanager.subscribeUser(this, sp);
        
        String credit_card_no = null;
        double amount = 0.0;
        boolean status = this.makePayment(credit_card_no, amount);
        return status;
    }
    public boolean makePayment(String CreditCardNo, double amount)
    {
        
        return true;
        
    }
    public String BrowsePackages()
    {
        ArrayList<SubscriptionPackages> packages = submanager.BrowsePackage(this);
        SubscriptionPackages sp = null;

        boolean status = this.Subscribe(sp);

        
        if (status)
        {
            submanager.updateUserSubscription(this, sp);
            return "Success";
        }
        else
        {
            return "Failure";
        }
    }

    public void setSub(Subscription sub) {
        this.sub = sub;
    }

}