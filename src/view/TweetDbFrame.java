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

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import controller.TweetDatabaseController;
import controller.interfaces.ITweetDatabaseGui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.JTableHeader;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import model.Tweet;
import model.TwitterUser;
import net.miginfocom.swing.MigLayout;
import view.components.DocumentSizeFilter;

/**
 * @author Csaba Farkas <Csaba.Farkas@mycit.ie Student ID: R00117945>
 */
public class TweetDbFrame extends JFrame implements ITweetDatabaseGui {
    
    //Button labels are in html form - BoxLayout will stretch buttons this way
    private static final String ADD_BUTTON_LABEL = "<html>Add User...</html>";
    private static final String ADD_TWEET_BUTTON_LABEL = "<html>Add Tweet...</html>";
    private static final String DELETE_BUTTON_LABEL = "<html>Delete...</html>";
    private static final String MODIFY_BUTTON_LABEL = "<html>Modify...</html>";
    private static final String CANCEL_BUTTON_LABEL = "<html>Cancel</html>";
    private static final String ADD_NEW_DIALOG_TITLE = "Add new user";
    private static final String USERNAME_LABEL = "Username: ";
    private static final String COUNTRY_LABEL = "Country: ";
    private static final String ADD_NEW_TWEET_TITLE = "Add new tweet";
    private static final String ADD_NEW_TWEET_LABEL = "\'s new tweet: ";
    private static final String CHARACTERS_REMAINING_LABEL = " characters remaining";
    private static final String MODIFY_DIALOG_TITLE = "Modify user";
    private static final String USERNAME_ERROR_MESSAGE = "Error: Username must be entered!";
    private static final String ERROR_DIALOG_TITLE = "Error";
    private static final String DELETE_WARNING_LABEL = "Are you sure you want to delete this user?";
    private static final String TABLE_BORDER_TITLE = "Twitter Users";
    private static final String TWEETS_BORDER_TITLE = "Tweets";
    private static final String EXIT_LABEL = "Are you sure you want to exit?";
    private static final String USER_DELETED_LABEL = " was deleted!";
    private static final String USER_DUPLICATE_ERROR = "Error: User already exists!";
    
    //Instance varaibles
    private JButton addButton;
    private JButton addTweetButton;
    private JButton deleteButton;
    private JButton modifyButton;
    private JButton cancelButton;
    private JTable userTable;
    private JTextArea tweets;
    private JTextField usernameField;
    private JTextField countryField;
    private TweetDbTableModel tableModel;
    private JLabel remaningLabel;
    private JTextArea tweetArea;
    
    
    public TweetDbFrame(String title) {
        super(title);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        
        //Panel with buttons
        mainPanel.add(createEastPanel(), BorderLayout.EAST);
        
        //Panel with table and JTextArea
        JPanel centerPanel = createCenterPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        this.getContentPane().add(mainPanel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(900, 600);
        
        //Frame appears in the middle of the screen
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();      //Get the size of the screen
        int surgeryFrameWidth = this.getSize().width;
        int surgeryFrameHeigth = this.getSize().height;
        int locationX = (dimension.width - surgeryFrameWidth)/2;                //X coordinate = (width of screen - width of frame) / 2
        int locationY = (dimension.height - surgeryFrameHeigth)/2;              //Y coordinate = (heigth of screen - height of frame) / 2
        this.setLocation(locationX, locationY);
        
        this.setVisible(true);
    }

    /**
     * Method that creates and returns the panel on the right hand side of the 
     * frame.
     * 
     * @return A JPanel object.
     */
    private JPanel createEastPanel() {
        this.usernameField = new JTextField(15);
        this.countryField = new JTextField(15);
        
        JPanel eastPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(eastPanel, BoxLayout.Y_AXIS);
        eastPanel.setLayout(boxLayout);
        
        //Add button with ActionListener -> Call add user action dialog
        this.addButton = new JButton(TweetDbFrame.ADD_BUTTON_LABEL);
        this.addButton.setBackground(new Color(204, 255, 229));
        this.addButton.addActionListener((ActionEvent e) -> {      
           
            JPanel addPanel = createAddPanel();
            
            //Reset JTextFields (after modify user button may have been used)
            this.usernameField.setText("");
            this.countryField.setText("");
            
            int result = JOptionPane.showConfirmDialog(this, addPanel, TweetDbFrame.ADD_NEW_DIALOG_TITLE, JOptionPane.OK_CANCEL_OPTION);
            
            //Parse results and call the controller to create a new user if 'OK' and username field is not empty
            if(result == JOptionPane.OK_OPTION && !this.usernameField.getText().isEmpty()) {
                String username = this.usernameField.getText();
                String country = this.countryField.getText();
                try {
                    TweetDatabaseController.getInstance().createUser(username, country);
                } catch (Exception ex) {
                    //Looking for MySQLIntegrityConstraintViolationException (wasn't accepted by IDE)
                    JOptionPane.showMessageDialog(TweetDbFrame.this, TweetDbFrame.USER_DUPLICATE_ERROR, TweetDbFrame.ERROR_DIALOG_TITLE, JOptionPane.ERROR_MESSAGE);
                }
            }
            else if(this.usernameField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(TweetDbFrame.this, TweetDbFrame.USERNAME_ERROR_MESSAGE, TweetDbFrame.ERROR_DIALOG_TITLE, JOptionPane.ERROR_MESSAGE);
            }
            
        });
        
        eastPanel.add(this.addButton);
        eastPanel.add(Box.createVerticalStrut(5));
        
        //Add tweet button with ActionListener -> Call add new tweet dialog
        this.addTweetButton = new JButton(TweetDbFrame.ADD_TWEET_BUTTON_LABEL);
        this.addTweetButton.setBackground(new Color(204, 255, 229));
        this.addTweetButton.setEnabled(false);
        this.addTweetButton.addActionListener((ActionEvent e) -> {
            
            AddTweetDialog addTweetDialog = new AddTweetDialog();
       
        });
        
        eastPanel.add(this.addTweetButton);
        eastPanel.add(Box.createVerticalStrut(5));
        
        //Modify button with ActionListener -> Call modify user dialog
        this.modifyButton = new JButton(TweetDbFrame.MODIFY_BUTTON_LABEL);
        this.modifyButton.setBackground(new Color(204, 255, 229));
        this.modifyButton.setEnabled(false);
        this.modifyButton.addActionListener((ActionEvent e) -> {
            
            int rowNumber = this.userTable.getSelectedRow();
            JPanel modifyPanel = createAddPanel();
            this.usernameField.setText(TweetDatabaseController.getInstance().getDataModel().get(rowNumber).getUsername());
            this.countryField.setText(TweetDatabaseController.getInstance().getDataModel().get(rowNumber).getCountry());
            
            int result = JOptionPane.showConfirmDialog(this, modifyPanel, TweetDbFrame.MODIFY_DIALOG_TITLE, JOptionPane.OK_CANCEL_OPTION);
            
            //Modify user if 'OK' and username field is not empty.
            if(result == JOptionPane.OK_OPTION && !this.usernameField.getText().isEmpty()) {
                String username = this.usernameField.getText();
                String country = this.countryField.getText();
                
                TwitterUser updatedUser = new TwitterUser(username, country);
                TweetDatabaseController.getInstance().updateUser(rowNumber, updatedUser);
            }
            else if(this.usernameField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(TweetDbFrame.this, TweetDbFrame.USERNAME_ERROR_MESSAGE, TweetDbFrame.ERROR_DIALOG_TITLE, JOptionPane.ERROR_MESSAGE);
            }
            
        });
        
        eastPanel.add(this.modifyButton);
        eastPanel.add(Box.createVerticalStrut(5));
        
        //Delete button with ActionListener -> Call remove user method.
        this.deleteButton = new JButton(TweetDbFrame.DELETE_BUTTON_LABEL);
        this.deleteButton.setBackground(new Color(204, 255, 229));
        this.deleteButton.setEnabled(false);
        this.deleteButton.addActionListener((ActionEvent e) -> {
            
            int rowSelected = this.userTable.getSelectedRow();
            
            String warning = TweetDbFrame.DELETE_WARNING_LABEL;
            int answer = JOptionPane.showConfirmDialog(rootPane, warning);
            
            if(answer == JOptionPane.YES_OPTION) {
                String username = TweetDatabaseController.getInstance().getDataModel().get(rowSelected).getUsername();
                TweetDatabaseController.getInstance().deleteUser(rowSelected);
                JOptionPane.showMessageDialog(rootPane, (username + TweetDbFrame.USER_DELETED_LABEL));
            }
        });
        
        eastPanel.add(this.deleteButton);
        eastPanel.add(Box.createVerticalStrut(5));
        
        //Cancel button with ActionListener with dispose() method
        this.cancelButton = new JButton(TweetDbFrame.CANCEL_BUTTON_LABEL);
        this.cancelButton.setBackground(new Color(204, 255, 229));
        this.cancelButton.addActionListener((ActionEvent e) -> {
            int answer = JOptionPane.showConfirmDialog(rootPane, TweetDbFrame.EXIT_LABEL);
            if(answer != JOptionPane.NO_OPTION && answer != JOptionPane.CANCEL_OPTION) {
                dispose();
            }
        });
        
        eastPanel.add(this.cancelButton);
        
        eastPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return eastPanel;
    }
    
    /**
     * Method that creates the center part of the frame including a table and a
     * text area. Table displays users, text area displays tweets of selected 
     * user.
     * 
     * @return A JPanel object.
     */
    private JPanel createCenterPanel() {
        
        //Create the main panel and set layout to BoxLayout X Axis.
        JPanel centerPanel = new JPanel();
        BoxLayout box = new BoxLayout(centerPanel, BoxLayout.X_AXIS);
        centerPanel.setLayout(box);
        
        //Create a JPanel that holds the JScrollPane that holds the table.
        JPanel tablePanel = new JPanel(new BorderLayout());
        this.userTable = new JTable();
        this.tableModel = new TweetDbTableModel(TweetDatabaseController.getInstance().getDataModel());
        this.userTable.setModel(this.tableModel);
        this.userTable.setDefaultRenderer(Object.class, new TweetDbTableRenderer());
        //ListSelectionListener attached to table-> enable/disable 3 buttons when selection is on/off
        this.userTable.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if(this.userTable.getSelectedRow() != -1) {
                this.deleteButton.setEnabled(true);
                this.modifyButton.setEnabled(true);
                this.addTweetButton.setEnabled(true);
                int rowNumber = this.userTable.getSelectedRow();
                TweetDatabaseController.getInstance().getGuiReference().refreshTweets(rowNumber);
            }
            else {
                this.deleteButton.setEnabled(false);
                this.modifyButton.setEnabled(false);
                this.addTweetButton.setEnabled(false);
            }
            
        });
        
        //Disable multi-line selection
        this.userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JTableHeader tableHeader = this.userTable.getTableHeader();
        tableHeader.setFont(tableHeader.getFont().deriveFont(Font.BOLD));
        tableHeader.setBackground(new Color(107, 161, 237));
        JScrollPane tablePane = new JScrollPane(this.userTable);
        tablePane.setBorder(BorderFactory.createTitledBorder(TweetDbFrame.TABLE_BORDER_TITLE));
        tablePanel.add(tablePane, BorderLayout.CENTER);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        centerPanel.add(tablePane);
        centerPanel.add(Box.createHorizontalStrut(10));
        
        //Create a text are insulated in a JPanel that displays tweets of selected user.
        JPanel textPanel = new JPanel(new BorderLayout());
        this.tweets = new JTextArea(10, 30);
        this.tweets.setLineWrap (true);
        this.tweets.setWrapStyleWord(true);
        this.tweets.setBackground(new Color(255, 255, 153));
        this.tweets.setEnabled(false);
        this.tweets.setDisabledTextColor(Color.black);
        Font font = this.tweets.getFont();  
        this.tweets.setFont(font.deriveFont(Font.BOLD));
        JScrollPane textPane = new JScrollPane(this.tweets);
        textPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        textPanel.add(textPane, BorderLayout.CENTER);
        textPanel.setBorder(BorderFactory.createTitledBorder(TweetDbFrame.TWEETS_BORDER_TITLE));
        centerPanel.add(textPanel);
        
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return centerPanel;
        
    }
    
    /**
     * Override ITweetDatabaseGui method.
     */
    @Override
    public void refresh() {
        this.tableModel.fireTableDataChanged();
    }

    @Override
    public void refreshTweets(int rowNumber) {
        TwitterUser user = TweetDatabaseController.getInstance().getDataModel().get(rowNumber);
        List<Tweet> userTweets = user.getTweets();

        String tweetStr = "";

        for(Tweet tweet : userTweets) {
            tweetStr += tweet + "\n\n";
        }
        
        this.tweets.setText(tweetStr);
    }
    /**
     * 
     * @return 
     */
    private JPanel createAddPanel() {
        JPanel addPanel = new JPanel(new MigLayout());
        addPanel.add(new JLabel(TweetDbFrame.USERNAME_LABEL));
        addPanel.add(this.usernameField, "wrap");
        addPanel.add(new JLabel(TweetDbFrame.COUNTRY_LABEL));
        addPanel.add(this.countryField);
        
        return addPanel;
    }

    /**
     * Add new tweet dialog. Customized JOptionPane to enter new tweet. Controller
     * is inserting new tweet into list and DB.
     */
    private class AddTweetDialog {

        public AddTweetDialog() {
            //Create a custom JOptionPane to create a new tweet.
            JPanel addTweetPanel = new JPanel(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            
            int rowNumber = TweetDbFrame.this.userTable.getSelectedRow();
            String username = TweetDatabaseController.getInstance().getDataModel().get(rowNumber).getUsername();
            
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = 0;
            addTweetPanel.add(new JLabel(username + TweetDbFrame.ADD_NEW_TWEET_LABEL), c);
            
            TweetDbFrame.this.tweetArea = new JTextArea(4, 30);
            TweetDbFrame.this.tweetArea.setBackground(new Color(255, 255, 153));
            TweetDbFrame.this.tweetArea.setBorder(BorderFactory.createLineBorder(new Color(0, 128, 255)));
            TweetDbFrame.this.tweetArea.setLineWrap (true);
            TweetDbFrame.this.tweetArea.setWrapStyleWord(true);
            
            TweetDbFrame.this.remaningLabel = new JLabel(140 + TweetDbFrame.CHARACTERS_REMAINING_LABEL);
            
            //DocumentFilter attached to track number of characters remaining
            //And to not allow typing after 140 characters were entered
            //Please see DocumentSizeFilter (Oracle) class for more details
            DefaultStyledDocument doc = new DefaultStyledDocument();
            doc.setDocumentFilter(new DocumentSizeFilter(140));
            doc.addDocumentListener(new DocumentListener(){
                @Override
                public void changedUpdate(DocumentEvent e) { 
                    updateCount();
                }
                @Override
                public void insertUpdate(DocumentEvent e) { 
                    updateCount();
                }
                @Override
                public void removeUpdate(DocumentEvent e) { 
                    updateCount();
                }
                
                private void updateCount() {
                    TweetDbFrame.this.remaningLabel.setText((140 - doc.getLength()) + TweetDbFrame.CHARACTERS_REMAINING_LABEL);
                    if(doc.getLength() == 140) {
                        TweetDbFrame.this.remaningLabel.setText("<html><span style='color: red;'>0</span>" + TweetDbFrame.CHARACTERS_REMAINING_LABEL + "</html>");
                    }
                }
            });
            TweetDbFrame.this.tweetArea.setDocument(doc);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.ipady = 20;
            c.weightx = 0.0;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 1;
            addTweetPanel.add(TweetDbFrame.this.tweetArea, c);
            
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = 2;
            addTweetPanel.add(TweetDbFrame.this.remaningLabel, c);
            
            
            int result = JOptionPane.showConfirmDialog(TweetDbFrame.this, addTweetPanel, TweetDbFrame.ADD_NEW_TWEET_TITLE, JOptionPane.OK_CANCEL_OPTION);
            
            if(result == JOptionPane.OK_OPTION) {
                try {
                    Tweet newTweet = new Tweet(doc.getText(0, doc.getLength()));
                    TweetDatabaseController.getInstance().addTweetForUser(newTweet, username, rowNumber);
                    TweetDatabaseController.getInstance().getGuiReference().refreshTweets(rowNumber);
                    
                } catch (BadLocationException ex) {
                    JOptionPane.showMessageDialog(null, ex, TweetDbFrame.ERROR_DIALOG_TITLE, JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        
        
        
    }

    

}
