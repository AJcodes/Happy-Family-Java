package view;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import model.Card;
import model.Deck;
import model.SortedListModel;
import model.Text;

import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultCaret;

import controller.PlayerThread;
import javax.swing.ScrollPaneConstants;


public class PlayPanel extends JPanel {
	private int PlayerID;
	private int selectedID;
	private Text selectedText = null;
	private Card selCard = null;
	private int desiredCard;
	private int seconds = 15;
	private Timer timer;
	private JTextField textField;
	private JTextArea textArea;
	private JButton btnThankYou = new JButton();
	private JButton btnRequestCard = new JButton();
	private JLabel lblItIsPlayer = new JLabel();
	private JLabel label;
	private JLabel label_1;
	private Socket socket;
	private String name;
	private CardPanel cardss;
	private StatPanel stat;
	private JList list;
	private ArrayList<Card> cards;
	private PlayerThread thread;
	private boolean isTurn = false;
	private JScrollPane scrollPane_1;
	private static final long serialVersionUID = 1L;
	private static final Color BACKGROUND_COLOR = Color.GRAY;
    private static int   PANEL_SIZE       = 150;
    private Thread mainthread;
    private TimeClass timeClass;
    ArrayList<Card> plCards = new ArrayList<Card>();
    ArrayList<Card> allCards = new ArrayList<Card>();
    private SortedListModel cardslist = new SortedListModel();
    DataInputStream FromServer;
	DataOutputStream ToServer;

	
	public PlayPanel(Socket sock, String name, ArrayList<Card> inCards, ArrayList<Card> c) {
		this.socket = sock;
		this.name = name;
		this.plCards = inCards;
		thread = new PlayerThread(this, socket);
		mainthread = new Thread(thread);
		mainthread.start();
		timeClass = new TimeClass(seconds, 1);
        timer = new Timer(1500, timeClass);
		cards = new ArrayList<Card>();
		cards = c;
		allCards.addAll(Deck.getCards());
		
		for(int i=0; i<cards.size();i++)
		{
			if(cards.get(i) == null)
				break;
			cardslist.addElement(cards.get(i).getFamilyName()+" - "+cards.get(i).getTitle()); 
		}
		
		
		
		setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBounds(10, 573, 855, 135);
		add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
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
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setEditable(false);
		DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_1.setBounds(10, 353, 855, 209);
		add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		cardss = new CardPanel(plCards);
		panel_1.add(scrollPane_2, BorderLayout.CENTER);
		scrollPane_2.setViewportView(cardss);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_2.setBounds(10, 11, 377, 331);
		add(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_3.setBounds(397, 11, 468, 331);
		add(panel_3);
		panel_3.setLayout(null);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 11, 150, 309);
		panel_3.add(scrollPane_1);
		
		list = new JList(cardslist);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		desiredCard = cards.get(list.getSelectedIndex()).getNum();
		selCard = cards.get(list.getSelectedIndex());
		list.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e) 
			{
				JList list = (JList)e.getSource();
				if (list.isSelectionEmpty() != true && cardslist != null){
				desiredCard = cards.get(list.getSelectedIndex()).getNum();
				selCard = cards.get(list.getSelectedIndex());
				}
		    }
		});
		scrollPane_1.setViewportView(list);
		
		btnRequestCard = new JButton("Request Card");
		btnRequestCard.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		btnRequestCard.setBounds(237, 98, 150, 27);
		panel_3.add(btnRequestCard);
		btnRequestCard.setVisible(false);
		btnRequestCard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					
			        label.setText("");
					if (selectedText != null){
						if (selCard != null){
							timer.stop();
					        timeClass = new TimeClass(seconds, 1);
					        timer = new Timer(1500, timeClass);
							ToServer.writeInt(4);
							ToServer.writeInt(PlayerID);
							ToServer.writeInt(selectedID);
							ToServer.writeInt(desiredCard);
							ToServer.flush(); 
							textField.setText("");
							textArea.setFocusable(true);
						}
						else
							label_1.setText("Please select a Card first");
					}
					else
						label_1.setText("Please select a Player first");
					}

					catch(IOException ex){
						
					}
				}
			
		});
		
		btnThankYou = new JButton("Say Thank You");
		btnThankYou.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		btnThankYou.setBounds(237, 147, 150, 27);
		panel_3.add(btnThankYou);
		btnThankYou.setVisible(false);
		btnThankYou.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					timer.stop();
					timeClass = new TimeClass(seconds, 1);
			        timer = new Timer(1500, timeClass);
			        //timer.start();
					ToServer.writeInt(6);
					ToServer.flush(); 
					textField.setText("");
					textArea.setFocusable(true);
					btnThankYou.setVisible(false);
					}

					catch(IOException ex){
						
					}
				}
			
		});
		
		lblItIsPlayer = new JLabel("");
		lblItIsPlayer.setHorizontalAlignment(SwingConstants.CENTER);
		lblItIsPlayer.setFont(new Font("Trebuchet MS", Font.PLAIN, 17));
		lblItIsPlayer.setBounds(191, 40, 244, 39);
		panel_3.add(lblItIsPlayer);
		
		label = new JLabel("");
		label.setFont(new Font("Trebuchet MS", Font.PLAIN, 17));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(191, 256, 244, 47);
		panel_3.add(label);
		
		label_1 = new JLabel("");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setFont(new Font("Trebuchet MS", Font.PLAIN, 17));
		label_1.setBounds(191, 206, 244, 39);
		panel_3.add(label_1);
		
		try{
			
			ToServer = new DataOutputStream(socket.getOutputStream());
			FromServer = new DataInputStream(socket.getInputStream());
			ToServer.writeInt(2);
			//PlayerID = FromServer.readInt();
			ToServer.flush(); 
			
			}

			catch(IOException ex){
				
			}
		
		stat = new StatPanel();
		JScrollPane jscrollpane_3 = new JScrollPane();
		jscrollpane_3.setViewportView(stat);
		panel_2.add(jscrollpane_3, BorderLayout.CENTER);

	}
	
	public void handle(String msg)
	{
		textArea.append(msg + "\n");
	}
	
	public void updateID(int id)
	{
		this.PlayerID = id;
		try{
		isTurn(0);
		}
		catch(NullPointerException n){
			isTurn(0);
		}
	}
	
	public void exchange(int state, int id)
	{
		timer.stop();
		if (state == 0){
			for (Card c: plCards){
				if (c.getNum() == id){
					plCards.remove(c);
					cards.add(c);
					Collections.sort(plCards, new CustomComparator());
					Collections.sort(cards, new CustomComparator());
					cardslist.addElement(c.getFamilyName()+" - "+c.getTitle());
					list.setSelectedIndex(0);
					cardss.replaceDeck(plCards);
					cardss.repaint();
					try{
						ToServer.writeInt(7);
						ToServer.writeInt(PlayerID);
						ToServer.writeInt(plCards.size());
						ToServer.flush(); 
						
						}
						catch(IOException ex){
						}
					break;
				}
			}
		}
		else if (state == 1){
			int count = 0;
			int count1 = 0;
			int count2 = 0;
			int count3 = 0;
			int count4 = 0;
			int count5 = 0;
			String temp = null;
			ArrayList<Card> attempt = new ArrayList<Card>();
			attempt = Deck.getCards();
			for (Card c: attempt){
				if (c.getNum() == id){
					plCards.add(c);
					cards.remove(c);
					for (Card m:plCards){
						if (m.getFamilyName().equals("Avion")){
							count++;
							temp = "Avion";
						}
						else if (m.getFamilyName().equals("Balloon")){
							count1++;
							temp = "Balloon";
						}
						else if (m.getFamilyName().equals("Dirgeable")){
							count2++;
							temp = "Dirgeable";
						}
						else if (m.getFamilyName().equals("Fusee")){
							count3++;
							temp = "Fusee";
						}
						else if (m.getFamilyName().equals("Helicoptere")){
							count4++;
							temp = "Helicoptere";
						}
						else if (m.getFamilyName().equals("Planeur")){
							count5++;
							temp = "Planeur";
						}
					}
					Collections.sort(plCards, new CustomComparator());
					Collections.sort(cards, new CustomComparator());
					cardslist.removeElement(c.getFamilyName()+" - "+c.getTitle());
					list.setSelectedIndex(0);
					cardss.replaceDeck(plCards);
					cardss.repaint();
					if (count == 6 || count1 == 6 || count2 == 6 || count3 == 6 || count4 == 6 || count5 == 6){
						try{
							ToServer.writeInt(11);
							ToServer.writeInt(PlayerID);
							ToServer.writeUTF(temp);
							ToServer.flush(); 
						
						}
						catch(IOException ex){
						}
					}
					else{
						try{
							ToServer.writeInt(7);
							ToServer.writeInt(PlayerID);
							ToServer.writeInt(plCards.size());
							ToServer.flush(); 
						
						}
						catch(IOException ex){
						}
					}
					break;
				}
				
			}
		}
		
	}
	
	public void updateStacks(int id, int size){
		stat.replaceDeck(id, size);
		stat.repaint();
	}
	
	public void gratitude(int cardid, int id)
	{
		btnThankYou.setVisible(true);
		String te = getCardString(cardid);
		lblItIsPlayer.setText("You have received " + te);
		label_1.setText("From Player " + (id+1));
		btnRequestCard.setVisible(false);
		timeClass = new TimeClass(5, 2);
        timer = new Timer(1500, timeClass);
        timer.start();
	}
	
	public String getCardString(int cardid)
	{
		ArrayList<Card> attempt = new ArrayList<Card>();
		attempt = Deck.getCards();
		String att = new String();
		for (Card c: attempt){
			if (c.getNum() == cardid){
				att = c.getFamilyName()+" - "+c.getTitle(); 
				break;
			}
			
		}
		return att;
		
	}
	
	public void request(int from, int cardid)
	{
		from++;
		lblItIsPlayer.setText("Player " + from + " has requested");
		String fe = getCardString(cardid);
		label_1.setText(fe);
		boolean no = false;
		for (Card c:plCards){
			if (c.getNum() == cardid){
				no = true;
				try{
					ToServer.writeInt(5);
					ToServer.writeInt(1);
					ToServer.flush(); 
					
					}
					catch(IOException ex){
					}
				break;
			}
			else
				no = false;
		}
		if (no == false){
			try{
				ToServer.writeInt(5);
				ToServer.writeInt(0);
				ToServer.flush(); 
				
				}
				catch(IOException ex){
				}
		}
	}
	
	
	public void isTurn(int id)
	{
		try
		{
		if (id == PlayerID)
		{
			isTurn = true;
			btnRequestCard.setVisible(true);
			lblItIsPlayer.setText("It is your turn now");
			label_1.setText("");
	        timer.start();
		}
		else{
			id++;
			isTurn = false;
			btnRequestCard.setVisible(false);
			lblItIsPlayer.setText("It is Player " + id + "'s Turn.");
			label_1.setText("");
		}
		}
		catch (NullPointerException n){
			id = 0;
			id++;
			isTurn = false;
			btnRequestCard.setVisible(false);
			lblItIsPlayer.setText("It is Player " + id + "'s Turn.");
			//label_1.setText("");
		}
	}
	
	public void Win()
	{
		btnRequestCard.setVisible(false);
		btnThankYou.setVisible(false);
		lblItIsPlayer.setText("You've Won!");
		label_1.setText("");
	}
	
	public void Lost()
	{
		btnRequestCard.setVisible(false);
		btnThankYou.setVisible(false);
		lblItIsPlayer.setText("You've Lost.");
		label_1.setText("");
	}
	
	
	
	class CardPanel extends JComponent{
		
		private static final long serialVersionUID = 1L;
		public ArrayList<Card> _deck;       
	    
	    public CardPanel(ArrayList<Card> deck) {
	        _deck = deck;
	        PANEL_SIZE = PANEL_SIZE * _deck.size();
	        this.setPreferredSize(new Dimension(PANEL_SIZE, 200));
	        this.setBackground(Color.blue);
	        
	    }
	    
	    public void replaceDeck(ArrayList<Card> deck){
	    	_deck = deck;
	    	//Collections.sort(_deck);
	    	PANEL_SIZE = 150 * _deck.size();
	        this.setPreferredSize(new Dimension(PANEL_SIZE, 200));
	    }
	    
	    @Override
	    public void paintComponent(Graphics g) {
	        int width = getWidth();
	        int height = getHeight();
	        Graphics2D g2 = (Graphics2D) g;
	        g2.setColor(BACKGROUND_COLOR);
	        g2.fillRect(0, 0, width, height);
	        int xPos = 10;
	        int yPos = 8;
	        for (Card c : _deck) {
	            c.draw(g2, this, xPos, yPos);
	            xPos += 120;
	        }
	        
	    }
	}
	
	class StatPanel extends JComponent implements MouseListener{
		
		private static final long serialVersionUID = 1L;
		private ArrayList<Card> _deck;   
	    private ArrayList<Card> _deck2;
	    private ArrayList<Card> _deck3;
	    private Text _text[] = new Text[3];
	    private int no[] = new int[3];
	    
	    public StatPanel() {
	        _deck = Deck.getDummyCards(9);
	        _deck2 = Deck.getDummyCards(9);
	        _deck3 = Deck.getDummyCards(9);
	        int j = 0;
	        for (int i = 1; i < 5; i++)
	        {
	        	if (i != PlayerID+1)
	        	{
	        		no[j] = i;
	        		_text[j] = new Text("Player", (no[j]-1));
	        		j++;
	        		if (j == 3){
	        			break;
	        		}
	        	}
	        }
	    	PANEL_SIZE = PANEL_SIZE * 4;
	        this.setPreferredSize(new Dimension(PANEL_SIZE, 130));
	        this.setBackground(Color.blue);
	        addMouseListener(this);
	    }
	    
	    public void replaceDeck(int id, int size){
	    	if (no[0] == id+1){
	    		_deck = Deck.getDummyCards(size);
	    	}
	    	else if (no[1] == id+1){
	    		_deck2 = Deck.getDummyCards(size);
	    	}
	    	else if (no[2] == id+1){
	    		_deck3 = Deck.getDummyCards(size);
	    	}
	    }
	    
	    @Override
	    public void paintComponent(Graphics g) {
	        int width = getWidth();
	        int height = getHeight();
	        Graphics2D g2 = (Graphics2D) g;
	        g2.setColor(BACKGROUND_COLOR);
	        g2.fillRect(0, 0, width, height);
	        int xP = 5;
	        int yP = 60;
	        int xPos = 100;
	        int yPos = 8;
	        for (int i = 0; i < 3; i++){
	        	_text[i].draw(g2, this, xP, yP);
	        	yP += 100;
	        }
	        for (Card c : _deck) {
	            c.draw(g2, this, xPos, yPos);
	            xPos += 15;
	        }
	        int xPos1 = 100;
	        int yPos1 = 108;
	        for (Card d : _deck2) {
	            d.draw(g2, this, xPos1, yPos1);
	            xPos1 += 15;
	        }
	        int xPos2 = 100;
	        int yPos2 = 208;
	        for (Card e : _deck3) {
	            e.draw(g2, this, xPos2, yPos2);
	            xPos2 += 15;
	        }
	    }

		@Override
		public void mouseClicked(MouseEvent e) {
			int x = e.getX();   
	        int y = e.getY();   
	        
	        for (int i = 0; i < 3; i++) {
	            if (_text[i].contains(x, y)) {
	            	_text[i].isSelected = true;
	            	selectedText = _text[i];
	                selectedID = _text[i].getID();
	                //label_1.setText("Player " + (selectedID+1) + " Selected");
	                repaint();
	                break;        // Stop when we find the first match.
	            }
	            else{
	            	_text[i].isSelected = false;
	            	selectedText = _text[i];
	            	repaint();
	            }
	        }
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	}
	
	class TimeClass implements ActionListener {
	    private int counter;
	    private int type;
	     
	    public TimeClass(int counter, int type) {
	      this.counter = counter;
	      this.type = type;
	    }
	     
	    public void actionPerformed(ActionEvent tc) {
	      if (counter >= 0) {
	        label.setText("Time Left: " + counter);
	      } else {
	    	
	        timer.stop();
	        if (type == 1){
	        	timeClass = new TimeClass(seconds, 1);
	        	timer = new Timer(1500, timeClass);
	        	isTurn = false;
	        	btnRequestCard.setVisible(false);
	        	label.setText("");
	        	try{
	        		ToServer.writeInt(3);
	        		ToServer.writeInt(PlayerID);
	        		ToServer.flush(); 
				}
				catch(IOException ex){
					
				}
	        }
	        else
	        {
	        	timeClass = new TimeClass(seconds, 1);
	        	timer = new Timer(1500, timeClass);
		        isTurn = false;
		        label.setText("");
		        btnThankYou.setVisible(false);
		        try{
	        		ToServer.writeInt(10);
	        		ToServer.flush(); 
				}
				catch(IOException ex){
					
				}
	        }
	        
	      }
	      counter--;  
	    }
	  }
	
	class CustomComparator implements Comparator<Card> {
	    @Override
	    public int compare(Card o1, Card o2) {
	        return o1.getfull().compareTo(o2.getfull());
	    }
	}

	
}
