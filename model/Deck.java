package model;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class Deck {
	static FileInputStream inputStream;
	static String familyNamesFile="src/model/family.txt", strLine, familyName;
	static DataInputStream inFamily;
	static BufferedReader brFamily;
	static StringTokenizer tokens;
	static ArrayList<Card> cards = new ArrayList<Card>();
	
	public static ArrayList<Card> getCards()
	{
		
		try
		{
			inputStream = new FileInputStream(familyNamesFile);
			inFamily = new DataInputStream(inputStream);
			brFamily = new BufferedReader(new InputStreamReader(inFamily));
			int p=0;
			while ((strLine = brFamily.readLine()) != null) {
				tokens = new StringTokenizer(strLine);
				familyName = tokens.nextToken();
		
				cards.add(new Card("Daughter", familyName, ++p));
				cards.add(new Card("Father", familyName,  ++p));
				cards.add(new Card("Grandfather", familyName,  ++p));
				cards.add(new Card("Grandmother", familyName,  ++p));
				cards.add(new Card("Mother", familyName, ++p));
				cards.add(new Card("Son", familyName, ++p));
				
			}
			
		}catch(FileNotFoundException e)
		{
			System.out.println("File not found");
		}
		catch(IOException e)
		{
			System.out.println("Problem ooccured in reading from the file");
		}
		return cards;
	}
	
	public static ArrayList<Card> getDummyCards(int limit)
	{
		ArrayList<Card> test = new ArrayList<Card>();
		int j = 0;
		while (j < limit)
		{
			test.add(new Card());
			j++;
		}
		
		return test;
	}
	 
}
