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

package view;

import java.util.List;
import javax.swing.table.DefaultTableModel;
import model.TwitterUser;

/**
 * @author Csaba Farkas <Csaba.Farkas@mycit.ie Student ID: R00117945>
 */
public class TweetDbTableModel extends DefaultTableModel {

    //Constant number of columns
    private static final int NUMBER_OF_COLUMNS = 2;     //Username, country
    
    //Constants for column indices
    private static final int USERNAME_COL = 0;
    private static final int COUNTRY_COL = 1;
    
    //Data structure instance variable
    List<TwitterUser> users;
    
    public TweetDbTableModel(List<TwitterUser> users) {
        super();
        this.users = users;
    }
    
    /**
     * Override DefaultTableModel method. Getter: number of columns.
     * 
     * @return Number of columns.
     */
    @Override
    public int getColumnCount() {
        
        return TweetDbTableModel.NUMBER_OF_COLUMNS;
        
    }
    
    /**
     * Override DefaultTableModel method. Getter: column name.
     * 
     * @param columnIndex Indicates position of column in table.
     * @return Name of column at position columnIndex.
     */
    @Override
    public String getColumnName(int columnIndex) {
        
        if(columnIndex == TweetDbTableModel.USERNAME_COL) {
            return "Username";
        }
        else {
            return "Country";
        }
        
    }
    
    /**
     * Override DefaultTableModel method. Getter: number of rows.
     * 
     * @return Number of rows (number of records in list).
     */
    @Override
    public int getRowCount() {
        
        if(this.users != null) {
            return this.users.size();
        }
        
        return 0;
        
    }
    
    /**
     * Override DefaultTableModel method. Getter: get value from a specified cell.
     * 
     * @param row Indicates position of row in table.
     * @param col Indicates position of column in table.
     * @return Value at intersection of row and column.
     */
    @Override
    public Object getValueAt(int row, int col) {
        
        TwitterUser current = this.users.get(row);
        
        if(col == TweetDbTableModel.USERNAME_COL) {
            return current.getUsername();
        }
        else {
            return current.getCountry();
        }
    }
    
    /**
     * Override DefaultTableModel method. Method returns a boolean value indicating
     * if a particular cell is editable.
     * 
     * @param row Indicates row number.
     * @param col Indicates column number.
     * @return A boolean value indicating if cell is editable.
     */
    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }
}
