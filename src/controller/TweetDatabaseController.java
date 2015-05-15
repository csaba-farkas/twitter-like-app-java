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
import java.util.List;
import model.Tweet;
import model.TwitterUser;

/**
 * Java singleton class serves as controller.
 * 
 * @author Csaba Farkas <Csaba.Farkas@mycit.ie Student ID: R00117945>
 */
public class TweetDatabaseController {
    
    public static TweetDatabaseController getInstance() {
        return TweetDatabaseControllerHolder.INSTANCE;
    }
    
    private TweetDatabaseController() {
    
    }
    
    private List<TwitterUser> twitterUsers;
    private ITweetDatabaseGui gui;
    private IPersistor persistor;
    private boolean isAddedToDatabase;
    
    private static class TweetDatabaseControllerHolder {

        private static final TweetDatabaseController INSTANCE = new TweetDatabaseController();
    }
    
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
        
        this.persistor.write(user);
        
        //Update model
        this.twitterUsers.add(user);
       
        //Update GUI
        this.gui.refresh();
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
        this.gui.refresh();
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
        
        //Update model
        this.twitterUsers.set(selectedRow, updatedUser);
        
        //Update database
        this.persistor.updateUser(primaryKey, updatedUser);
        
        //Update GUI
        this.gui.refresh();
        
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
    
    public void refreshTweets(int rowNumber) {
        this.gui.refreshTweets(rowNumber);
    }
}
