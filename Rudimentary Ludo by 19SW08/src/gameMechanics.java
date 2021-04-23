import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;

public class gameMechanics{
	
	private JFrame MainGame;

	private JLabel lblNewLabel;
	private JLabel label_1;
	private JLabel label_2;
	private JLabel label_3;
	
	private int turn;
	private Player players[];
	private Dice dice;
	private int rollCount;
	private int rolls[];
	private int Rank;
	private int Winners[];
	
	JLabel tokensHome[];
	JLabel points[];
	
	JButton moveT[]=new JButton[4];
	JButton rolldice;
	JPanel path[];
	JPanel Vpath[];
	JPanel Board;
	
	
	private JButton startToken;
	private JLabel turntext;
	
	private String colors[]= {"Green","Red","Blue","Yellow"};
	
	gameBoard b1;
	
	gameMechanics()
	{
		this(4);
	}
	gameMechanics(int Playercount)
	{
		instantiate(Playercount);
	}
	
	public void instantiate(int Playercount)
	{
		b1=new gameBoard();
		
		if(Playercount<=1||Playercount>4)throw new IllegalStateException("Illegal Number of Players");
		Rank=1;
		turn=0;
		
		Winners=new int[4];
		players=new Player[Playercount];
		
		tokensHome=new JLabel[4];
		points=new JLabel[4];
		
		for(int i=0;i<players.length;i++) {
			points[i]=new JLabel();
			tokensHome[i]=new JLabel();
			players[i]=new Player(i,tokensHome[i],points[i]);
		}
		
		Vpath=new JPanel[20];
		path=new JPanel[52];
		initialize();
		setVisuals();
	}
	
	public void setVisuals()
	{
		
		spot a=b1.endPath[0].start;
		spot b=b1.endPath[1].start;
		spot c=b1.endPath[2].start;
		spot d=b1.endPath[3].start;
		
		for(int i=0;i<5;i++)
		{
			a.setVPanel(Vpath[i]);
			b.setVPanel(Vpath[i+5]);
			c.setVPanel(Vpath[i+10]);
			d.setVPanel(Vpath[i+15]);
			a=a.next;
			b=b.next;
			c=c.next;
			d=d.next;
		}
		
		
		spot p=this.b1.Path[0].start;
		int i=1;
		while(p.next!=b1.Path[0].start) {
			p.setPanel(path[i]);
			p=p.next;
			i++;
		}
		p.setPanel(path[0]);
	}
	
	public void arrangePath(JPanel p[],int index,int x,int y,boolean direction,boolean up,int num)
	{
		int	dir=direction? 1:-1;
		for(int i=0;i<num;i++)
		{
			p[index+i]=new JPanel();
			if(up)p[index+i].setBounds(x,y+dir*i*38,37,37);
			else  p[index+i].setBounds(x+dir*i*38,y,37,37);
			p[index+i].setBackground(Color.gray);
			Board.add(p[index+i]);
			
		}
	}
	
	public void rankCheck()
	{
		if(players[turn].tokensWon==4)
		{
			String s="Player "+colors[turn]+" has achieved Rank:"+Rank;
			JOptionPane.showMessageDialog(Board, s+"\n Please Skip this players turn from now on");
			Winners[Rank-1]=turn;
			Rank++;
		}
		if(Rank>4) {
			String s="";
			for(int i=0;i<4;i++)s+="Rank "+(i+1)+": Player "+colors[Winners[i]]+"\n";
			int i=JOptionPane.showConfirmDialog(Board, "Game has ended Following are the results \n"+s+"Do You wanna play another one?", "Game Ended", 0);
			
			if(i==1)
			{
				MainGame.dispose();
			}
			else {
				MainGame.dispose();
				instantiate(4);
				MainGame.setVisible(true);
			}
			
		}
	}
	
	public void move(token t,int id)
	{
		int roll=rolls[0];
		spot p=t.position;
		while(roll-->0)
		{
			try {
				if(p.next.next==b1.Path[turn].start)
					p=b1.endPath[turn].start;
				else p=p.next;
				
				if(p==null)throw new NullPointerException();
			}
			catch(NullPointerException e) {
				System.out.println(roll);
				if(roll==0)
					{
						players[turn].tokenWin();
						rankCheck();
						t.won=true;
						t.position.remove(t);
						rollUsed();
						return;
					}
				else {
					moveT[id].setEnabled(false);
					return;
				}
				
			}
		}
		try {
			if((!p.isSafe())&&p.occupants[0].color!=t.color) {
				knockout(p);
			}
		}
		catch(NullPointerException e) {
			System.out.println("handled");
		}
		t.position.remove(t);
		t.position=p;
		p.add(t);
		rollUsed();
	}
	
	public void rollUsed()
	{
		for(int i=0;i<rollCount-1;i++)rolls[i]=rolls[i+1];
		rolls[rollCount-1]=0;
		
		if(rolls[0]==0)turnChange();
		
		if(rolls[0]==6&&players[turn].tokensHome>0)startToken.setEnabled(true);
		else startToken.setEnabled(false);
		rollCount--;
	}
	
	
	public void knockout(spot p) {
		
		players[p.occupants[0].color].resetCount(p.occupied);
		for(int i=0;i<p.occupied;i++)
		{
			p.occupants[i].renew();
		}
		p.removeall();
		
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					gameMechanics window = new gameMechanics();
					window.MainGame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		MainGame = new JFrame();
		MainGame.setResizable(false);
		MainGame.getContentPane().setEnabled(false);
		MainGame.setTitle("Ludo 19SW08");
		MainGame.setBounds(100, 100, 1273, 761);
		MainGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MainGame.getContentPane().setLayout(null);
		
		rolldice = new JButton("Roll");
		
		rolldice.setFont(new Font("Atmosphere Script Typeface", Font.PLAIN, 20));
		rolldice.setBounds(1112, 448, 137, 47);
		MainGame.getContentPane().add(rolldice);
		
		dice = new Dice();
		dice.setBounds(959, 309, 290, 125);
		MainGame.getContentPane().add(dice);
		
		JLabel vtext1 = new JLabel("0");
		vtext1.setFont(new Font("Malgun Gothic", Font.PLAIN, 54));
		vtext1.setBounds(63, 37, 30, 51);
		dice.add(vtext1);
		
		JLabel vtext2 = new JLabel("0");
		vtext2.setFont(new Font("Malgun Gothic", Font.PLAIN, 54));
		vtext2.setBounds(133, 37, 30, 51);
		dice.add(vtext2);
		
		JLabel vtext3 = new JLabel("0");
		vtext3.setFont(new Font("Malgun Gothic", Font.PLAIN, 54));
		vtext3.setBounds(203, 37, 30, 51);
		dice.add(vtext3);
		
		dice.initVals(vtext1, vtext2, vtext3);
		
		JLabel rnum = new JLabel("0");
		rnum.setBounds(55, 10, 34, 72);
		rnum.setFont(new Font("Segoe UI Black", Font.PLAIN, 39));
		rnum.setForeground(new Color(255, 255, 255));
		
		moveT[0] = new JButton("Token 1");
		moveT[0].setEnabled(false);
		moveT[0].setBounds(1073, 247, 75, 23);
		MainGame.getContentPane().add(moveT[0]);
		
		moveT[1] = new JButton("Token 2");
		moveT[1].setEnabled(false);
		moveT[1].setBounds(1174, 247, 75, 23);
		MainGame.getContentPane().add(moveT[1]);
		
		moveT[2] = new JButton("Token 3");
		moveT[2].setEnabled(false);
		moveT[2].setBounds(1073, 280, 75, 23);
		MainGame.getContentPane().add(moveT[2]);
		
		moveT[3] = new JButton("Token 4");
		moveT[3].setEnabled(false);
		moveT[3].setBounds(1174, 280, 75, 23);
		MainGame.getContentPane().add(moveT[3]);
		
		startToken = new JButton("Unlock");
		startToken.setEnabled(false);
		
		
		startToken.setBounds(1174, 204, 75, 23);
		MainGame.getContentPane().add(startToken);
		
		turntext = new JLabel("Player turn: "+colors[turn]);
		turntext.setFont(new Font("MS UI Gothic", Font.PLAIN, 31));
		turntext.setBounds(975, 100, 274, 54);
		MainGame.getContentPane().add(turntext);
		
		
		
		JButton skipTurn = new JButton("Skip");
	
		
		skipTurn.setBounds(996, 472, 93, 23);
		MainGame.getContentPane().add(skipTurn);
		
		Board = new JPanel();
		Board.setBounds(10, 10, 612, 659);
		MainGame.getContentPane().add(Board);
		Board.setLayout(null);
		
		lblNewLabel = new JLabel("start");
		lblNewLabel.setForeground(Color.GREEN);
		lblNewLabel.setBounds(52, 254, 39, 15);
		Board.add(lblNewLabel);
		
		label_1 = new JLabel("start");
		label_1.setForeground(Color.RED);
		label_1.setBounds(355, 99, 39, 15);
		Board.add(label_1);
		
		label_2 = new JLabel("start");
		label_2.setForeground(Color.ORANGE);
		label_2.setBounds(198, 561, 39, 15);
		Board.add(label_2);
		
		label_3 = new JLabel("start");
		label_3.setForeground(Color.BLUE);
		label_3.setBounds(509, 399, 39, 15);
		Board.add(label_3);
		
		
		
		points[0].setBounds(243, 311, 17, 56);
		points[1].setBounds(286, 268, 17, 56);
		points[2].setBounds(330, 311, 17, 56);
		points[3].setBounds(286, 351, 17, 56);
		
		tokensHome[0].setBounds(102, 96, 66, 148);
		tokensHome[1].setBounds(432, 96, 66, 148);
		tokensHome[3].setBounds(102, 396, 66, 148);
		tokensHome[2].setBounds(432, 396, 66, 148);
		
		
		
		for(int i=0;i<4;i++)
		{
			Board.add(points[i]);
			Board.add(tokensHome[i]);
		}
		
		arrangePath(path,0,10,280,true,false,6);
		arrangePath(path,6,238,242,false,true,6);
		arrangePath(path,12,276,52,false,true,1);
		arrangePath(path,13,314,52,true,true,6);
		arrangePath(path,19,351,280,true,false,6);
		arrangePath(path,25,541,318,true,false,1);
		arrangePath(path,26,541,356,false,false,6);
		arrangePath(path,32,314,394,true,true,6);
		arrangePath(path,38,276,584,true,true,1);
		arrangePath(path,39,238,584,false,true,6);
		arrangePath(path,45,200,356,false,false,6);
		arrangePath(path,51,10,318,false,true,1);
		
		
		arrangePath(Vpath,0,48,318,true,false,5);
		arrangePath(Vpath,5,276,90,true,true,5);
		arrangePath(Vpath,10,503,318,false,false,5);
		arrangePath(Vpath,15,276,546,false,true,5);
		
		
		
		startToken.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i=0;i<4;i++)
				{
					if(players[turn].tokenHome(i)&&!players[turn].tokenWon(i))
						{
							players[turn].t[i].start(b1.Path[players[turn].color].start);
							moveT[i].setEnabled(true);
							break;
						}
				}
				rollUsed();
				players[turn].resetCount(0);
			}
		});
		
		
		skipTurn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				turnChange();
				dice.reset();
				rolls=null;
			}
		});
		
		rolldice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rollCount=0;
				dice.reset();
				dice.roll();
				rolls=dice.getvalues();	
				
				for(int i=0;i<3;i++)
					if(rolls[i]==0)break;
					else rollCount++;
				
				//rolldice.setEnabled(false);
				check6();
				enableTokens(true);
			}
		});
		
		
		moveT[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				move(players[turn].t[0],0);
			}
		});
		moveT[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				move(players[turn].t[1],1);
			}
		});
		moveT[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				move(players[turn].t[2],2);
			}
		});
		moveT[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				move(players[turn].t[3],3);
			}
		});
		
	}
	
	public void check6()
	{
		int n=0;
		for(int i=0;i<3;i++) 
				if(rolls[i]==6) n++;
		
		if(n==3)turnChange();
		if(n==0||n==3||players[turn].tokensHome==0)startToken.setEnabled(false);
		else	startToken.setEnabled(true);
	}

	public void turnChange()
	{
		if(turn==3)turn-=3;
		else turn++;
		
		turntext.setText("Player turn: "+colors[turn]);
		enableTokens(false);
		startToken.setEnabled(false);
		dice.reset();
		rolldice.setEnabled(true);
		
	}
	
	public void enableTokens(boolean enable)
	{
		if(enable) {
			for(int i=0;i<4;i++)
			{
				if(players[turn].tokenHome(i)||players[turn].tokenWon(i))moveT[i].setEnabled(false);
				else moveT[i].setEnabled(true);
			}
		}
		else
		{
			for(int i=0;i<4;i++)
			{
				moveT[i].setEnabled(false);
			}
		}
	}
}



