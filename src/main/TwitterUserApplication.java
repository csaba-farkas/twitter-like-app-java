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

package main;

import controller.TweetDatabaseController;
import controller.TweetDatabasePersistor;
import controller.interfaces.IPersistor;
import controller.interfaces.ITweetDatabaseGui;
import java.util.List;
import model.TwitterUser;
import view.TweetDbFrame;

/**
 * Main class. Creates a data persistor object and the controller sets this object
 * to be the data persistor of the system. Then it creates a list and sets it
 * to be the data model of the system. Then a GUI object is created and is set
 * to be the gui of the system.
 * @author Csaba Farkas <Csaba.Farkas@mycit.ie Student ID: R00117945>
 */
public class TwitterUserApplication {
    
    public static final String TITLE = "Twitter Application";

    public static void main(String[] args) {
        
        IPersistor persistor = new TweetDatabasePersistor();
        TweetDatabaseController.getInstance().setPersistor(persistor);
        
        List<TwitterUser> users = persistor.read();
        
        TweetDatabaseController.getInstance().setDataModel(users);
        
        ITweetDatabaseGui gui = new TweetDbFrame(TwitterUserApplication.TITLE);
        
        TweetDatabaseController.getInstance().setGuiReference(gui);
        
    }
}
