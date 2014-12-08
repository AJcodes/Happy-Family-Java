package controller;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import model.Card;
import model.Deck;

import view.ViewPanel;


public class SpectatorThread implements Runnable{

	private Socket           socket   = null;
	private ViewPanel       client   = null;
	private DataInputStream  streamIn = null;
	private int cardNum;
	private ArrayList<Card> cards = new ArrayList<Card>();
	
	public SpectatorThread(ViewPanel _view, Socket _socket)
	{
		this.socket = _socket;
		this.client = _view;
		cards.addAll(Deck.getCards());
		try
	      {  
			 streamIn  = new DataInputStream(socket.getInputStream());
	      }
	      catch(IOException ioe)
	      {  System.out.println("Error getting input stream: " + ioe);
	         //client.stop();
	      }
	}
	
	@Override
	public void run() {
		try
	      {  
			while(true){
				int Case = 0;
				Case = streamIn.readInt();
				switch(Case){
					case -7:
						client.exchangeCards(streamIn.readInt(), streamIn.readInt(), streamIn.readInt());
					break;
					case -6:
						int k = 0; 
						
						while(k<9)
						{
							cardNum = streamIn.readInt();
							for(int p=0;p<cards.size();p++)
							{	 
								if(cardNum == cards.get(p).getNum())
								{	
									client.addCard(0, cards.get(p));
									break;
								}
							}
							k++;
						}
						int j = 0;
						while(j<9)
						{
							cardNum = streamIn.readInt();
							for(int p=0;p<cards.size();p++)
							{	 
								if(cardNum == cards.get(p).getNum())
								{	
									client.addCard(1, cards.get(p));
									break;
								}
							}
							j++;
						}
						int i = 0;
						while(i<9)
						{
							cardNum = streamIn.readInt();
							for(int p=0;p<cards.size();p++)
							{	 
								if(cardNum == cards.get(p).getNum())
								{	
									client.addCard(2, cards.get(p));
									break;
								}
							}
							i++;
						}
						int h = 0;
						while(h<9)
						{
							cardNum = streamIn.readInt();
							for(int p=0;p<cards.size();p++)
							{	 
								if(cardNum == cards.get(p).getNum())
								{	
									client.addCard(3, cards.get(p));
									break;
								}
							}
							h++;
						}
					break;
					case 0:
						client.handle(streamIn.readUTF());
					break;
					default:
						System.err.println("error in Spectator Thread");
					break;
				}
			}
	      }
	      catch(IOException ioe)
	      {  System.out.println("Error getting input stream: " + ioe);
	         //client.stop();
	      }
		
	}
	

}
