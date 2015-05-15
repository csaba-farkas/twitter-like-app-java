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
 * @author Csaba Farkas <Csaba.Farkas@mycit.ie Student ID: R00117945>
 */
public class TwitterUserApplication {

    public static void main(String[] args) {
        
        IPersistor persistor = new TweetDatabasePersistor();
        TweetDatabaseController.getInstance().setPersistor(persistor);
        
        List<TwitterUser> users = persistor.read();
        
        TweetDatabaseController.getInstance().setDataModel(users);
        
        ITweetDatabaseGui gui = new TweetDbFrame("Twitter User Application");
        
        TweetDatabaseController.getInstance().setGuiReference(gui);
        
    }
}
