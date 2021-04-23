import java.awt.Color;

import javax.swing.JLabel;

public class token {
	int steps;
	spot position;
	int color;
	boolean won;
	JLabel img;
	
	public token(int col,int index)
	{
		won=false;
		color=col;
		img=new JLabel("t"+(index+1));
		img.setSize(10, 10);
		if(col==0)img.setForeground(Color.GREEN);
		if(col==1)img.setForeground(Color.RED);
		if(col==2)img.setForeground(Color.BLUE);
		if(col==3)img.setForeground(Color.YELLOW);
		
	}
	
	public void renew()
	{
		steps=0;
		position=null;
	}
	public void start(spot start)
	{
		steps=0;
		position=start;
		start.add(this);
	}
	
	public int getColor() {return color;}
	
}
