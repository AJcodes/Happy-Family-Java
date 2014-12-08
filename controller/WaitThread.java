package controller;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import model.Card;
import model.Deck;

import view.WaitPanel;


public class WaitThread implements Runnable{

	private Socket           socket   = null;
	private WaitPanel       client   = null;
	private DataInputStream  streamIn = null;
	private int cardNum;
	ArrayList<Card> cards = new ArrayList<Card>();
	
	public WaitThread(WaitPanel _Wait, Socket _socket)
	{
		this.socket = _socket;
		this.client = _Wait;
		try
	      {  
			 streamIn  = new DataInputStream(socket.getInputStream());
	      }
	      catch(IOException ioe)
	      {  System.out.println("Error getting input stream: " + ioe);
	         //client.stop();
	      }
		cards.addAll(Deck.getCards());
	}
	
	@Override
	public void run() {
		try
	      {  
			while(true){
				int Case = 0;
				Case = streamIn.readInt();
				switch(Case){
					case -3:
						client.spectate();
					break;
					case -2:
						client.total = streamIn.readInt();
						client.count();
					break;
					case 0:
						client.handle(streamIn.readUTF());
					break;
					case 1:
						client.update(streamIn.readUTF());
					break;
					case 2:
						
						int k = 0; 
						
						while(k<9)
						{
							cardNum = streamIn.readInt();
							for(int p=0;p<cards.size();p++)
							{	 
								if(cardNum == cards.get(p).getNum())
								{	
									client.addCard(cards.get(p));
									cards.remove(p);
									break;
								}
							}
							client.updateCards(cards);
							k++;
							if(k == 9){
								client.regPlayer();
							}
						}
						
					break;
					default:
						System.err.println("error in Wait Thread");
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
