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

package model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Csaba Farkas <Csaba.Farkas@mycit.ie Student ID: R00117945>
 */
public class TwitterUser {
    
    //Instance variables
    private String username;
    private String country;
    private List<Tweet> tweets;

    /**
     * Constructor method.
     * 
     * @param username Indicates username of TwitterUser.
     * @param country Indicates country of TwitterUser.
     */
    public TwitterUser(String username, String country) {
        this.username = username;
        this.country = country;
        this.tweets = new ArrayList<Tweet>();
    }

    /**
     * Empty constructor used by TweetDatabasePersistor class when retrieving data.
     */
    public TwitterUser() {
        this.tweets = new ArrayList<Tweet>();
    }
    
    public String getUsername() {
        return username;
    }

    public String getCountry() {
        return country;
    }

    public List<Tweet> getTweets() {
        return tweets;
    }
    
    public void setTweets(List<Tweet> tweets) {
        this.tweets = tweets;
    }
    public void addTweet(Tweet tweet) {
        this.tweets.add(tweet);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    
    @Override
    public String toString() {
        
        String tweetsString = "";
        for(Tweet tweet : this.tweets) {
            tweetsString += tweet;
        }
        return this.username + "\n" +
               this.country + "\n" +
               tweetsString;
    }

}
