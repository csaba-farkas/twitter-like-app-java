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

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author Csaba Farkas <Csaba.Farkas@mycit.ie Student ID: R00117945>
 */
public class TweetDbTableRenderer extends DefaultTableCellRenderer {

    public TweetDbTableRenderer() {
        super();
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

        //Set the background color of every second row to a lighter blue than the header
        if(row%2 != 0) {
            this.setBackground(new Color(205, 224, 250));
        }

        //Set the background color of the other rows to white
        else {
            this.setBackground(Color.white);
        }

        //Set the background color of a row when selected to a light gray color
        if(isSelected) {
            this.setBackground(new Color(184, 187, 191));
        }

        //Set the background color of the cell when it has focus to a darker gray color
        if(hasFocus) {
            this.setBackground(new Color(123, 138, 135));
        }
        
        //Align cell values to CENTER
        this.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        return this;
    }
}
