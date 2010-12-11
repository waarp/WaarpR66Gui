/**
   This file is part of GoldenGate Project (named also GoldenGate or GG).

   Copyright 2009, Frederic Bregier, and individual contributors by the @author
   tags. See the COPYRIGHT.txt in the distribution for a full listing of
   individual contributors.

   All GoldenGate Project is free software: you can redistribute it and/or 
   modify it under the terms of the GNU General Public License as published 
   by the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   GoldenGate is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with GoldenGate .  If not, see <http://www.gnu.org/licenses/>.
 */

package openr66.clientgui;

import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 * The application's main frame.
 */
public class R66ClientGuiView extends FrameView {

    public R66ClientGuiView(SingleFrameApplication app) {
        super(app);
        String [] hosts = R66ClientGui.getHostIds();
        hostIds = new DefaultComboBoxModel(hosts);
        String [] srules = R66ClientGui.getRules();
        rules = new DefaultComboBoxModel(srules);
        initComponents();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        busyIconTimer.setInitialDelay(busyAnimationRate);
        busyIconTimer.setCoalesce(true);
        busyIconTimer.setRepeats(true);
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = R66ClientGuiApp.getApplication().getMainFrame();
            aboutBox = new R66ClientGuiAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        R66ClientGuiApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jComboBoxHostId = new javax.swing.JComboBox();
        jComboBoxRule = new javax.swing.JComboBox();
        jCheckBoxMD5 = new javax.swing.JCheckBox();
        jTextFieldInformation = new javax.swing.JTextField();
        jTextFieldFilepath = new javax.swing.JTextField();
        jButtonFindFile = new javax.swing.JButton();
        jButtonCheckConnection = new javax.swing.JButton();
        jButtonStartTransfer = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setName("mainPanel"); // NOI18N

        jComboBoxHostId.setModel(hostIds);
        jComboBoxHostId.setName("jComboBoxHostId"); // NOI18N

        jComboBoxRule.setModel(rules);
        jComboBoxRule.setName("jComboBoxRule"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(openr66.clientgui.R66ClientGuiApp.class).getContext().getResourceMap(R66ClientGuiView.class);
        jCheckBoxMD5.setText(resourceMap.getString("jCheckBoxMD5.text")); // NOI18N
        jCheckBoxMD5.setName("jCheckBoxMD5"); // NOI18N

        jTextFieldInformation.setText(resourceMap.getString("jTextFieldInformation.text")); // NOI18N
        jTextFieldInformation.setName("jTextFieldInformation"); // NOI18N

        jTextFieldFilepath.setText(resourceMap.getString("jTextFieldFilepath.text")); // NOI18N
        jTextFieldFilepath.setName("jTextFieldFilepath"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(openr66.clientgui.R66ClientGuiApp.class).getContext().getActionMap(R66ClientGuiView.class, this);
        jButtonFindFile.setAction(actionMap.get("findFile")); // NOI18N
        jButtonFindFile.setText(resourceMap.getString("jButtonFindFile.text")); // NOI18N
        jButtonFindFile.setName("jButtonFindFile"); // NOI18N

        jButtonCheckConnection.setAction(actionMap.get("checkConnection")); // NOI18N
        jButtonCheckConnection.setText(resourceMap.getString("jButtonCheckConnection.text")); // NOI18N
        jButtonCheckConnection.setName("jButtonCheckConnection"); // NOI18N

        jButtonStartTransfer.setAction(actionMap.get("startsTransfer")); // NOI18N
        jButtonStartTransfer.setText(resourceMap.getString("jButtonStartTransfer.text")); // NOI18N
        jButtonStartTransfer.setName("jButtonStartTransfer"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(jButtonCheckConnection)
                        .addGap(22, 22, 22)
                        .addComponent(jLabel1))
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonStartTransfer)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jComboBoxHostId, javax.swing.GroupLayout.Alignment.LEADING, 0, 311, Short.MAX_VALUE)
                            .addComponent(jComboBoxRule, 0, 311, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(jCheckBoxMD5))
                    .addComponent(jTextFieldInformation, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(jTextFieldFilepath, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonFindFile)))
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxHostId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jButtonCheckConnection))
                .addGap(26, 26, 26)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxRule, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBoxMD5)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldInformation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(26, 26, 26)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldFilepath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonFindFile)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addComponent(jButtonStartTransfer)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 432, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @author Frederic Bregier
     *
     */
    public class R66ClientGuiActions extends SwingWorker<String, Integer>{
        static final int CHECKCONNECTION = 1;
        static final int STARTTRANSFER = 2;
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
            if (!busyIconTimer.isRunning()) {
                busyIconTimer.setRepeats(true);
                statusAnimationLabel.setIcon(busyIcons[0]);
                busyIconIndex = 0;
                busyIconTimer.start();
            }
            mainPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            switch (method) {
                case CHECKCONNECTION:
                    R66ClientGui.checkConnection();
                    break;
                case STARTTRANSFER:
                    R66ClientGui.startsTransfer();
                    break;
                default:
                    R66ClientGui.GuiResultat = "Action not recognized";
            }
            if (checkConnBox == null) {
                JFrame mainFrame = R66ClientGuiApp.getApplication().getMainFrame();
                checkConnBox = new R66ClientGuiCheckConnectionBox(mainFrame);
                checkConnBox.setLocationRelativeTo(mainFrame);
            }
            mainPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            R66ClientGuiApp.getApplication().show(checkConnBox);
            checkConnBox = null;
            enableAllButtons();
            busyIconTimer.stop();
            statusAnimationLabel.setIcon(idleIcon);
            return R66ClientGui.GuiResultat;
        }
    }

    public void disableAllButtons() {
        jButtonCheckConnection.setEnabled(false);
        jButtonFindFile.setEnabled(false);
        jButtonStartTransfer.setEnabled(false);
    }
    public void enableAllButtons() {
        jButtonCheckConnection.setEnabled(true);
        jButtonFindFile.setEnabled(true);
        jButtonStartTransfer.setEnabled(true);
    }
    @Action
    public void checkConnection() {
        if (checkConnBox != null) {
            checkConnBox.dispose();
            checkConnBox = null;
        }
        R66ClientGui.GuiHostId = (String) this.jComboBoxHostId.getSelectedItem();
        if (R66ClientGui.GuiHostId == null || R66ClientGui.GuiHostId.trim().length() <= 0) {
            R66ClientGui.GuiHostId = "NO HOST";
            R66ClientGui.GuiResultat = "No Host specified!";
            if (checkConnBox == null) {
                JFrame mainFrame = R66ClientGuiApp.getApplication().getMainFrame();
                checkConnBox = new R66ClientGuiCheckConnectionBox(mainFrame);
                checkConnBox.setLocationRelativeTo(mainFrame);
            }
            R66ClientGuiApp.getApplication().show(checkConnBox);
            checkConnBox = null;
        } else {
            R66ClientGui.GuiHostId = R66ClientGui.GuiHostId.trim();
            R66ClientGuiActions action =
                new R66ClientGuiActions(R66ClientGuiActions.CHECKCONNECTION);
            action.execute();
        }
    }

    @Action
    public void findFile() {
        disableAllButtons();
        if (!busyIconTimer.isRunning()) {
            busyIconTimer.setRepeats(true);
            statusAnimationLabel.setIcon(busyIcons[0]);
            busyIconIndex = 0;
            busyIconTimer.start();
        }
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(R66ClientGuiApp.getApplication().getMainFrame());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            jTextFieldFilepath.setText(file.getAbsolutePath());
        }
        enableAllButtons();
        busyIconTimer.stop();
        statusAnimationLabel.setIcon(idleIcon);
    }

    @Action
    public void startsTransfer() {
        if (checkConnBox != null) {
            checkConnBox.dispose();
            checkConnBox = null;
        }
        boolean ok = true;
        R66ClientGui.GuiHostId = (String) this.jComboBoxHostId.getSelectedItem();
        if (R66ClientGui.GuiHostId == null || R66ClientGui.GuiHostId.trim().length() <= 0) {
            R66ClientGui.GuiHostId = "NO HOST";
            ok = false;
        } else {
            R66ClientGui.GuiHostId = R66ClientGui.GuiHostId.trim();
        }
        R66ClientGui.GuiFile = this.jTextFieldFilepath.getText();
        if (R66ClientGui.GuiFile == null || R66ClientGui.GuiFile.trim().length() <= 0) {
            R66ClientGui.GuiFile = "NO FILE";
            ok = false;
        } else {
            R66ClientGui.GuiFile = R66ClientGui.GuiFile.trim();
        }
        R66ClientGui.GuiInfo = this.jTextFieldInformation.getText();
        if (R66ClientGui.GuiInfo == null || R66ClientGui.GuiInfo.trim().length() <= 0) {
            R66ClientGui.GuiInfo = "noinfo";
        } else {
            R66ClientGui.GuiInfo = R66ClientGui.GuiInfo.trim();
        }
        R66ClientGui.GuiRule = (String) this.jComboBoxRule.getSelectedItem();
        if (R66ClientGui.GuiRule == null || R66ClientGui.GuiRule.trim().length() <= 0) {
            R66ClientGui.GuiRule = "NO RULE";
            ok = false;
        } else {
            R66ClientGui.GuiRule = R66ClientGui.GuiRule.trim();
        }
        R66ClientGui.GuiMd5 = this.jCheckBoxMD5.isSelected();
        if (ok) {
            R66ClientGuiActions action =
                new R66ClientGuiActions(R66ClientGuiActions.STARTTRANSFER);
            action.execute();
        } else {
            R66ClientGui.GuiResultat = "<html>Not enough arg to start the transfer:<br>   "+
                "HostId: "+R66ClientGui.GuiHostId+" Rule: "+R66ClientGui.GuiRule+
                " File: "+R66ClientGui.GuiFile;
            if (checkConnBox == null) {
                JFrame mainFrame = R66ClientGuiApp.getApplication().getMainFrame();
                checkConnBox = new R66ClientGuiCheckConnectionBox(mainFrame);
                checkConnBox.setLocationRelativeTo(mainFrame);
            }
            R66ClientGuiApp.getApplication().show(checkConnBox);
            checkConnBox = null;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCheckConnection;
    private javax.swing.JButton jButtonFindFile;
    private javax.swing.JButton jButtonStartTransfer;
    private javax.swing.JCheckBox jCheckBoxMD5;
    private javax.swing.JComboBox jComboBoxHostId;
    private javax.swing.JComboBox jComboBoxRule;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField jTextFieldFilepath;
    private javax.swing.JTextField jTextFieldInformation;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;
    private JDialog checkConnBox;
    private DefaultComboBoxModel hostIds;
    private DefaultComboBoxModel rules;
}
