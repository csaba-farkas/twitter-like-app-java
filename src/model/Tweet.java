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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Tweet class. It creates a Tweet object containing the text and the date when
 * the tweet was entered into the system.
 * 
 * @author Csaba Farkas <Csaba.Farkas@mycit.ie Student ID: R00117945>
 */
public class Tweet {
    
    //Instance variables
    private String tweetText;
    private Date dateTimeSent;
    private DateFormat dateFormat;

    /**
     * Constructor method
     * 
     * @param tweeText Indicates text of a tweet.
     */
    public Tweet(String tweeText) {
        this.tweetText = tweeText;
        this.dateTimeSent = Calendar.getInstance().getTime();
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * Constructor method for retrieving existing tweets from database where
     * time is already set.
     * 
     * @param tweetText Indicates the text of tweet.
     * @param dateTimeSent Indicates when the tweet was sent.
     */
    public Tweet(String tweetText, Date dateTimeSent) {
        this.tweetText = tweetText;
        this.dateTimeSent = dateTimeSent;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
    /**
     * TweetText getter method.
     * 
     * @return A String object containing a tweet.
     */
    public String getTweeText() {
        return tweetText;
    }

    /**
     * TweetText setter method.
     * 
     * @param tweeText A new tweet.
     */
    public void setTweeText(String tweeText) {
        this.tweetText = tweeText;
    }

    /**
     * DateTimeSent getter method.
     * 
     * @return Date and time when tweet was sent.
     */
    public Date getDateTimeSent() {
        return dateTimeSent;
    }

    /**
     * Override toString. Display tweet and date and time.
     * 
     * @return A string with text of tweet and date and time.
     */
    @Override
    public String toString() {
        return "\"" + this.tweetText + "\" \nDate and time: " + this.dateFormat.format(this.dateTimeSent.getTime());
    }
    
    
    
    
    

}
