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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import model.Tweet;
import model.TwitterUser;

/**
 * @author Csaba Farkas <Csaba.Farkas@mycit.ie Student ID: R00117945>
 */
public class TweetDatabasePersistor implements IPersistor {

    //Instance variables
    private Connection connection;
    private final String dbUrl = "jdbc:mysql://localhost:3306/tweetdata"; //"jdbc:mysql://sql5.freemysqlhosting.net:3306/sql577244";
    private final String userName = "root";//"sql577244";
    private final String pword = "";//"rV6!tQ7!";
    
    /**
     * Constructor method. Creates a connection to the database using url, username
     * and password.
     */
    public TweetDatabasePersistor() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection(this.dbUrl, this.userName, this.pword);
            JOptionPane.showMessageDialog(null, "INFO: Connected to database" + connection, "Message", JOptionPane.INFORMATION_MESSAGE);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex, "Database Error" , JOptionPane.ERROR_MESSAGE);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            JOptionPane.showMessageDialog(null, sqlEx, "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 
     * @return 
     */
    @Override
    public List<TwitterUser> read() {
        List<TwitterUser> users = new ArrayList<TwitterUser>();
        
        try {
            //Get user details from table: user.
            Statement getUserStmt = connection.createStatement();
            ResultSet resSet = getUserStmt.executeQuery("select * from user;");
            while(resSet.next()) {
                TwitterUser user = new TwitterUser();
                user.setUsername(resSet.getString("username"));
                user.setCountry(resSet.getString("country"));
                users.add(user);
            }
            resSet.close();
            
            //Add tweets to user.
            for(TwitterUser user : users) {
                user.setTweets(this.getUserTweets(user.getUsername()));
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex, "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        
        return users;
    }
    /**
     * Implemented IPersistor method. Writes data from a TwitterUser object into
     * the database. Database consists of two tables. 
     * First table: User(username, country) Primary key: username).
     * Second table: Tweet(username, tweet, date) Primary key: (username, tweet, date).
     * Foreign key: username.
     * Data types in User table: String, String.
     * Data types in Tweet table: String, String, Timestamp.
     * 
     * @param twitterUser Indicates TwitterUser objects to be stored in database.
     */
    @Override
    public void write(TwitterUser twitterUser) {
        try {
            PreparedStatement statement = this.connection.prepareStatement("insert into user values (?, ?)");
            statement.setString(1, twitterUser.getUsername());
            statement.setString(2, twitterUser.getCountry());
            statement.execute();
            
            statement = this.connection.prepareStatement("insert into tweet values(?, ?, ?)");
            List<Tweet> tweets = twitterUser.getTweets();
            
            for(Tweet tweet : tweets) {
                statement.setString(1, twitterUser.getUsername());
                statement.setString(2, tweet.getTweeText());
                statement.setTimestamp(3, new Timestamp(tweet.getDateTimeSent().getTime()));
                statement.execute();
            }
            statement.close();
            
        } catch(SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex, ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        } 
    }
    
    /**
     * Implemented IPersistor method. Reads data about a particular user from the
     * database. 
     * 
     * @param username Indicates username of particular user.
     * @return A TwitterUser object
     */
    @Override
    public TwitterUser readTwitterUser(String username) {
        
        //Create a new TwitterUser object using an empty constructor.
        TwitterUser user = new TwitterUser();
        try {
            //Get user details from table: user.
            Statement getUserStmt = connection.createStatement();
            ResultSet resSet = getUserStmt.executeQuery("select * from user where username = \'" + username + "\';");
            while(resSet.next()) {
                user.setUsername(resSet.getString("username"));
                user.setCountry(resSet.getString("country"));
            }
            resSet.close();
            
            //Get tweets from table: tweet.
            List<Tweet> userTweets = new ArrayList<Tweet>();
            userTweets = getUserTweets(username);
            
            //Add tweets to user.
            for(Tweet tweet : userTweets) {
                user.addTweet(tweet);
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex, "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        
        return user;
    }
    
    /**
     * Implemented IPersistor method. Reads tweets of a particular user from the
     * database.
     * 
     * @param username Indicates username of particular user.
     * @return A List<Tweet> object containing all tweets of user.
     */
    @Override
    public List<Tweet> getUserTweets(String username) {
        
        //Create a new list.
        List<Tweet> userTweets = new ArrayList<Tweet>();
        
        try {
            //Read all tweets from table: tweet and add them the userTweets.
            Statement getUserStmt = connection.createStatement();
            ResultSet resSet1 = getUserStmt.executeQuery("select * from tweet where username = \'" + username + "\';");
            while(resSet1.next()) {
                String tweetText = resSet1.getString("tweet");
                Date tweetDate = resSet1.getTimestamp("date");
                Tweet userTweet = new Tweet(tweetText, tweetDate);
                userTweets.add(userTweet);
            }
            resSet1.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex, "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return userTweets;
    }
    
    /**
     * Implemented IPersistor method. Adds a tweet to the tweet table with username
     * and tweet are specified in parameters.
     * 
     * @param newTweet New tweet object to be added to user's tweets.
     * @param username Username.
     */
    @Override
    public void addTweetForUser(Tweet newTweet, String username) {
        try {
            PreparedStatement insertNewTweet = this.connection.prepareStatement("insert into tweet values (?, ?, ?)");
            insertNewTweet.setString(1, username);
            insertNewTweet.setString(2, newTweet.getTweeText());
            insertNewTweet.setTimestamp(3, new Timestamp(newTweet.getDateTimeSent().getTime()));

            insertNewTweet.execute();
            insertNewTweet.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex, "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Implemented IPersistor method. Removes a user from user table. Tweets are
     * automatically removed from tweet table because of foreign key constraint.
     * 
     * Please see sql below.
     * CREATE TABLE `tweet` (  
     * `username` varchar(100) NOT NULL,  
     * `tweet` varchar(140) NOT NULL,  
     * `date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',  
     * PRIMARY KEY (`username`,`tweet`,`date`),  
     * CONSTRAINT `fktweet` FOREIGN KEY (`username`) REFERENCES `user` (`username`) 
     * ON DELETE CASCADE ON UPDATE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=latin1
     * 
     * @param username User who is to be deleted from db.
     */
    @Override
    public void removeTwitterUser(String username) {
        try {
            Statement removeFromUsersStatement = this.connection.createStatement();
            removeFromUsersStatement.execute("delete from user where username = \'" + username + "\';");
            removeFromUsersStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex, "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Implemented IPersistor method. Updates user in table whose primary key is
     * passed as a parameter. (Username is primary key).
     * 
     * @param primaryKey Username as primary key.
     * @param updatedUser Updated user object.
     */
    @Override
    public void updateUser(String primaryKey, TwitterUser updatedUser) {
        try {
            Statement updateStatement = this.connection.createStatement();
            updateStatement.execute("update user set username = \'" + 
                                    updatedUser.getUsername() +
                                    "\', country = \'" + 
                                    updatedUser.getCountry() +
                                    "\' where username = \'" +
                                    primaryKey + "\';");
            updateStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, ex, "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
