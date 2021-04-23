import java.util.Random;

import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Font;

public class Dice extends JPanel {
	private Random r=new Random();
	private int rollCount;
	private int values[]= {0,0,0};
	private JLabel val[];
	/**
	 * Create the panel.
	 */
	public Dice() {
		setLayout(null);
		setBackground(new Color(0, 128, 0));
		rollCount=0;
	}
	public Dice(JLabel v[]) {
		setLayout(null);
		setBackground(new Color(0, 128, 0));
		rollCount=0;	
	}
	
	public void initVals(JLabel v1,JLabel v2,JLabel v3)
	{
		val=new JLabel[3];
		val[0]=v1;
		val[1]=v2;
		val[2]=v3;
	}
	
	public void roll()
	{
		values[rollCount]=r.nextInt(6)+1;
		val[rollCount].setText(""+values[rollCount]);
		if(values[rollCount]==6&&rollCount<2)
			{
				rollCount++;
				roll();
			}
		if(rollCount!=3)
		rollCount=0;
	}
	public void reset()
	{
		for(int i=0;i<3;i++)
		{
			values[i]=0;
			val[i].setText("0");
		}
	}
	public int[] getvalues() {
		return values;
	}
}









