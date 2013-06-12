/*
 * ParameterLearner.java
 *
 * Created on Jun 7, 2013, 4:32:39 PM
 */
package de.naoth.rc.dialogs;

import de.naoth.rc.AbstractDialog;
import de.naoth.rc.RobotControl;
import de.naoth.rc.server.Command;
import de.naoth.rc.server.CommandSender;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.events.Init;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;

/**
 *
 * @author auke
 */
@PluginImplementation
public class ParameterLearner extends AbstractDialog implements CommandSender {

  private Command commandToExecute;
  private final String strIKParameters = "ParameterList:IKParameters";
  private final String strMLParameters = "ParameterList:MachineLearningParameters";
  
  @InjectPlugin
  public RobotControl parent;

  Map<String, String> commandMap = new HashMap<String, String>();

    /** Creates new form ParameterLearner */
     public ParameterLearner() {
        initComponents();
        // LOL HACKS
        cbLearningMethod.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "" }));
        
        jTextAreaLearningParams.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e)
            { 
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER)
                {
                    sendLearningParameters();

                    int k = jTextAreaLearningParams.getCaretPosition();
                    if(k > 0)
                        jTextAreaLearningParams.setCaretPosition(k-1);
                }
            }
        });
        
        jTextAreaWalkingParams.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e)
            { 
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER)
                {
                    saveWalkingParameters();

                    int k = jTextAreaWalkingParams.getCaretPosition();
                    if(k > 0)
                        jTextAreaWalkingParams.setCaretPosition(k-1);
                }
            }
        });
    }

  @Init
  public void init()
  {
    if(parent == null)
    {
      throw (new IllegalArgumentException("\"parent\" was null"));
    }
  }

  @Override
  public JPanel getPanel()
  {
    return this;
  }

private static Command parseTextArea(String cmdName, String text)
{
    return parseTextArea(cmdName, text, "");
} // end parseTextArea

private static Command parseTextArea(String cmdName, String text, String method)
{
    Command cmd = new Command(cmdName);
    
    text = text.replaceAll("( |\t)+", "");
    String[] lines = text.split("(\n)+");
    if (!method.isEmpty())
        method += ".";
    
    for (String l : lines)
    {
      String[] splitted = l.split("=");
      if (splitted.length == 2)
      {
        String key = method + splitted[0].trim();
        String value = splitted[1].trim();
        // remove the last ;
        if (value.charAt(value.length() - 1) == ';')
        {
          value = value.substring(0, value.length() - 1);
        }

        cmd.addArg(key, value);
      }
    } // end for
    return cmd;
} // end parseTextArea
  
  
private void saveWalkingParameters() {
  if (parent.checkConnected())
  {
    getWalkingParameterList();
    Command cmd = parseTextArea(strIKParameters + ":set", 
                                this.jTextAreaWalkingParams.getText());
    sendCommand(cmd);
    // stop learning when walking parameters are saved , for now
    // TODO save somewhere externally, or under some specific name, e.g.
    DateFormat dateFormat = new SimpleDateFormat("_yy/MM/dd_HHmmss");
    String savefilename = cbLearningMethod.getSelectedItem().toString() + 
                            dateFormat.format(new Date());
                          
    jToggleButtonLearn.setSelected(false);
  }
  else
  {
    jToggleButtonLearn.setSelected(false);
  }    
}

private boolean getParameterList(String strCommand)
{
    if (parent.checkConnected())
    {
      Command cmd = new Command(strCommand);
      sendCommand(cmd);
      return true;
    }
    return false;
}

private void getWalkingParameterList() {
    if(!getParameterList(strIKParameters + ":get"))
    {
         jToggleButtonReceive.setSelected(false);
    } 
}//end getWalkingParameterList

private void getLearningParameterList() {
    if (cbLearningMethod.getSelectedItem() != null) {
        if(!getParameterList(strMLParameters + ":get"))
        {
            jToggleButtonLearn.setSelected(false);
        }
    }
}//end getLearningParameterList

private void sendLearningParameters()
{
  if (parent.checkConnected())
  {
     // TODO add combobox methsendCommandod name, send correct parameters only
    Command cmd = parseTextArea(strMLParameters + ":set",
            this.jTextAreaLearningParams.getText(), 
            cbLearningMethod.getSelectedItem().toString());

    sendCommand(cmd);
    getLearningParameterList();
  }
  else
  {
    jToggleButtonLearn.setSelected(false);
  }
}

private void sendCommand(Command command)
  {
    commandToExecute = command;
    parent.getMessageServer().executeSingleCommand(this, command);
  }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaWalkingParams = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaLearningParams = new javax.swing.JTextArea();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0));
        jLabelInfoDisplay = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        jToggleButtonReceive = new javax.swing.JToggleButton();
        jButtonSave = new javax.swing.JButton();
        jToolBar2 = new javax.swing.JToolBar();
        jToggleButtonLearn = new javax.swing.JToggleButton();
        jButtonSetLP = new javax.swing.JButton();
        jToggleButtonList = new javax.swing.JToggleButton();
        cbLearningMethod = new javax.swing.JComboBox();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTable1);

        jTextAreaWalkingParams.setColumns(20);
        jTextAreaWalkingParams.setRows(5);
        jScrollPane1.setViewportView(jTextAreaWalkingParams);

        jTextAreaLearningParams.setColumns(20);
        jTextAreaLearningParams.setRows(5);
        jScrollPane2.setViewportView(jTextAreaLearningParams);

        jLabelInfoDisplay.setText("Infodisplay");

        jToolBar1.setRollover(true);

        jToggleButtonReceive.setText("Receive");
        jToggleButtonReceive.setFocusable(false);
        jToggleButtonReceive.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButtonReceive.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButtonReceive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonReceiveActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButtonReceive);

        jButtonSave.setText("Save");
        jButtonSave.setFocusable(false);
        jButtonSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonSaveMouseClicked(evt);
            }
        });
        jToolBar1.add(jButtonSave);

        jToolBar2.setRollover(true);

        jToggleButtonLearn.setText("Learn");
        jToggleButtonLearn.setFocusable(false);
        jToggleButtonLearn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButtonLearn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButtonLearn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonLearnActionPerformed(evt);
            }
        });
        jToolBar2.add(jToggleButtonLearn);

        jButtonSetLP.setText("Set");
        jButtonSetLP.setFocusable(false);
        jButtonSetLP.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonSetLP.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonSetLP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonSetLPMouseClicked(evt);
            }
        });
        jToolBar2.add(jButtonSetLP);

        jToggleButtonList.setText("List");
        jToggleButtonList.setFocusable(false);
        jToggleButtonList.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButtonList.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButtonList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jToggleButtonListMouseClicked(evt);
            }
        });
        jToggleButtonList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonListActionPerformed(evt);
            }
        });
        jToolBar2.add(jToggleButtonList);

        cbLearningMethod.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbLearningMethod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbLearningMethodActionPerformed(evt);
            }
        });
        jToolBar2.add(cbLearningMethod);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jToolBar2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 250, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 250, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(28, 28, 28)
                        .add(filler1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 250, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jToolBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 250, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .add(0, 0, Short.MAX_VALUE))
            .add(jLabelInfoDisplay, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jToolBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jToolBar2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                    .add(jScrollPane1))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(filler1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabelInfoDisplay, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSetLPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonSetLPMouseClicked
        sendLearningParameters();
    }//GEN-LAST:event_jButtonSetLPMouseClicked

    private void jButtonSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonSaveMouseClicked
        saveWalkingParameters();
    }//GEN-LAST:event_jButtonSaveMouseClicked

    private void jToggleButtonLearnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonLearnActionPerformed
    if (jToggleButtonLearn.isSelected())
        {
        if (parent.checkConnected()) {
         // TODO create manager which refreshes commands, see debugrequestmanager
          } else {
            jToggleButtonLearn.setSelected(false);
          }
        } else {
          // TODO remove listener
        }
    }//GEN-LAST:event_jToggleButtonLearnActionPerformed

    private void cbLearningMethodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbLearningMethodActionPerformed
        getLearningParameterList();
    }//GEN-LAST:event_cbLearningMethodActionPerformed

    private void jToggleButtonReceiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonReceiveActionPerformed
        if (jToggleButtonReceive.isSelected())
        {
          if (parent.checkConnected())
          {
            //paramManager.addListener(this.paramListener);
          }
          else
          {
            jToggleButtonReceive.setSelected(false);
          }
        }
        else
        {
          //paramManager.removeListener(this.paramListener);
        }
    }//GEN-LAST:event_jToggleButtonReceiveActionPerformed

    private void jToggleButtonListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonListActionPerformed
        if (jToggleButtonLearn.isSelected()) 
        {
            getLearningParameterList();
        }
    }//GEN-LAST:event_jToggleButtonListActionPerformed

    private void jToggleButtonListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jToggleButtonListMouseClicked
        getLearningParameterList();
    }//GEN-LAST:event_jToggleButtonListMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ParameterLearner.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ParameterLearner.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ParameterLearner.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ParameterLearner.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new ParameterLearner().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cbLearningMethod;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JButton jButtonSetLP;
    private javax.swing.JLabel jLabelInfoDisplay;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextAreaLearningParams;
    private javax.swing.JTextArea jTextAreaWalkingParams;
    private javax.swing.JToggleButton jToggleButtonLearn;
    private javax.swing.JToggleButton jToggleButtonList;
    private javax.swing.JToggleButton jToggleButtonReceive;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    // End of variables declaration//GEN-END:variables

  @Override
  public void handleResponse(byte[] result, Command originalCommand)
  {
    String strResult = new String(result);
    if (strResult.contains("[DebugServer] unknown command: ") || strResult.contains("Unknown command"))
    {
      JOptionPane.showMessageDialog(this, "Can not send parameters!", 
              "Parameter Learner", JOptionPane.ERROR_MESSAGE);
      jToggleButtonLearn.setSelected(false);
    }
    else
    {
        String strCommand = originalCommand.getName();
        if(strCommand.compareTo(strIKParameters + ":get") == 0)
        {
            int k = jTextAreaWalkingParams.getCaretPosition();
            jTextAreaWalkingParams.setText(strResult);
            try {
                jTextAreaWalkingParams.setCaretPosition(k);
            } catch(IllegalArgumentException ex) { /* do nothing */ }
        } 
        else if(strCommand.compareTo(strMLParameters + ":get") == 0)
        {
            int k = jTextAreaLearningParams.getCaretPosition();
            
            String selectedMethod = null;
            if (cbLearningMethod.getSelectedItem() != null)
                selectedMethod = cbLearningMethod.getSelectedItem().toString();
            
            if (jToggleButtonList.isSelected()) {
                cbLearningMethod.removeAllItems();
                String[] mlParameterList = strResult.split("\n");

                ArrayList <String> methods = new ArrayList <String>();

                for (String method : mlParameterList)
                {
                    String methodname = method.split("\\.")[0];
                    if (!methods.contains(methodname)) {
                        methods.add(methodname);
                    }
                }
                for (String method : methods)
                {
                    cbLearningMethod.addItem(method);
                }
             }
             if(selectedMethod != null) {
                 String parameterList = getMethodParameters(selectedMethod, strResult);
                 jTextAreaLearningParams.setText(parameterList);
                 
                 // Reset method lister combo box if items were removed
                 if(jToggleButtonList.isSelected()) {
                    cbLearningMethod.setSelectedItem(selectedMethod);
                 }
             }
             jToggleButtonList.setSelected(false);

             try {
                jTextAreaLearningParams.setCaretPosition(k);
             } catch(IllegalArgumentException ex) { /* do nothing */ }

        } 
    }
  }//end handleResponse

  @Override
  public void handleError(int code)
  {
    jToggleButtonLearn.setSelected(false);
    JOptionPane.showMessageDialog(this,
              "Error occured, code " + code, "ERROR", JOptionPane.ERROR_MESSAGE);
  }//end handleError

  @Override
  public Command getCurrentCommand()
  {
    return commandToExecute;
  }
    
  public String getMethodParameters(String method, String parameters){
      String [] params = parameters.split("\n");
      String [] split = new String[2];  
      String res = "";
      
      for(String p : params){
          split = p.split("\\.", 2);
          if (split[0].equals(method)) res+=split[1]+"\n"; 
      }
      
      return res;
  }
}