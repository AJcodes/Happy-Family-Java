package model;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;

import controller.ControlPointsStroke;

public class Text {

	String input;
	int id;
	TextLayout tl;
	private int       _x;
    private int       _y;
    private int width = 80;
    private int height = 80;
    public boolean isSelected;
	
	public Text(String input, int id)
	{
		this.id = id;
		this.input = input;
		
	}
	
	public int getID()
	{
		return id;
	}
	
	public boolean contains(int x, int y) {
        return (x > (_x-20) && x < (_x + width-20) && 
                y > (_y-20) && y < (_y + height-20));
    }
	
	public void draw(Graphics g, Component c, int x, int y) {
		this._x = x;
		this._y = y;
		Graphics2D g2 = (Graphics2D) g;
		FontRenderContext frc = g2.getFontRenderContext();
        Font f = new Font("Trebuchet MS",Font.PLAIN, 17);
        String s = new String(input + " " + (id+1));
        tl = new TextLayout(s, f, frc);
        g2.setColor(Color.black);
        tl.draw(g2, _x, _y);
        if (isSelected){
       	 g2.setStroke(new ControlPointsStroke(3));
       	 g2.setColor(Color.black);
       	 g2.drawRect(_x,_y-20,width,height-40);
        }
    }
	
	public void redraw(){
	}
	
	
}
