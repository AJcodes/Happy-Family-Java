package main;

import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Random;
import java.io.*;

import model.Card;
import model.Deck;

import view.ServerMain;

import controller.ServerThread;

public class Server
{  
   static ServerMain serv = new ServerMain();
   private ArrayList<ServerThread> clients = new ArrayList<ServerThread>();
   private ServerThread players[] = new ServerThread[4];
   private ArrayList<ServerThread> spectators = new ArrayList<ServerThread>();
   private int j = 0;
   public boolean hasStarted = false;
   private ServerSocket server = null;
   private int clientNo = 0;
   private int specNo = 0;
   private String user = null;
   private int test = 0;
   private int join = 0;
   private int tempId;
   private int tempTarget;
   private int lostid[] = new int[4];
   private int step = 0;
   private int cardNum;
   Random randomGenerator = new Random();
   ArrayList<Card> cards = new ArrayList<Card>();
   ArrayList<Card> myCards = new ArrayList<Card>();
   ArrayList allCards[] = new ArrayList[4];
   private ArrayList<String> users = new ArrayList<String>();

   public Server(int port)
   {  
	   cards.addAll(Deck.getCards());
	   Collections.shuffle(cards);
	   try{            
			ServerSocket serverSocket = new ServerSocket(port);             
			serv.print("Server started at " + new Date() + "\n\n");                           
               
			while(true){
					Socket socket = serverSocket.accept();
					ServerThread task = new ServerThread(this, socket, clientNo);
					serv.print("starting thread for client " + clientNo + " at " + new Date() + '\n');                                   
					InetAddress inetAddress = socket.getInetAddress();                 
					serv.print("Client " + clientNo + "'s host name is " + inetAddress.getHostName() + '\n');                 
					serv.print("Client " + clientNo + "'s IP address is " + inetAddress.getHostAddress() + '\n');
					clients.add(clientNo, task);
					new Thread(clients.get(clientNo)).start();
					spectators.add(specNo, clients.get(clientNo));
					clientNo++;  
					specNo++;
					
			}         
		}         
		catch(IOException ex){   
			serv.print(ex.toString());         
		}    
   }
   
   public synchronized void count()
   {  
         for (int i = 0; i < clientNo; i++){
            clients.get(i).upcount(clientNo);  
         }
	   	   
   }
   
   public synchronized void lost(int id){
	   int temp = id + 1;
	   players[id].lose();
	   lostid[step] = id;
	   step++;
	   for (int i = 0; i < 4; i++){
		   if (i != id){
			   players[i].send("Player " + temp + " has lost the game.");
		   }
		}   
	   if (specNo != 0){
		   for (int k = 0; k < spectators.size(); k++)
			{
				spectators.get(k).send("Player " + temp + " has lost the game.");
			}
		   
	   }
   }
   
   public synchronized void end(int id, String family){
	   int temp = id + 1;
	   players[id].win();
	   players[id].send("Player " + temp + " has won the game by completing the " + family + " family set.");
	   for (int i = 0; i < 4; i++){
		   if (i != id){
			   players[i].lose();
			   players[i].send("Player " + temp + " has won the game by completing the " + family + " family set.");
		   }
		}   
	   if (specNo != 0){
		   for (int k = 0; k < spectators.size(); k++)
			{
				spectators.get(k).send("Player " + temp + " has won the game by completing the " + family + " family set.");
			}
		   
	   }
   }
   
   public synchronized void closed(int id)
   {
	   int temp = 0;
	   serv.print("Client " + id + " has disconnected.\n");
	   if (hasStarted == true){
		   for (int i = 0; i < 4; i++){
			   if (players[i] == clients.get(id)){
				   lostid[step] = i;
				   temp = i;
				   step++;
				   for (int j = 0; j < 4; j++){
			       	   players[j].send("Player " + (temp+1) + " has disconnected from the game.");
					}   
				   if (specNo != 0){
					   for (int k = 0; k < spectators.size(); k++)
						{
							spectators.get(k).send("Player " + (temp+1) + " has disconnected from the game.");
						}
					   
				   }
				   break;
			   }
		   }
		   
		   
	   }
   }
   
   public synchronized void updateID()
   {  
         for (int i = 0; i < 4; i++){
            players[i].sendID(i);
         }
	   	   
   }
   
   public synchronized void handle(String name, String input)
   {  
         for (int i = 0; i < clientNo; i++){
            clients.get(i).send(name + ": " + input);  
         }
	   	   
   }
   
   public synchronized void request(int id, int targetPlayer, int Cardnum) {
	   	tempTarget = targetPlayer;
		tempId = id;
		cardNum = Cardnum;
		players[targetPlayer].request(id, Cardnum);
		for (int i = 0; i < 4; i++){
       	 	players[i].send("Player " + (id+1) + " has requested " + getCardString(cardNum) + " from Player " + (targetPlayer+1));
		}   
		serv.print("Player " + (id+1) + " has requested " + getCardString(cardNum) + " from Player " + (targetPlayer+1) + "\n");
		if (specNo != 0){
			for (int k = 0; k < spectators.size(); k++)
			{
				spectators.get(k).send("Player " + (id+1) + " has requested " + getCardString(cardNum) + " from Player " + (targetPlayer+1));
			}
		}
	}
   
   public synchronized void updateStacks(int id, int size) {
	   for (int i = 0; i < 4; i++){
           players[i].upstacks(id, size);
       }
   }
   
   public synchronized void exchange()
   {  
	     int tempId2 = tempId+1;
	     int tempTarget2 = tempTarget+1;
	   	 players[tempId].trade(1, cardNum);
         players[tempTarget].trade(0, cardNum);
         serv.print("Player " + tempId2 + " has received " + getCardString(cardNum) + " from Player " + tempTarget2 + "\n");
         for (int i = 0; i < 4; i++){
        	 players[i].setTurn(tempId);
        	 players[i].send("Player " + tempId2 + " has received " + getCardString(cardNum) + " from Player " + tempTarget2);
         }   
         if (specNo != 0){
         for (int k = 0; k < spectators.size(); k++)
			{
        	 	spectators.get(k).updateStat(tempId, tempTarget, cardNum);
				spectators.get(k).send("Player " + tempId2 + " has received " + getCardString(cardNum) + " from Player " + tempTarget2);
			}
         }
         
   }
   
   public synchronized void fail() {
	   int tempId2 = tempId+1;
	   int tempTarget2 = tempTarget+1;
	   changeTurn(tempId);
		for (int i = 0; i < 4; i++){
			players[i].send("Player " + tempId2 + " forfeits turn and card " + getCardString(cardNum) + " for not saying Thank You to Player " + tempTarget2);
			
		}
		serv.print("Player " + tempId2 + " forfeits turn and card " + getCardString(cardNum) + " for not saying Thank You to Player " + tempTarget2);
		if (specNo != 0){
		for (int k = 0; k < spectators.size(); k++)
		{
			spectators.get(k).send("Player " + tempId2 + " forfeits turn and card " + getCardString(cardNum) + " for not saying Thank You to Player " + tempTarget2);
		}
		}
   }
   
   public synchronized void gratitude(int yesno) {
	   int tempId2 = tempId+1;
	   int tempTarget2 = tempTarget+1;
		if (yesno == 1){
			players[tempId].await(cardNum, tempTarget);
		}
		else{
			changeTurn(tempId);
			for (int i = 0; i < 4; i++){
				players[i].send("Player " + tempId2 + " forfeits turn as Player " + tempTarget2 + " does not have card " + getCardString(cardNum));
				
			}
			serv.print("Player " + tempId2 + " forfeits turn as Player " + tempTarget2 + " does not have card " + getCardString(cardNum));
			if (specNo != 0){
			for (int k = 0; k < spectators.size(); k++)
			{
				spectators.get(k).send("Player " + tempId2 + " forfeits turn as Player " + tempTarget2 + " does not have card " + getCardString(cardNum));
			}
			}
		}
   }
   
   public synchronized void changeTurn(int id)
   {  
       int random = randomGenerator.nextInt(4);
       int random2 = random+1;
       while (true){
    	   if (random == id){
    		   random = randomGenerator.nextInt(4);
    		   random2 = random+1;
    	   }
    	   else
    		   break;
       }
	   for (int i = 0; i < 4; i++){
		   players[i].send("It is now Player " + random2 + "'s turn");
		   players[i].setTurn(random);
        }   
	   serv.print("It is now Player " + random2 + "'s turn" + "\n"); 
	   if (specNo != 0){
	   for (int k = 0; k < spectators.size(); k++)
		{
			spectators.get(k).send("It is now Player " + random2 + "'s turn");
		}
	   }
   }
   
   public synchronized void updateViews(){
	   if (specNo != 0){
			for (int k = 0; k < specNo; k++)
			{
				spectators.get(k).loadStacks(allCards[0], allCards[1], allCards[2], allCards[3]);
			}
		}
   }
   
   public synchronized void regPlayer(int id) 
   {
	    if (j < 4){
	    	players[j] = clients.get(id);
	    	try{
	    	spectators.remove(id);
	    	specNo--;
	    	}
	    	catch(Exception e){
	    		
	    	}
	    	j++;
	    }
	    if (j == 4){
	    	hasStarted = true;
			for (int i = 0; i < 4; i++){
				getCards();
				players[i].ready(myCards);
				allCards[i] = myCards;
				//players[i].setTurn(0);
				myCards = new ArrayList<Card>();
				
			}
			if (specNo != 0){
				for (int k = 0; k < specNo; k++)
				{
					spectators.get(k).observe();
				}
			}
		}
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
   
   public boolean getCards()
	{
		int i = 8;
		while(i > -1)
		{
			myCards.add(cards.get(i));
			try{
				cards.remove(i);
			}
			catch(Exception e){
				
			}
			i--;
		}
		return false;
	}
   
   public synchronized void upready(String name)
   {
	   	 for (int i = 0; i < clientNo; i++){
           clients.get(i).update(name);  
         }
   }

   public static void main(String args[]) { 
	   serv = new ServerMain();
	   serv.frame.setVisible(true);
	   Server server = null;
       server = new Server(8000); 
   }




}