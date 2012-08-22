/**
   This file is part of Waarp Project.

   Copyright 2009, Frederic Bregier, and individual contributors by the @author
   tags. See the COPYRIGHT.txt in the distribution for a full listing of
   individual contributors.

   All Waarp Project is free software: you can redistribute it and/or 
   modify it under the terms of the GNU General Public License as published 
   by the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   Waarp is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with Waarp .  If not, see <http://www.gnu.org/licenses/>.
 */
package org.waarp.openr66.r66gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import org.waarp.common.logging.WaarpInternalLogger;
import org.waarp.common.logging.WaarpInternalLoggerFactory;

import com.swtdesigner.FocusTraversalOnArray;

/**
 * R66 Client GUI to show how to use the API and also to enable to test the connectivity to R66 servers 
 * and the validity of a transfer through a rule.
 * 
 * @author Frederic Bregier
 *
 */
public class R66ClientGui {
	/**
     * Internal Logger
     */
    private static final WaarpInternalLogger logger = WaarpInternalLoggerFactory
            .getLogger(R66ClientGui.class);


    public static String []static_args;
    public static R66ClientGui window;
    
    private JFrame frmRClientGui;
    private JTextField textFieldInformation;
    private JTextField textFieldFile;
    private R66Environment environnement = new R66Environment();
    private JEditorPane textFieldStatus;
    private JComboBox comboBoxHosts;
    private JComboBox comboBoxRules;
    private JCheckBox checkBoxMD5;
    private JProgressBar progressBarTransfer;
    private JButton buttonTransferStart;
    private JMenu menu;
    private JButton buttonCheckConnection;
    private JButton buttonFileFind;
    private R66Dialog dialog;
    private JTextArea textPaneLog;
    private JScrollPane scrollPane;
    private JScrollPane scrollPane_1;
    private JCheckBox checkBoxDebug;
    
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        static_args = args;
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    window = new R66ClientGui(static_args);
                    window.frmRClientGui.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public R66ClientGui(String []args) {
        environnement.initialize(args);
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        String [] shosts = R66Environment.getHostIds();
        String [] srules = R66Environment.getRules();
        
        frmRClientGui = new JFrame();
        frmRClientGui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                environnement.exit();
                System.exit(0);
            }
        });
        frmRClientGui.setTitle("R66 Client Gui");
        frmRClientGui.setBounds(100, 100, 724, 546);
        frmRClientGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JMenuBar menuBar = new JMenuBar();
        frmRClientGui.setJMenuBar(menuBar);
        
        menu = new JMenu("Menu");
        menuBar.add(menu);
        
        JMenuItem menuItemExit = new JMenuItem("Exit");
        menuItemExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                environnement.exit();
                System.exit(0);
            }
        });
        menu.add(menuItemExit);
        
        JSeparator separator = new JSeparator();
        menu.add(separator);
        
        JMenuItem menuItemHelp = new JMenuItem("Help");
        menuItemHelp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                environnement.about();
                showDialog();
            }
        });
        menu.add(menuItemHelp);
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{24, 130, 80, 369, 99, 0};
        gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 27, 179, 162};
        gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0};
        frmRClientGui.getContentPane().setLayout(gridBagLayout);
        
        buttonCheckConnection = new JButton("Check Connection");
        buttonCheckConnection.setToolTipText("Check the connectivity with the selected Host by sending a simple message");
        buttonCheckConnection.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                R66ClientGuiActions action = new R66ClientGuiActions(R66ClientGuiActions.CHECKCONNECTION);
                action.execute();
            }
        });
        GridBagConstraints gbc_buttonCheckConnection = new GridBagConstraints();
        gbc_buttonCheckConnection.insets = new Insets(0, 0, 5, 5);
        gbc_buttonCheckConnection.gridx = 1;
        gbc_buttonCheckConnection.gridy = 0;
        frmRClientGui.getContentPane().add(buttonCheckConnection, gbc_buttonCheckConnection);
        
        JLabel label = new JLabel("Host Id");
        GridBagConstraints gbc_label = new GridBagConstraints();
        gbc_label.insets = new Insets(0, 0, 5, 5);
        gbc_label.gridx = 2;
        gbc_label.gridy = 0;
        frmRClientGui.getContentPane().add(label, gbc_label);
        
        comboBoxHosts = new JComboBox(shosts);
        comboBoxHosts.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                environnement.GuiResultat = R66Environment.getHost((String) comboBoxHosts.getSelectedItem());
                setStatus(environnement.GuiResultat);
            }
        });
        label.setLabelFor(comboBoxHosts);
        comboBoxHosts.setToolTipText("Select a host on which you want to test the connectivity or send a request of transfer");
        GridBagConstraints gbc_comboBoxHosts = new GridBagConstraints();
        gbc_comboBoxHosts.fill = GridBagConstraints.HORIZONTAL;
        gbc_comboBoxHosts.insets = new Insets(0, 0, 5, 5);
        gbc_comboBoxHosts.gridx = 3;
        gbc_comboBoxHosts.gridy = 0;
        frmRClientGui.getContentPane().add(comboBoxHosts, gbc_comboBoxHosts);
        
        JLabel label_1 = new JLabel("Rule");
        GridBagConstraints gbc_label_1 = new GridBagConstraints();
        gbc_label_1.insets = new Insets(0, 0, 5, 5);
        gbc_label_1.gridx = 2;
        gbc_label_1.gridy = 1;
        frmRClientGui.getContentPane().add(label_1, gbc_label_1);
        
        comboBoxRules = new JComboBox(srules);
        comboBoxRules.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                environnement.GuiResultat = R66Environment.getRule((String) comboBoxRules.getSelectedItem());
                setStatus(environnement.GuiResultat);
            }
        });
        label_1.setLabelFor(comboBoxRules);
        comboBoxRules.setToolTipText("Select a Rule to use in case if a request of transfer");
        GridBagConstraints gbc_comboBoxRules = new GridBagConstraints();
        gbc_comboBoxRules.fill = GridBagConstraints.HORIZONTAL;
        gbc_comboBoxRules.insets = new Insets(0, 0, 5, 5);
        gbc_comboBoxRules.gridx = 3;
        gbc_comboBoxRules.gridy = 1;
        frmRClientGui.getContentPane().add(comboBoxRules, gbc_comboBoxRules);
        
        checkBoxMD5 = new JCheckBox("MD5");
        checkBoxMD5.setToolTipText("Checked if you want that all packets are checked using MD5 (optional and not recommended if already using SSL)");
        GridBagConstraints gbc_checkBoxMD5 = new GridBagConstraints();
        gbc_checkBoxMD5.insets = new Insets(0, 0, 5, 0);
        gbc_checkBoxMD5.gridx = 4;
        gbc_checkBoxMD5.gridy = 1;
        frmRClientGui.getContentPane().add(checkBoxMD5, gbc_checkBoxMD5);
        
        JLabel label_2 = new JLabel("Information");
        GridBagConstraints gbc_label_2 = new GridBagConstraints();
        gbc_label_2.insets = new Insets(0, 0, 5, 5);
        gbc_label_2.gridx = 2;
        gbc_label_2.gridy = 2;
        frmRClientGui.getContentPane().add(label_2, gbc_label_2);
        
        textFieldInformation = new JTextField();
        label_2.setLabelFor(textFieldInformation);
        textFieldInformation.setToolTipText("Information to provide in the field info of the request to the remote host");
        GridBagConstraints gbc_textFieldInformation = new GridBagConstraints();
        gbc_textFieldInformation.weightx = 1.0;
        gbc_textFieldInformation.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldInformation.gridwidth = 2;
        gbc_textFieldInformation.insets = new Insets(0, 0, 5, 0);
        gbc_textFieldInformation.gridx = 3;
        gbc_textFieldInformation.gridy = 2;
        frmRClientGui.getContentPane().add(textFieldInformation, gbc_textFieldInformation);
        textFieldInformation.setColumns(10);
        
        JLabel label_3 = new JLabel("File");
        GridBagConstraints gbc_label_3 = new GridBagConstraints();
        gbc_label_3.insets = new Insets(0, 0, 5, 5);
        gbc_label_3.gridx = 2;
        gbc_label_3.gridy = 3;
        frmRClientGui.getContentPane().add(label_3, gbc_label_3);
        
        textFieldFile = new JTextField();
        textFieldFile.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                setFindFile();
            }
        });
        label_3.setLabelFor(textFieldFile);
        textFieldFile.setToolTipText("File reference to send or receive. It might be a full path or a relative path. Note that \" \" might be necessary if blank characters occur.");
        GridBagConstraints gbc_textFieldFile = new GridBagConstraints();
        gbc_textFieldFile.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldFile.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldFile.gridx = 3;
        gbc_textFieldFile.gridy = 3;
        frmRClientGui.getContentPane().add(textFieldFile, gbc_textFieldFile);
        textFieldFile.setColumns(10);
        
        buttonFileFind = new JButton("File Find");
        buttonFileFind.setToolTipText("Helper to find a local file to send");
        buttonFileFind.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                R66ClientGuiActions action = new R66ClientGuiActions(R66ClientGuiActions.FILESELECT);
                action.execute();
            }
        });
        GridBagConstraints gbc_buttonFileFind = new GridBagConstraints();
        gbc_buttonFileFind.insets = new Insets(0, 0, 5, 0);
        gbc_buttonFileFind.gridx = 4;
        gbc_buttonFileFind.gridy = 3;
        frmRClientGui.getContentPane().add(buttonFileFind, gbc_buttonFileFind);
        
        buttonTransferStart = new JButton("Starts Transfer");
        buttonTransferStart.setToolTipText("Starts the request of transfer according to the above options");
        buttonTransferStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                R66ClientGuiActions action = new R66ClientGuiActions(R66ClientGuiActions.STARTTRANSFER);
                action.execute();
            }
        });
        GridBagConstraints gbc_buttonTransferStart = new GridBagConstraints();
        gbc_buttonTransferStart.insets = new Insets(0, 0, 5, 5);
        gbc_buttonTransferStart.gridx = 3;
        gbc_buttonTransferStart.gridy = 4;
        frmRClientGui.getContentPane().add(buttonTransferStart, gbc_buttonTransferStart);
        
        checkBoxDebug = new JCheckBox("Debug");
        checkBoxDebug.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                environnement.debug(checkBoxDebug.isSelected());
            }
        });
        GridBagConstraints gbc_checkBoxDebug = new GridBagConstraints();
        gbc_checkBoxDebug.insets = new Insets(0, 0, 5, 5);
        gbc_checkBoxDebug.gridx = 1;
        gbc_checkBoxDebug.gridy = 5;
        environnement.debug(checkBoxDebug.isSelected());
        frmRClientGui.getContentPane().add(checkBoxDebug, gbc_checkBoxDebug);
        
        progressBarTransfer = new JProgressBar();
        GridBagConstraints gbc_progressBarTransfer = new GridBagConstraints();
        gbc_progressBarTransfer.weightx = 1.0;
        gbc_progressBarTransfer.fill = GridBagConstraints.HORIZONTAL;
        gbc_progressBarTransfer.insets = new Insets(0, 0, 5, 0);
        gbc_progressBarTransfer.gridwidth = 3;
        gbc_progressBarTransfer.gridx = 2;
        gbc_progressBarTransfer.gridy = 5;
        frmRClientGui.getContentPane().add(progressBarTransfer, gbc_progressBarTransfer);
        progressBarTransfer.setVisible(false);
        
        scrollPane_1 = new JScrollPane();
        scrollPane_1.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
        gbc_scrollPane_1.weighty = 1.0;
        gbc_scrollPane_1.weightx = 1.0;
        gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
        gbc_scrollPane_1.gridwidth = 5;
        gbc_scrollPane_1.insets = new Insets(0, 0, 5, 0);
        gbc_scrollPane_1.gridx = 0;
        gbc_scrollPane_1.gridy = 6;
        frmRClientGui.getContentPane().add(scrollPane_1, gbc_scrollPane_1);
        
        textFieldStatus = new JEditorPane();
        textFieldStatus.setToolTipText("Result of last command");
        scrollPane_1.setViewportView(textFieldStatus);
        textFieldStatus.setForeground(Color.GRAY);
        textFieldStatus.setBackground(new Color(255, 255, 153));
        textFieldStatus.setContentType("text/html");
        textFieldStatus.setEditable(false);
        
        scrollPane = new JScrollPane();
        scrollPane.setViewportBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.weighty = 1.0;
        gbc_scrollPane.weightx = 1.0;
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridwidth = 5;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 7;
        frmRClientGui.getContentPane().add(scrollPane, gbc_scrollPane);
        
        textPaneLog = new JTextArea();
        scrollPane.setViewportView(textPaneLog);
        textPaneLog.setToolTipText("Output of internal commands of R66");
        textPaneLog.setEditable(false);
        
        System.setOut(new PrintStream(new JTextAreaOutputStream(textPaneLog)));
        frmRClientGui.getContentPane().setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{buttonCheckConnection, comboBoxHosts, comboBoxRules, checkBoxMD5, textFieldInformation, textFieldFile, buttonFileFind, buttonTransferStart}));
    }

    /**
     * @author Frederic Bregier
     *
     */
    public class R66ClientGuiActions extends SwingWorker<String, Integer>{
        static final int CHECKCONNECTION = 1;
        static final int STARTTRANSFER = 2;
        static final int FILESELECT = 3;
        int method;
        R66ClientGuiActions(int method) {
            this.method = method;
        }
        /* (non-Javadoc)
         * @see javax.swing.SwingWorker#doInBackground()
         */
        @Override
        protected String doInBackground() throws Exception {
            disableAllButtons();
            startRequest();
            switch (method) {
                case CHECKCONNECTION:
                    checkConnection();
                    break;
                case STARTTRANSFER:
                    startTransfer();
                    break;
                case FILESELECT:
                    findFile();
                    break;
                default:
                    environnement.GuiResultat = "Action not recognized";
            }
            setStatus(environnement.GuiResultat);
            if (method != FILESELECT) {
                showDialog();
            } else {
                enableAllButtons();
            }
            stopRequest();
            return environnement.GuiResultat;
        }
    }
    
    private void showDialog() {
        disableAllButtons();
        if (dialog != null) {
            dialog.dispose();
            dialog = null;
        }
        if (dialog == null) {
            dialog = new R66Dialog();
            dialog.setLocationRelativeTo(frmRClientGui);
            if (dialog.isAlwaysOnTopSupported()) {
                dialog.setAlwaysOnTop(true);
            } else {
                dialog.toFront();
            }
        }
        dialog.textPaneDialog.setText(environnement.GuiResultat);
        dialog.setVisible(true);
        dialog.requestFocus();
    }
    private void setStatus(String mesg) {
        textFieldStatus.setText(mesg);
    }
    private void startRequest() {
        progressBarTransfer.setIndeterminate(true);
        progressBarTransfer.setValue(0);
        progressBarTransfer.setVisible(true);
        textPaneLog.setText("");
    }
    private void stopRequest() {
        progressBarTransfer.setIndeterminate(true);
        progressBarTransfer.setValue(0);
        progressBarTransfer.setVisible(false);
        frmRClientGui.toFront();
        frmRClientGui.requestFocus();
    }
    private void checkConnection() {
        startRequest();
        disableAllButtons();
        environnement.hostId = (String) comboBoxHosts.getSelectedItem();
        if (environnement.hostId == null || environnement.hostId.trim().length() <= 0) {
            environnement.hostId = "NO HOST";
            environnement.GuiResultat = "No Host specified!";
        } else {
            environnement.hostId = environnement.hostId.trim();
            environnement.checkConnection();
        }
        setStatus(environnement.GuiResultat);
        showDialog();
        stopRequest();
    }
    private void startTransfer() {
        disableAllButtons();
        environnement.hostId = (String) comboBoxHosts.getSelectedItem();
        environnement.ruleId = (String) comboBoxRules.getSelectedItem();
        environnement.filePath = textFieldFile.getText();
        environnement.information = textFieldInformation.getText();
        environnement.isMD5 = checkBoxMD5.isSelected();
        
        boolean ok = true;
        if (environnement.hostId == null || environnement.hostId.trim().length() <= 0) {
            environnement.hostId = "NO HOST";
            ok = false;
        } else {
            environnement.hostId = environnement.hostId.trim();
        }
        if (environnement.filePath == null || environnement.filePath.trim().length() <= 0) {
            environnement.filePath = "NO FILE";
            ok = false;
        } else {
            environnement.filePath = environnement.filePath.trim();
        }
        if (environnement.information == null || environnement.information.trim().length() <= 0) {
            environnement.information = "";
        } else {
            environnement.information = environnement.information.trim();
        }
        if (environnement.ruleId == null || environnement.ruleId.trim().length() <= 0) {
            environnement.ruleId = "NO RULE";
            ok = false;
        } else {
            environnement.ruleId = environnement.ruleId.trim();
        }
        if (ok) {
            environnement.startsTransfer(progressBarTransfer, textFieldStatus);
        } else {
            environnement.GuiResultat = "<html>Not enough arg to start the transfer:<br>   "+
                "HostId: "+environnement.hostId+" Rule: "+environnement.ruleId+
                " File: "+environnement.filePath;
        }
        setStatus(environnement.GuiResultat);
        showDialog();
    }
    private void findFile() {
        startRequest();
        disableAllButtons();
        try {
            JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(frmRClientGui);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                try {
					textFieldFile.setText(file.getCanonicalPath());
				} catch (IOException e) {
				}
                setFindFile();
                environnement.GuiResultat = "New file sets";
            }
        } finally {
            enableAllButtons();
            stopRequest();
        }
    }
    private void setFindFile() {
        String text = null;
        try {
            text = textFieldFile.getText();
        } catch (NullPointerException e1) {
            text = null;
        }
        if (text != null) {
    		File file = new File(text);
    		logger.debug("File: "+text+" : "+file.toURI().toString());
    		if (file.exists()) {
				text = file.toURI().toString();
    		} else {
    			text = "unknown file";
    		}
            textFieldFile.setText(text);
        }
    }
    public void disableAllButtons() {
        //frmRClientGui.setEnabled(false);
        buttonCheckConnection.setEnabled(false);
        buttonFileFind.setEnabled(false);
        buttonTransferStart.setEnabled(false);
        menu.setEnabled(false);
        textFieldInformation.setEnabled(false);
        textFieldFile.setEnabled(false);
        comboBoxHosts.setEnabled(false);
        comboBoxRules.setEnabled(false);
        checkBoxMD5.setEnabled(false);
        checkBoxDebug.setEnabled(false);
    }
    public void enableAllButtons() {
        //frmRClientGui.setEnabled(true);
        buttonCheckConnection.setEnabled(true);
        buttonFileFind.setEnabled(true);
        buttonTransferStart.setEnabled(true);
        menu.setEnabled(true);
        textFieldInformation.setEnabled(true);
        textFieldFile.setEnabled(true);
        comboBoxHosts.setEnabled(true);
        comboBoxRules.setEnabled(true);
        checkBoxMD5.setEnabled(true);
        checkBoxDebug.setEnabled(true);
        frmRClientGui.toFront();
    }
    
    public class JTextAreaOutputStream extends OutputStream {
        JTextArea ta;

        public JTextAreaOutputStream(JTextArea t) {
          super();
          ta = t;
        }

        public void write(int i) {
          ta.append(Character.toString((char)i));
        }

        public void write(char[] buf, int off, int len) {
          String s = new String(buf, off, len);
          ta.append(s);
        }

      }
}
