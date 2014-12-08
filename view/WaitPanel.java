package view;

import java.awt.BorderLayout;
import java.awt.TextArea;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

import model.Card;
import controller.WaitThread;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.border.BevelBorder;
import javax.swing.text.DefaultCaret;

public class WaitPanel extends JPanel {

	private Socket socket;
	private JList textArea2;
	private JTextArea textArea;
	private WaitThread thread;
	private JTextField textField;
	private String name;
	private JButton btnReady;
	DefaultListModel model = new DefaultListModel();
	public int total;
	private Thread mainthread;
	private JLabel lblNewLabel_2 = new JLabel("");
	ArrayList<Card> myCards = new ArrayList<Card>();
	ArrayList<Card> Cards = new ArrayList<Card>();
	DataInputStream FromServer;
	DataOutputStream ToServer;
	/**
	 * Create the panel.
	 */
	public WaitPanel(Socket sock, String nam) {
		this.socket = sock;
		this.name = nam;
		thread = new WaitThread(this, socket);
		mainthread = new Thread(thread);
		mainthread.start();
		try{
			
			ToServer = new DataOutputStream(socket.getOutputStream());
			ToServer.writeInt(-2);
			ToServer.flush(); 
			
			}

			catch(IOException ex){
				
			}
		
		setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		//ChatPanel chat = new ChatPanel(socket);
		panel.setLayout(new BorderLayout(0, 0));
		panel.setBounds(25, 374, 800, 317);
		textArea = new JTextArea();
		textArea.setEditable(false);
		DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane scrollPane = new JScrollPane(textArea);
		panel.add(scrollPane, BorderLayout.CENTER);
		
		textField = new JTextField();
		panel.add(textField, BorderLayout.SOUTH);
		textField.setColumns(15);
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					//open(); 
					//FromServer = new DataInputStream(socket.getInputStream());
					
					ToServer.writeInt(0);
					ToServer.writeUTF(textField.getText());
					ToServer.flush(); 
					textField.setText("");
					textArea.setFocusable(true);
					}

					catch(IOException ex){
						
					}
				}
			
		});
		
		add(panel);
		
		btnReady = new JButton("Ready");
		btnReady.setFont(new Font("Trebuchet MS", Font.PLAIN, 16));
		btnReady.setBounds(456, 182, 104, 37);
		btnReady.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					//open(); 
					//FromServer = new DataInputStream(socket.getInputStream());
					ToServer = new DataOutputStream(socket.getOutputStream());
					ToServer.writeInt(1);
					ToServer.writeUTF(name);
					ToServer.flush(); 
					//isReady = true;
					btnReady.setVisible(false);					
					
					}

					catch(IOException ex){
						
					}
				}
			
			
		});
		add(btnReady);
		//btnReady.setVisible(false);
		
		JButton btnDisconnect = new JButton("Disconnect");
		btnDisconnect.setFont(new Font("Trebuchet MS", Font.PLAIN, 16));
		btnDisconnect.setBounds(640, 182, 98, 37);
		add(btnDisconnect);
		
		textArea2 = new JList(model);
		textArea2.setBounds(25, 93, 247, 270);
		add(textArea2);
		
		JLabel lblNewLabel = new JLabel("Players Ready");
		lblNewLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 17));
		lblNewLabel.setBounds(25, 56, 247, 26);
		add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("There must be at least 4 clients connected to start game.");
		lblNewLabel_1.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(374, 98, 451, 73);
		add(lblNewLabel_1);
	
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		lblNewLabel_2.setBounds(507, 251, 184, 26);
		add(lblNewLabel_2);

	}
	
	public void count()
	{
		
		if (total == 1){
			lblNewLabel_2.setText(total + " client is connected.");
		}
		else{
			lblNewLabel_2.setText(total + " clients are connected.");
		}
		
	}
	
	public void handle(String msg)
	{
		textArea.append(msg + "\n");
	}
	
	public void update(String msg)
	{
		model.addElement(msg);
	}

	public void regPlayer() {
		
		removeAll();
		validate();
		repaint();
		PlayPanel play = new PlayPanel(socket, name, myCards, Cards);
		setLayout(new BorderLayout(0,0));
		add(play, BorderLayout.CENTER);
		validate();
		repaint();
		mainthread.stop();
	}
	
public void spectate() {
		
		removeAll();
		validate();
		repaint();
		ViewPanel view = new ViewPanel(socket, name);
		setLayout(new BorderLayout(0,0));
		add(view, BorderLayout.CENTER);
		validate();
		repaint();
		mainthread.stop();
	}
	
	public void updateCards(ArrayList<Card> C)
	{
		Cards = C;
	}
	
	public void addCard(Card newCard)
	{
		int Size = myCards.size();
		boolean added = false;
		if(Size == 0)
			myCards.add(newCard);
		else
		{
			for(int i=0;i<Size;i++)
			{
				if(myCards.get(i).getNum()>newCard.getNum())
				{
					myCards.add(i,newCard);
					added = true;
					break;
				}
			}
			if(!added)
				myCards.add(newCard);
		}
	}
}
