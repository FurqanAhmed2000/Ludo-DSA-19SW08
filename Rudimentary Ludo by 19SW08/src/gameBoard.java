import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class gameBoard {
	Linkedspots Path[];
	Linkedspots endPath[];
	
	gameBoard(){
		Path=new Linkedspots[4];
		endPath=new Linkedspots[4];
		
		for(int i=0;i<4;i++)
		{
			Path[i]=new Linkedspots(13);
			endPath[i]=new Linkedspots(5);
		}
		
		Path[0].append(Path[1]);
		Path[1].append(Path[2]);
		Path[2].append(Path[3]);
		Path[3].append(Path[0]);
	}
}


class Linkedspots{
	
	spot start;
	spot end;
	
	
	Linkedspots(int size)
	{
		int i=1;
		start=new spot(true);
		spot p=start;
		while(i<size-1) {
			if(i==8)
				p.next=new spot(true);
			else
				p.next=new spot(false);
			p=p.next;
			i++;
		}
		end=p.next=new spot(false);
	}
	
	public void append(Linkedspots l)
	{
		end.next=l.start;
	}
}

class spot{
	boolean safe;
	int occupied;
	token[] occupants=new token[16];
	JPanel panel;
	spot next;
	
	public spot(boolean safe) {
		this.safe=safe;
		occupied=0;
		next=null;
	}
	
	public void setPanel(JPanel p)
	{
		panel=p;
		if(safe) {
			panel.setBackground(Color.LIGHT_GRAY);
		}
	}
	public void setVPanel(JPanel p)
	{
		panel=p;
		panel.setBackground(Color.DARK_GRAY);
	}
	public boolean isSafe() {return safe;}
	
	public void add(token t) {
		panel.add(t.img);
		panel.revalidate();
		panel.repaint();
		
		occupants[occupied]=t;
		occupied++;
	}
	public void removeall() {
		panel.removeAll();
		panel.revalidate();
		panel.repaint();
		occupants=new token[16];
		occupied=0;
	}
	public void remove(token t) {
		panel.remove(t.img);
		panel.revalidate();
		panel.repaint();
		int i=occupied-1;
		if(i==0) {
			this.removeall();
			return;
		}
		
		boolean found=false;
		
		while(!found)
		{
			if(occupants[i-1]==t) {
				found=true;
			}
			occupants[i-1]=occupants[i];
			i--;
		}
		occupants[occupied--]=null;
	}
	
}
