package controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import model.Card;
import model.Deck;

import view.PlayPanel;


public class PlayerThread implements Runnable{

	private Socket           socket   = null;
	private PlayPanel       client   = null;
	private DataInputStream  streamIn = null;
	private DataOutputStream streamOut = null;
	private int cardNum;
	ArrayList<Card> cards = new ArrayList<Card>();
	
	public PlayerThread(PlayPanel _Play, Socket _socket)
	{
		this.socket = _socket;
		this.client = _Play;
		try
	      {  
			 streamIn  = new DataInputStream(socket.getInputStream());
			 streamOut  = new DataOutputStream(socket.getOutputStream());
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
					case -2:
						client.Lost();
					break;
					case 0:
						client.handle(streamIn.readUTF());
					break;
					case 4:
						client.updateID(streamIn.readInt());
					break;
					case 5:
						try{
							client.isTurn(streamIn.readInt());
						}
						catch(NullPointerException n){
							client.isTurn(0);
						}
					break;
					case 6:
						client.request(streamIn.readInt(), streamIn.readInt());
					break;
					case 7:
						client.gratitude(streamIn.readInt(), streamIn.readInt());
					break;
					case 8:
						client.exchange(streamIn.readInt(), streamIn.readInt());
					break;
					case 9:
						client.updateStacks(streamIn.readInt(), streamIn.readInt());
					break;
					case 10:
						client.Win();
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
