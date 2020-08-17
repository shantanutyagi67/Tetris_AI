import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUI extends JFrame{
	private static final long serialVersionUID = 1L;
	
	tetrominoes asset = new tetrominoes();
	int size = 40, spacing = 1, speed = 300;
	int ran = new Random().nextInt(7);
	int n = ran==0||ran==1 ? 4 : 3;
	int x=3, y=-2, cnt = 0;
	int endState[][] = new int [20][10];
	int score = 0;
	boolean prev = false;
	Vector<Integer> Tminoes = new Vector<Integer>();
	
	public GUI() {
		this.setTitle("TETRIS");
		this.setSize(880+6,880+29); 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
		this.setBackground(new Color(47,61,75));
		
		Board board = new Board();
		this.setContentPane(board);
		Move move = new Move();
		//this.addKeyListener(move);
		this.addKeyListener(move);
		this.setFocusable(true);
		
		for(int i=0;i<20;i++)
			for(int j=0;j<10;j++)
				endState[i][j]= -1;
		for(int i=0;i<70;i++)
			Tminoes.add(i%7);
	}
	
	public class Board extends JPanel{
		private static final long serialVersionUID = 1L;
		
		boolean validDownMove() {
			//y++;
			for(int i=0;i<n;i++) {
				for(int j=0;j<n;j++) {
					if( asset.peices[ran][i][j]==1 && (y+i>=20 || (y>=0&&endState[i+y][j+x]!=-1) ) ) {
						//y--;
						return false;
					}
				}
			}
			//y--;
			return true;
		}
		
		public void paintComponent(Graphics g) {
			Graphics2D g2D = (Graphics2D) g;
			//asset.rotate(2);
			
			//SCORE BOARD
			g2D.setColor(new Color(6,24,33));
			g2D.setFont(new Font("Monospaced", Font.BOLD, 40));
			g2D.drawString("SCORE:",575,30);
			if(score<10)
				g2D.drawString("00000"+ Integer.toString(score),720,30);
			else if(score<100)
				g2D.drawString("0000"+ Integer.toString(score),720,30);
			else if(score<1000)
				g2D.drawString("000"+ Integer.toString(score),720,30);
			else if(score<10000)
				g2D.drawString("00"+ Integer.toString(score),720,30);
			else if(score<100000)
				g2D.drawString("0"+ Integer.toString(score),720,30);
			else
				g2D.drawString(Integer.toString(score),720,30);
			
			// background
			for(int i=1;i<=20;i++) {
				for(int j=6;j<=15;j++) {
					g2D.setColor(new Color(6,24,33));
					g2D.fill(new Rectangle2D.Double(spacing+j*size, spacing+i*size, size-2*spacing, size-2*spacing));
				}
			}
			
			// fixed pieces
			for(int i=0;i<20;i++) {
				for(int j=0;j<10;j++) {
					if(endState[i][j] != -1) {
						g2D.setColor(blockColor(endState[i][j]));
						g2D.fill(new Rectangle2D.Double(spacing+(j+6)*size, spacing+(i+1)*size, size-2*spacing, size-2*spacing));
					}
				}
			}
			
			// moving pieces
			for(int i=0;i<n;i++) {
				for(int j=0;j<n;j++) {
					g2D.setColor(blockColor(ran));
					if(asset.peices[ran][i][j]==1 && x+j+6<16 && x+j+6>=6 && y+i+1<=20 && y+i+1>=1)
						g2D.fill(new Rectangle2D.Double(spacing+(x+j+6)*size, spacing+(y+i+1)*size, size-2*spacing, size-2*spacing));
					
				}
			}
			
			// color pallette
			Toolkit t=Toolkit.getDefaultToolkit();  
	        Image im=t.getImage("images/color_pallette3.jpeg");
	        g2D.drawImage(im, 800,558,this);
	        g2D.setColor(new Color(6,24,33));
			g2D.setFont(new Font("Monospaced", Font.PLAIN, 15));
			g2D.drawString("Pallette",785,850);
			
			checkRow();
			if(!validDownMove()) {
				for(int i=0;i<n;i++)
					for(int j=0;j<n;j++)
						if(asset.peices[ran][i][j]==1)
							endState[i+y-1][j+x] = ran;
				cnt=0;
				x=3;
				y=-2;
				ran = new Random().nextInt(7);
				n = ran==0||ran==1 ? 4 : 3;
			}
			cnt++;
			if(cnt%speed==0&& validDownMove()) {
				cnt=0;
				y++;
			}
			
		}
	}
	
		public class Move implements KeyListener{

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_UP && validRotation())
						asset.rotate(ran);
				if (e.getKeyCode()==KeyEvent.VK_DOWN){
//					ran = new Random().nextInt(7);
//					n = ran==0||ran==1 ? 4 : 3;
					speed=30;
				}
				if (e.getKeyCode()==KeyEvent.VK_LEFT && validLeftMove()) {
					x--;
				}
				if (e.getKeyCode()==KeyEvent.VK_RIGHT && validRightMove()) {
					x++;
				}
				if (e.getKeyCode()==KeyEvent.VK_SPACE){
					speed=10;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				speed = 300;
			}

			@Override
			public void keyTyped(KeyEvent e) {
				
			}
		}
		boolean validRotation() {
			asset.rotate(ran); // check if this rotation is valid
			for(int i=0;i<n;i++) {
				for(int j=0;j<n;j++) {
					if(x<0) {
						x = 0;
						asset.rotate(ran);// undo the rotation
						asset.rotate(ran);
						asset.rotate(ran);
						return true;
					}
					if(x+n>9) {
						x = 10 - n;
						asset.rotate(ran);// undo the rotation
						asset.rotate(ran);
						asset.rotate(ran);
						return true;
					}
					if((asset.peices[ran][i][j]==1 && !(x+j<10 && x+j>=0 && y+i<20 && endState[y+i][x+j]==-1)) ) {
						asset.rotate(ran);// undo the rotation
						asset.rotate(ran);
						asset.rotate(ran);
						return false;
					}
				}
			}
			asset.rotate(ran);// undo the rotation
			asset.rotate(ran);
			asset.rotate(ran);
			return true;
		}
		public Color blockColor(int ran) {
			switch(ran) {
			case 0: {
				return (new Color(212, 111, 125));
			}
			case 1: {
				return (new Color(216, 105, 84));
			}
			case 2: {
				return (new Color(209, 164, 81));
			}
			case 3: {
				return (new Color(153, 197, 89));
			}
			case 4: {
				return (new Color(95, 214, 109));
			}
			case 5: {
				return (new Color(92, 191, 164));
			}
			case 6: {
				return (new Color(98, 124, 189));
			}
		}
			return null;
		}
		boolean validLeftMove() {
			x--;
			for(int i=0;i<n;i++) {
				for(int j=0;j<n;j++) {
					if((asset.peices[ran][i][j]==1 && x+j<0) || (asset.peices[ran][i][j]==1&&x+j>=0&&i+y>=0&& endState[i+y][j+x]!=-1) ) {
						x++;
						return false;
					}
				}
			}
			x++;
			return true;
		}
		boolean validRightMove() {
			x++;
			for(int i=0;i<n;i++) {
				for(int j=0;j<n;j++) {
					if((asset.peices[ran][i][j]==1 && x+j>=10) || (asset.peices[ran][i][j]==1 && x+j<=10 && i+y>=0 && endState[i+y][j+x]!=-1)) {
						x--;
						return false;
					}
				}
			}
			x--;
			return true;
		}
		
		void checkRow() {
			int s = 0;
			for(int i=19;i>=0;i--) {
				int count = 0;
				for(int j=0;j<10;j++) {
					if(endState[i][j]!=-1) count++; 
				}
				if(count==10) {
					s++;
					removeRow(i);
				}
			}
			if(s==4) {
				if(prev) {
					score += 1200;
					prev = true;
				}
				else {
					prev = true;
					score += 800;
				}
			}
			else if(s>0) {
				System.out.println(s);
				score += 100*s;
				prev = false;
			}
		}
		
		private void removeRow(int x) {
			for(int i=x;i>0;i--) {
				for(int j=0;j<10;j++) {
						endState[i][j] = endState[i-1][j];
					}
				}
			for(int j=0;j<10;j++) {
				endState[0][j] = -1;
			}
		}
		public void reset() {
			
		}
}
