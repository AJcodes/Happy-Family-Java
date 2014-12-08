package controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.Scanner;

import model.Card;

import model.Deck;

import main.Server;

public class ServerThread implements Runnable{
	
	private Socket socket;
	public String isActive;
	private Server server;
	DataInputStream inputFromClient;                 
	DataOutputStream outputToClient;
	Scanner inStream;
	Formatter outStream;
	private int id;
	private String name;
	ArrayList<Card> cards = new ArrayList<Card>();
	ArrayList<Card> myCards = new ArrayList<Card>();
	
	public ServerThread(Server _server, Socket socket, int id){            
		this.server = _server;
		this.socket = socket;
		isActive = "Active";
		this.id = id;
		cards.addAll(Deck.getCards());
		Collections.shuffle(cards);
	}       
	
	public int getID(){
		return id;
	}
	
	public String getUsername(){
		return name;
	}

	public void send(String msg)
	   {   try
	       {  
		   	  outputToClient.writeInt(0);
		   	  outputToClient.writeUTF(msg);
		   	  outputToClient.flush();
		   	  
	       }
	       catch(IOException ioe)
	       {  

	       }
	   }
	
	public void setTurn(int set)
	   {   try
	       {  
		   		  outputToClient.writeInt(5);
		   		  outputToClient.writeInt(set);
		   		  outputToClient.flush();
		   	  
	       }
	       catch(IOException ioe)
	       {  

	       }
	   }
	
	public void sendID(int id)
	   {   try
	       {  
		   	  outputToClient.writeInt(4);
		   	  outputToClient.writeInt(id);
		   	  outputToClient.flush();
		   	  
	       }
	       catch(IOException ioe)
	       {  

	       }
	   }
	
	public void updateStat(int id, int target, int cardnum)
	{
		try
	       {  
		   	  outputToClient.writeInt(-7);
		   	  outputToClient.writeInt(id);
		   	  outputToClient.writeInt(target);
		   	  outputToClient.writeInt(cardnum);
		   	  outputToClient.flush();
		   	  
	       }
	       catch(IOException ioe)
	       {  

	       }
	}
	
	public void loadStacks(ArrayList<Card> fromServer, ArrayList<Card> fromServer2, ArrayList<Card> fromServer3, ArrayList<Card> fromServer4){
		try
	       {  
		   	  outputToClient.writeInt(-6);
		   	for(int j=0; j<fromServer.size(); j++)
		   	  {
		   		outputToClient.writeInt(fromServer.get(j).getNum());
				//outStream.format(myCards.get(j).getNum()+"\n");
				//outStream.flush();
		   	  }
		   	for(int j=0; j<fromServer2.size(); j++)
		   	  {
		   		outputToClient.writeInt(fromServer2.get(j).getNum());
				//outStream.format(myCards.get(j).getNum()+"\n");
				//outStream.flush();
		   	  }
		   	for(int j=0; j<fromServer3.size(); j++)
		   	  {
		   		outputToClient.writeInt(fromServer3.get(j).getNum());
				//outStream.format(myCards.get(j).getNum()+"\n");
				//outStream.flush();
		   	  }
		   	for(int j=0; j<fromServer4.size(); j++)
		   	  {
		   		outputToClient.writeInt(fromServer4.get(j).getNum());
				//outStream.format(myCards.get(j).getNum()+"\n");
				//outStream.flush();
		   	  }
		   	  outputToClient.flush();
		   	  
	       }
	       catch(IOException ioe)
	       {  

	       }
	}
	
	public void observe()
	   {   try
	       {  
		   	  outputToClient.writeInt(-3);
		   	  outputToClient.flush();
		   	  
	       }
	       catch(IOException ioe)
	       {  

	       }
	   }
	
	public void lose()
	   {   try
	       {  
		   	  outputToClient.writeInt(-2);
		   	  outputToClient.flush();
		   	  
	       }
	       catch(IOException ioe)
	       {  

	       }
	   }
	
	public void win()
	   {   try
	       {  
		   	  outputToClient.writeInt(10);
		   	  outputToClient.flush();
		   	  
	       }
	       catch(IOException ioe)
	       {  

	       }
	   }
	
	
	public void upcount(int i)
	   {   try
	       {  
		   	  outputToClient.writeInt(-2);
		   	  outputToClient.writeInt(i);
		   	  outputToClient.flush();
		   	  
	       }
	       catch(IOException ioe)
	       {  

	       }
	   }
	
	public void ready(ArrayList<Card> fromServer) {
		try
	       {  
		   	  outputToClient.writeInt(2);
		   	  for(int j=0; j<fromServer.size(); j++)
		   	  {
		   		outputToClient.writeInt(fromServer.get(j).getNum());
				//outStream.format(myCards.get(j).getNum()+"\n");
				//outStream.flush();
		   	  }
		   	  outputToClient.flush();
		   	  
	       }
	       catch(IOException ioe)
	       {  

	       }
		
	}
	
	public void request(int from, int cardnum) {
		try
	       {  
		   	  outputToClient.writeInt(6);
		   	  outputToClient.writeInt(from);
		   	  outputToClient.writeInt(cardnum);
		   	  outputToClient.flush();
		   	  
	       }
	       catch(IOException ioe)
	       {  

	       }
		
	}
	
	public void trade(int state, int cardnum) {
		try
	       {  
		   	  outputToClient.writeInt(8);
		   	  outputToClient.writeInt(state);
		   	  outputToClient.writeInt(cardnum);
		   	  outputToClient.flush();
		   	  
	       }
	       catch(IOException ioe)
	       {  

	       }
	}
	
	public void await(int cardno, int it) {
		try
	       {  
		   	  outputToClient.writeInt(7);
		   	  outputToClient.writeInt(cardno);
		   	  outputToClient.writeInt(it);
		   	  outputToClient.flush();
		   	  
	       }
	       catch(IOException ioe)
	       {  
	       }
		
	}
	
	public void upstacks(int id, int size) {
		try
	       {  
		   	  outputToClient.writeInt(9);
		   	  outputToClient.writeInt(id);
		   	  outputToClient.writeInt(size);
		   	  outputToClient.flush();
		   	  
	       }
	       catch(IOException ioe)
	       {  
	       }
		
	}
	
	public void update(String msg)
	   {   
		   try
	       {  
		   	  outputToClient.writeInt(1);
		   	  outputToClient.writeUTF(msg);
		   	  outputToClient.flush();
		   	  
	       }
	       catch(IOException ioe)
	       {  

	       }
	   }
	
	@Override
	public void run() {
		try{                 
			inputFromClient = new DataInputStream(socket.getInputStream());                 
			outputToClient = new DataOutputStream(socket.getOutputStream());
			inStream = new Scanner(socket.getInputStream());
			outStream = new Formatter(socket.getOutputStream());
			while(true){       
				int Case = 0;
				Case = inputFromClient.readInt();
				switch(Case){
					case -5:
						server.updateViews();
					break;
					case -2:
						server.count();
					break;
					case -1: 
						name = inputFromClient.readUTF();
						if (server.hasStarted == true){
							outputToClient.writeInt(1);
						}
						else{
							outputToClient.writeInt(0);
						}
						outputToClient.writeInt(1);
						outputToClient.flush();
					break;
					case 0:
						server.handle(name, inputFromClient.readUTF());
					break;
					case 1:
						server.upready(inputFromClient.readUTF());
						server.regPlayer(id);
					break;
					case 2:
						server.updateID();
					break;
					case 3:
						server.changeTurn(inputFromClient.readInt());
					break;
					case 4:
						server.request(inputFromClient.readInt(), inputFromClient.readInt(), inputFromClient.readInt());
					break;
					case 5:
						server.gratitude(inputFromClient.readInt());
					break;
					case 6:
						server.exchange();
					break;
					case 7:
						int tempid = inputFromClient.readInt();
						int size = inputFromClient.readInt();
						if (size == 0){
							server.lost(tempid);
						}
						else{
							server.updateStacks(tempid, size);
						}
					break;
					case 10:
						server.fail();
					break;
					case 11:
						server.end(inputFromClient.readInt(), inputFromClient.readUTF());
					break;
					default:
						System.err.println("error");
					break;
				}
				
	               
			}             
		}             
		catch(IOException e){
			server.closed(id);
			System.err.println(e);       
			
		}      
		
	}
	
	public boolean getCards()
	{
		int i = 0;
		while(myCards.size() < 9)
		{
			myCards.add(cards.get(i));
			//System.out.println(myCards.get(i).getTitle()+" "+myCards.get(i).getFamilyName());
			cards.remove(i);
			i++;
		}
		return false;
	}

	

	

	

}
