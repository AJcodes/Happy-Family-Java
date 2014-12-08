package view;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;

import controller.SpectatorThread;
import java.awt.FlowLayout;
import javax.swing.border.BevelBorder;
import javax.swing.text.DefaultCaret;

import model.Card;
import model.Deck;
import model.Text;

public class ViewPanel extends JPanel {
	private JTextField textField;
	private Socket socket;
	private String name;
	private JTextArea textArea;
	private SpectatorThread thread;
	private StatPanel stat;
	private Thread mainthread;
	private static final Color BACKGROUND_COLOR = Color.GRAY;
    private static int   PANEL_SIZE       = 80;
	DataOutputStream ToServer;
	DataInputStream FromServer;
	private ArrayList<Card> deck = new ArrayList<Card>();
	private ArrayList[] cards = new ArrayList[4];
	
	public ViewPanel(Socket sock, String name) {
		this.socket = sock;
		this.name = name;
		deck.addAll(Deck.getCards());
		Collections.sort(deck, new CustomComparator());
		for (int i = 0; i < 4; i++)
		{
			cards[i] = new ArrayList<Card>();
		}
		thread = new SpectatorThread(this, socket);
		mainthread = new Thread(thread);
		mainthread.start();
		try{
			
			ToServer = new DataOutputStream(socket.getOutputStream());
			FromServer = new DataInputStream(socket.getInputStream());
			ToServer.writeInt(-5);
			//PlayerID = FromServer.readInt();
			ToServer.flush(); 
			
			}

			catch(IOException ex){
				
			}
		setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBounds(10, 573, 855, 135);
		add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		textField = new JTextField();
		panel.add(textField, BorderLayout.SOUTH);
		textField.setColumns(10);
		
		textArea = new JTextArea();
		JScrollPane jscroll = new JScrollPane();
		jscroll.setViewportView(textArea);
		panel.add(jscroll, BorderLayout.CENTER);
		textArea.setEditable(false);
		DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_1.setBounds(10, 11, 855, 552);
		add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		stat = new StatPanel(cards[0], cards[1], cards[2], cards[3]);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(stat);
		panel_1.add(scrollPane, BorderLayout.CENTER);
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					
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

	}
	
	public void handle(String msg)
	{
		textArea.append(msg + "\n");
	}
	
	public void updateCards(int id, ArrayList<Card> C)
	{
		cards[id] = C;
	}
	
	public void exchangeCards(int id, int target, int cardnum)
	{
		Card req = null;
		for(Card d:deck){
			if (d.getNum() == cardnum){
				req = d;
				break;
			}
		}
		cards[id].add(req);
		cards[target].remove(req);
		stat.repaint();
	}
	
	@SuppressWarnings("unchecked")
	public void addCard(int id, Card newCard)
	{
		int Size = cards[id].size();
		boolean added = false;
		if(Size == 0)
			cards[id].add(newCard);
		else
		{
			for(int i=0;i<Size;i++)
			{
				if(((Card) cards[id].get(i)).getNum()>newCard.getNum())
				{
					cards[id].add(i,newCard);
					added = true;
					break;
				}
			}
			if(!added)
				cards[id].add(newCard);
		}
	}
	
	class StatPanel extends JComponent{
		
		private static final long serialVersionUID = 1L;
		private ArrayList<Card> _deck;   
	    private ArrayList<Card> _deck2;
	    private ArrayList<Card> _deck3;
	    private ArrayList<Card> _deck4;
	    private Text _text[] = new Text[4];
	    private int no[] = new int[4];
	    
	    public StatPanel(ArrayList<Card> deck1, ArrayList<Card> deck2, ArrayList<Card> deck3, ArrayList<Card> deck4) {
	        _deck = deck1;
	        _deck2 = deck2;
	        _deck3 = deck3;
	        _deck4 = deck4;
	        int j = 0;
	        for (int i = 1; i < 5; i++)
	        {
	        		no[j] = i;
	        		_text[j] = new Text("Player", (no[j]-1));
	        		j++;
	        	
	        }
	    	PANEL_SIZE = PANEL_SIZE * 50;
	        this.setPreferredSize(new Dimension(PANEL_SIZE, 1000));
	        this.setBackground(Color.blue);
	    }
	    
	    
	    @Override
	    public void paintComponent(Graphics g) {
	        int width = getWidth();
	        int height = getHeight();
	        Graphics2D g2 = (Graphics2D) g;
	        g2.setColor(BACKGROUND_COLOR);
	        g2.fillRect(0, 0, width, height);
	        int xP = 5;
	        int yP = 100;
	        int xPos = 100;
	        int yPos = 8;
	        for (int i = 0; i < 4; i++){
	        	_text[i].draw(g2, this, xP, yP);
	        	yP += 190;
	        }
	        for (Card c : _deck) {
	            c.draw(g2, this, xPos, yPos);
	            xPos += 120;
	        }
	        int xPos1 = 100;
	        int yPos1 = 198;
	        for (Card d : _deck2) {
	            d.draw(g2, this, xPos1, yPos1);
	            xPos1 += 120;
	        }
	        int xPos2 = 100;
	        int yPos2 = 388;
	        for (Card e : _deck3) {
	            e.draw(g2, this, xPos2, yPos2);
	            xPos2 += 120;
	        }
	        int xPos3 = 100;
	        int yPos3 = 578;
	        for (Card f : _deck4) {
	            f.draw(g2, this, xPos3, yPos3);
	            xPos3 += 120;
	        }
	    }

	}
	
	class CustomComparator implements Comparator<Card> {
	    @Override
	    public int compare(Card o1, Card o2) {
	        return o1.getfull().compareTo(o2.getfull());
	    }
	}
	
	
}
