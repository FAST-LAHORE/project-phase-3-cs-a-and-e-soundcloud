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
public class Subscription 
{
    int user_id;
    String subscription_expiry; //dd/mm/yyyy

    public Subscription(int user_id, String subscription_expiry) {
        this.user_id = user_id;
        this.subscription_expiry = subscription_expiry;
    }
    
}
