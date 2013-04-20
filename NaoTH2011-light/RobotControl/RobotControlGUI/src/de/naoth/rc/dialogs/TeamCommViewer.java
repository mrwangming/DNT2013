/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TeamCommViewer.java
 *
 * Created on 08.11.2010, 21:41:28
 */

package de.naoth.rc.dialogs;
import de.naoth.rc.AbstractDialog;
import de.naoth.rc.DialogPlugin;
import de.naoth.rc.RobotControl;
import de.naoth.rc.dialogs.panels.RobotStatus;
import de.naoth.rc.manager.TeamCommDrawingManager;
import de.naoth.rc.messages.Messages.TeamCommMessage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import javax.swing.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;

/**
 *
 * @author Heinrich Mellmann
 */
public class TeamCommViewer extends AbstractDialog
{

  @PluginImplementation
  public static class Plugin extends DialogPlugin<TeamCommViewer>
  {
      @InjectPlugin
      public static RobotControl parent;
      @InjectPlugin
      public static TeamCommDrawingManager teamCommDrawingManager;
  }//end Plugin
  

  private HashMap<String,RobotStatus> robotsMap;

  private Timer timerCheckMessages = null;
  
    /** Creates new form TeamCommViewer */
    public TeamCommViewer() {
      initComponents();

      this.robotsMap = new HashMap<String,RobotStatus>();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    robotStatusPanel = new javax.swing.JPanel();
    btListen = new javax.swing.JToggleButton();

    robotStatusPanel.setBackground(new java.awt.Color(255, 255, 255));
    robotStatusPanel.setLayout(new javax.swing.BoxLayout(robotStatusPanel, javax.swing.BoxLayout.Y_AXIS));

    btListen.setText("Listen to TeamComm");
    btListen.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btListenActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(btListen)
          .addComponent(robotStatusPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(154, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(btListen)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(robotStatusPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(98, Short.MAX_VALUE))
    );
  }// </editor-fold>//GEN-END:initComponents

    private void btListenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btListenActionPerformed
      if(this.btListen.isSelected())
      {
        if(this.timerCheckMessages == null)
        {
          this.timerCheckMessages = new Timer(100, new TeamCommListener());
          this.timerCheckMessages.setCoalesce(true);
        }
        this.timerCheckMessages.start();
      }else
      {
        if(this.timerCheckMessages != null)
        {
          this.timerCheckMessages.stop();
        }
      }
      
    }//GEN-LAST:event_btListenActionPerformed

  @Override
  public void dispose()
  {
    // stop the listener
    if(this.timerCheckMessages != null)
        this.timerCheckMessages.stop();
  }//end dispose


  private void parseMessage(String address, int port, TeamCommMessage msg)
  {
    RobotStatus robotStatus = this.robotsMap.get(address);
    if(robotStatus == null)
    {
      robotStatus = new RobotStatus(Plugin.parent.getMessageServer(), address);
      robotStatus.setStatus(msg.getPlayerNumber());
      this.robotsMap.put(address, robotStatus);
      this.robotStatusPanel.add(robotStatus);
    }else
    {
      robotStatus.setStatus(msg.getPlayerNumber());
    }

    // TODO: make it better
    Plugin.teamCommDrawingManager.setCurrenId(address);
    Plugin.teamCommDrawingManager.handleResponse(msg.toByteArray(), null);
  }//end parseMessage

 
  class TeamCommListener implements ActionListener
  {
    public final int TEAMCOMM_PORT = 10400;
    public final int MAX_PACKAGE_SIZE = 256;
    public final int NUM_OF_PLAYERS = 11;

    private long sizeReceived;
    private long lastSecondStart;

    private DatagramSocket udpSocket;
  
    public TeamCommListener()
    {
      connect();
    }

    public final void connect()
    {
        try
        {
            udpSocket = new DatagramSocket(TEAMCOMM_PORT);
        }
        catch(IOException ex)
        {
            Logger.getLogger(TeamCommViewer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//end connect
    
    
    public final void disconnect()
    {
        if(udpSocket != null)
        {
            udpSocket.close();
            udpSocket = null;
        }
    }//end disconnect
    
    @Override
    public void actionPerformed(ActionEvent e)
    {

      long currentTime = new Date().getTime();
      if(currentTime - lastSecondStart > 1000)
      {
        double kbps = (double) sizeReceived / 1024.0;
        //setTitle(String.format("NaotH TeamComm Viewer - received %.2f Kibit/s", kbps));
        sizeReceived = 0;
        lastSecondStart = currentTime;
      }

      if(udpSocket != null && udpSocket.isBound())
      {
        try
        {
          while(true) // read all available data
          {
            byte[] buffer = new byte[MAX_PACKAGE_SIZE];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            // only check if something is there
            udpSocket.setSoTimeout(1);
            udpSocket.receive(packet);
            sizeReceived += packet.getLength();

            byte[] truncatedBuffer = Arrays.copyOf(buffer, packet.getLength());
            TeamCommMessage msg = TeamCommMessage.parseFrom(truncatedBuffer);

            int playerNumber = msg.getPlayerNumber();
            if(playerNumber >= 0 && playerNumber <= NUM_OF_PLAYERS)
            {
              parseMessage(packet.getAddress().toString().substring(1), packet.getPort(), msg);
            }//end if
          }//end while
        }//end try
        catch(SocketTimeoutException ex)
        {
          // no data to read, ignore
        }
        catch(SocketException ex)
        {
          Logger.getLogger(TeamCommViewer.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(IOException ex)
        {
          Logger.getLogger(TeamCommViewer.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
  }//end class TeamCommListener

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JToggleButton btListen;
  private javax.swing.JPanel robotStatusPanel;
  // End of variables declaration//GEN-END:variables

}