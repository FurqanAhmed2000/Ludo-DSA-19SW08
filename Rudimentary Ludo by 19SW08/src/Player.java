import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Player {
	
	token t[];
	int color;
	JLabel home;
	JLabel points;
	int tokensHome;
	int tokensWon;
	
	public Player(int col,JLabel h,JLabel p) {
		tokensHome=4;
		tokensWon=0;
		color=col;
		t=new token[4];
		for(int i=0;i<4;i++)
			{
				t[i]=new token(color,i);
			}
		home=h;
		points=p;
		setPoints();
		setCount();
	}
	
	public void tokenWin()
	{
		tokensWon++;
		resetPoints();
	}
	
	private void setPoints()
	{
		points.setText(""+tokensWon);
		points.setFont(new Font("SimSun", Font.PLAIN, 29));
		
		         if(color==0)	points.setForeground(Color.GREEN);
			else if(color==1)	points.setForeground(Color.RED);
			else if(color==2)	points.setForeground(Color.BLUE);
			else if(color==3)	points.setForeground(Color.YELLOW);
	}
	
	private void resetPoints()
	{
		points.setText(tokensWon+"");
	}
	
	private void setCount() {
		home.setText(tokensHome+"");
		home.setFont(new Font("SimSun", Font.PLAIN, 99));
		
			 if(color==0)	home.setForeground(Color.GREEN);
		else if(color==1)	home.setForeground(Color.RED);
		else if(color==2)	home.setForeground(Color.BLUE);
		else if(color==3)	home.setForeground(Color.YELLOW);
	
	}
	
	public void resetCount(int i)
	{
		if(i!=0)tokensHome+=i;
		else tokensHome--;
		
		home.setText(tokensHome+"");
	}
	
	public token gettoken(int i)
	{
		return t[i];
	}
	public boolean tokenHome(int i)
	{
		return t[i].position==null;
	}
	public boolean tokenWon(int i)
	{
		return t[i].won;
	}
	
	
}
