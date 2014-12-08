package view;

import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JTextField;
import javax.swing.JButton;

import controller.WaitThread;

public class LoginPanel extends JPanel {
	private JTextField textField;
	private DataOutputStream ToServer;
	private DataInputStream FromServer;
	public Socket connectToServer;
	/**
	 * Create the panel.
	 */
	public LoginPanel() {
		setLayout(null);

		JLabel lblPleaseEnterA = new JLabel("Please Enter A Username:");
		lblPleaseEnterA.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		lblPleaseEnterA.setBounds(340, 220, 234, 24);
		add(lblPleaseEnterA);
		
		textField = new JTextField();
		textField.setBounds(375, 292, 168, 24);
		add(textField);
		textField.setColumns(10);
		
		JButton btnStartPlaying = new JButton("Start Playing");
		btnStartPlaying.setBounds(297, 424, 89, 23);
		add(btnStartPlaying);
		btnStartPlaying.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!textField.getText().isEmpty()){
				try{
					connectToServer = new Socket("localhost",8000);
					FromServer = new DataInputStream(connectToServer.getInputStream());
					ToServer = new DataOutputStream(connectToServer.getOutputStream());
					ToServer.writeInt(-1);
					ToServer.writeUTF(textField.getText());
					ToServer.flush();
					int isStarted = FromServer.readInt();
					if (isStarted == 1){
						removeAll();
						validate();
						repaint();
						ViewPanel Spec = new ViewPanel(connectToServer, textField.getText());
						setLayout(new BorderLayout(0,0));
						add(Spec, BorderLayout.CENTER);
						validate();
						repaint();
					}
					else{
						int isLogin = FromServer.readInt();
						if (isLogin == 1){
							removeAll();
							validate();
							repaint();
							WaitPanel wait = new WaitPanel(connectToServer, textField.getText());
							setLayout(new BorderLayout(0,0));
							add(wait, BorderLayout.CENTER);
							validate();
							repaint();
							//dispose();
						
						}
					}
					//Wait.print("You are now connected \n");
					//ClientWindow Play = new ClientWindow(connectToServer);
					//Play.frame.setVisible(true);
					}

					catch(IOException ex){
						
					}
				
				}
				else{
					
				}
			}
		});
		
		JButton btnHowToPlay = new JButton("How To Play");
		btnHowToPlay.setBounds(532, 424, 89, 23);
		add(btnHowToPlay);

	}
}
