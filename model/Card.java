package model;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.ImageIcon;

public class Card {

	String title, familyName;
	String full;
	ImageIcon icon;
	int number;
	private int       _x;
    private int       _y;
	
	public Card(String title , String familyName, int number)
	{
		this.title = title;
		this.familyName = familyName;
		full = familyName + " - " + title;
		this.number = number;
		icon = createImageIcon(("Images/"+title+"_"+familyName+".png"));
	}
	
	public Card()
	{
		icon = createImageIcon(("Images/b.gif"));
	}
	
	public String getfull(){
		return full;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public String getFamilyName()
	{
		return familyName;
	}
	
	public int getNum()
	{
		return number;
	}
	
	public ImageIcon getIcon()
	{
		return icon;
	}
	public ImageIcon createImageIcon(String path)
	{
		java.net.URL imgURL = getClass().getResource(path);
		if (!path.isEmpty()) 
		{
			return new ImageIcon(path);
		} 
		else 
		{
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
	
	public void draw(Graphics g, Component c) {
        icon.paintIcon(c, g, _x, _y);
    }
	
	public void draw(Graphics g, Component c, int x, int y) {
		this._x = x;
		this._y = y;
        icon.paintIcon(c, g, x, y);
    }
	
	public void moveTo(int x, int y) {
        _x = x;
        _y = y;
    }
	
}
