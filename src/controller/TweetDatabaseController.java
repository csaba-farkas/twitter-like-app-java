/*
 * Copyright (C) 2015 Csaba Farkas <Csaba.Farkas@mycit.ie Student ID: R00117945>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package controller;

import controller.interfaces.IPersistor;
import controller.interfaces.ITweetDatabaseGui;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import model.Tweet;
import model.TwitterUser;

/**
 * Java singleton class serves as controller.
 * 
 * @author Csaba Farkas <Csaba.Farkas@mycit.ie Student ID: R00117945>
 */
public class TweetDatabaseController {
    
    //Static method to call an instance of this class
    public static TweetDatabaseController getInstance() {
        return TweetDatabaseControllerHolder.INSTANCE;
    }
    
    //Empty constructor
    private TweetDatabaseController() {
    
    }
    
    //Instance variables
    private List<TwitterUser> twitterUsers;
    private ITweetDatabaseGui gui;
    private IPersistor persistor;
    
    private static class TweetDatabaseControllerHolder {

        private static final TweetDatabaseController INSTANCE = new TweetDatabaseController();
    }
    
    //Getters-setters
    public List<TwitterUser> getDataModel() {
        return this.twitterUsers;
    }
    
    public void setDataModel(List<TwitterUser> twitterUsers) {
        this.twitterUsers = twitterUsers;
    }
    
    public ITweetDatabaseGui getGuiReference() {
        return this.gui;
    }
    
    public void setGuiReference(ITweetDatabaseGui gui) {
        this.gui = gui;
    }
    
    public IPersistor getPersistor() {
        return this.persistor;
    }
    
    public void setPersistor(IPersistor persistor) {
        this.persistor = persistor;
    }
    
    /**
     * Add new user to model and database.
     * 
     * @param username Username of new user.
     * @param country Country of new user.
     */
    public void createUser(String username, String country) {
        
        //Create a new user
        TwitterUser user = new TwitterUser(username, country);
        
        //Add user if, and only if there is no other user with the same username in db.
        try {
            this.persistor.write(user);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex, ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        //Update model only after user was successfully added to database.
        this.twitterUsers.add(user);
       
        //Update GUI
        this.gui.refreshTable();
    }
    
    /**
     * Removes a user from the database and from the model.
     * 
     * @param selectedUser User who is to be removed.
     */
    public void deleteUser(int selectedUser) {
        //Delete from database first
        String username = this.twitterUsers.get(selectedUser).getUsername();
        this.persistor.removeTwitterUser(username);
        
        //Delete from model
        this.twitterUsers.remove(selectedUser);
        
        //Update GUI
        this.gui.refreshTable();
    }
    
    /**
     * Updates a user in model and in database.
     * 
     * @param selectedRow Indicates position of user in list.
     * @param updatedUser Updated user object.
     */
    public void updateUser(int selectedRow, TwitterUser updatedUser) {
        //Get primary key
        String primaryKey = this.twitterUsers.get(selectedRow).getUsername();
        
        //Update user in db if, and only if the updated username is not a duplicate.
        try {
            //Update database
            this.persistor.updateUser(primaryKey, updatedUser);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex, "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        //Update model only after database is updated (in case an SQLException is thrown)
        this.twitterUsers.set(selectedRow, updatedUser);
        
        //Update GUI
        this.gui.refreshTable();
    }
    /**
     * Add a new tweet to a user. Update model and database record separately.
     * 
     * @param tweet New Tweet object.
     * @param username Username.
     * @param selectedUser Position of user in data model.
     */
    public void addTweetForUser(Tweet tweet, String username, int selectedUser) {
        //Update model
        TwitterUser user = this.twitterUsers.get(selectedUser);
        user.addTweet(tweet);
        
        //Update persistor
        this.persistor.addTweetForUser(tweet, username);
        
    }
}
