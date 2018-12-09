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
public class SubscriptionManager extends Account
{
    ArrayList<SubscriptionPackages> sub_packages = new ArrayList<SubscriptionPackages>();
    private SubscriptionManager(String name, String email, String mobile_number, String password)
    {
        super(name, email, mobile_number, password);
    }
    
    // static variable single_instance of type Singleton 
    private static SubscriptionManager sub_manager = null; 
    public static SubscriptionManager getInstance(String name, String email, String mobile_number, String password) 
    { 
        if (sub_manager == null) 
            SubscriptionManager.sub_manager = new SubscriptionManager (name, email, mobile_number, password); 
        return sub_manager; 
    } 
    void addPackage(){}
} 

