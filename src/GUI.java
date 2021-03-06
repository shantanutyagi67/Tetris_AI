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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JPanel;

// NOTE: add sound fx

public class GUI extends JFrame{
	private static final long serialVersionUID = 1L;
	
	tetrominoes asset = new tetrominoes();
	int size = 40, spacing = 1, speed = 400;
	int ran;
	int n;
	int x,y, cnt = 0, tmpY;
	int endState[][] = new int [20][10];
	int score = 0;
	boolean prev = false, hold = true, end = false, reset = false;
	int T = 0, swap = -1;
	Toolkit t=Toolkit.getDefaultToolkit();  
    Image im=t.getImage("images/color_pallette3.jpeg"), tile;
	Vector<Integer> Tminoes = new Vector<Integer>();
	
	public GUI() {
		this.setTitle("TETRIS");
		this.setSize(880+6,880+29); 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		//this.setResizable(false);
		this.setBackground(new Color(47,61,75));
		
		Board board = new Board();
		this.setContentPane(board);
		Move move = new Move();
		this.addMouseListener(move);
		this.addKeyListener(move);
		this.setFocusable(true);
		
		for(int i=0;i<20;i++)
			for(int j=0;j<10;j++)
				endState[i][j]= -1;
		for(int i=0;i<7;i++)
			Tminoes.add(i%7);
		Collections.shuffle(Tminoes);
		
		ran = Tminoes.get(T);
		n = ran==0||ran==1 ? 4 : 3;
		x=3;
		y=-n+3;
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
			//System.out.println(y);
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
					//tile = t.getImage("tiles/-1.png");
					//g2D.drawImage(tile, (j)*size,(i)*size,this);
				}
			}
			//check if game end
			if(!validDownMove()&&!end) {
				if(y<=0) end = true;
				else {
					for(int i=0;i<n;i++)
						for(int j=0;j<n;j++)
							if(asset.peices[ran][i][j]==1 && i+y-1>=0)
								endState[i+y-1][j+x] = ran;
					//System.out.println(holeBelow());
					T++;
					if(T>=7) {
						T %= 7;
						Collections.shuffle(Tminoes);
					}
					ran = Tminoes.get(T%7);
					n = ran==0||ran==1 ? 4 : 3;
					speed = 400;
					hold = true;   
					cnt=0;
					x=3;
					y=-n+2;
					
				}
				//System.out.println(end);
			}
			
			//held piece
//			if(swap!=-1) {
//				for(int i=0;i<n;i++) {
//					for(int j=0;j<n;j++) {
//						g2D.setColor(blockColor(swap));
//						if(asset.peices[swap][i][j]==1)
//							g2D.fill(new Rectangle2D.Double(spacing+(j+1)*size, spacing+(i+1)*size, size-2*spacing, size-2*spacing));
//					}
//				}
//			}
			
			// fall position of moving piece
			// edit: border highlight
			tmpY = y;
			while(validDownMove()) y++;
			y--;
//			for(int i=0;i<n;i++) {
//				for(int j=0;j<n;j++) {
//					g2D.setColor(new Color(255,255,255,120));
//					if(asset.peices[ran][i][j]==1 && x+j+6<16 && x+j+6>=6 && y+i+1<=20 && y+i+1>=1)
//						g2D.fill(new Rectangle2D.Double(spacing+(x+j+6)*size, spacing+(y+i+1)*size, size-2*spacing, size-2*spacing));
//					
//				}
//			}
			if(!end) {
				for(int i=0;i<n;i++) {
					for(int j=0;j<n;j++) {
	//					g2D.setColor(Color.LIGHT_GRAY);
						if(asset.peices[ran][i][j]==1 && x+j+6<16 && x+j+6>=6 && y+i+1<=20 && y+i+1>=1) {
							g2D.setColor(new Color(255,255,255,120));
							g2D.fill(new Rectangle2D.Double(spacing+(x+j+6)*size, spacing+(y+i+1)*size, size-2*spacing, size-2*spacing));
							g2D.setColor(blockColor(ran,120));
							g2D.fill(new Rectangle2D.Double(spacing+(x+j+6)*size, spacing+(y+i+1)*size, size-2*spacing, size-2*spacing));
	
						}
					}
				}
			}
			y=tmpY;
			
			// fixed pieces
			for(int i=0;i<20;i++) {
				for(int j=0;j<10;j++) {
					if(endState[i][j] != -1) {
						//g2D.setColor(blockColor(endState[i][j],255));
						//g2D.fill(new Rectangle2D.Double(spacing+(j+6)*size, spacing+(i+1)*size, size-2*spacing, size-2*spacing));
						tile = t.getImage("tiles/"+endState[i][j]+".png");
						g2D.drawImage(tile, (j+6)*size,(i+1)*size,this);
					}
				}
			}
			
			// moving pieces
			for(int i=0;i<n;i++) {
				for(int j=0;j<n;j++) {
					g2D.setColor(blockColor(ran,255));
					if(asset.peices[ran][i][j]==1 && x+j+6<16 && x+j+6>=6 && y+i+1<=20 && y+i+1>=1) {
						//g2D.fill(new Rectangle2D.Double(spacing+(x+j+6)*size, spacing+(y+i+1)*size, size-2*spacing, size-2*spacing));
						tile = t.getImage("tiles/"+ran+".png");
						g2D.drawImage(tile, (x+j+6)*size,(y+i+1)*size,this);
					}
				}
			}
			
			
			// color pallette
	        g2D.drawImage(im, 800,558,this);
	        g2D.setColor(new Color(6,24,33));
			g2D.setFont(new Font("Monospaced", Font.PLAIN, 15));
			g2D.drawString("Pallette",785,850);
			
			checkRow();
			
			cnt++;
			if(cnt%speed==0&& validDownMove()&&!end) {
				cnt=0;
				y++;
			}
			if(end & reset) reset();
			if(reset) reset();
		}
	}
	
		public class Move implements KeyListener, MouseListener{

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_UP && validRotation())
						asset.rotate(ran);
				if (e.getKeyCode()==KeyEvent.VK_DOWN){
					speed=30;
					score++;
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
				if(e.getKeyCode()==KeyEvent.VK_R) {
					reset = true;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				speed = 400;
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON3) {
					if(swap==-1&&hold) { //cold start no swap just hold
						swap = ran;
						T++;
						if(T>=7) {
							T %= 7;
							Collections.shuffle(Tminoes);
						}
						ran = Tminoes.get(T%7);
						n = ran==0||ran==1 ? 4 : 3;
						x = 3;
						y = -n+2;
					}
					else if(hold){ // swap and hold
						int temp = swap;
						swap = ran;
						ran = temp;
						n = ran==0||ran==1 ? 4 : 3;
						x = 3;
						y = -n+3;
						hold = false;
					}
					
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
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
//					if((asset.peices[ran][i][j]==1 && !(x+j<10 && x+j>=0 && y+i<20 && endState[y+i][x+j]==-1)) ) {
//						asset.rotate(ran);// undo the rotation
//						asset.rotate(ran);
//						asset.rotate(ran);
//						return false;
//					}
					if((asset.peices[ran][i][j]==1 && endState[y+i][x+j]!=-1) ) {
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
		public boolean checkEnd() {
			return true;
		}
		public Color blockColor(int ran, int t) {
			switch(ran) {
			case 0: {
				return (new Color(212, 111, 125, t));
			}
			case 1: {
				return (new Color(216, 105, 84, t));
			}
			case 2: {
				return (new Color(209, 164, 81, t));
			}
			case 3: {
				return (new Color(153, 197, 89, t));
			}
			case 4: {
				return (new Color(95, 214, 109, t));
			}
			case 5: {
				return (new Color(92, 191, 164, t));
			}
			case 6: {
				return (new Color(98, 124, 189, t));
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
			for(int i=0;i<20;i++) {
				int count = 0;
				for(int j=0;j<10;j++) {
					if(endState[i][j]!=-1) {
						count++; 
						
					}
				}
				if(count==10) {
					//audioPlayer.play(); 
					s++;
					removeRow(i);
					//audioPlayer.pause();; 
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
				//System.out.println(s);
				score += 100*s;
				prev = false;
			}
		}
		
		private void removeRow(int x) {
			//SimpleAudioPlayer audioPlayer1 = new SimpleAudioPlayer("C:\\Users\\geekSA67\\Videos\\youtube\\audios\\meme\\roblox-death-sound_1.wav");
			for(int i=x;i>0;i--) {
				for(int j=0;j<10;j++) {
						endState[i][j] = endState[i-1][j];
					}
				}
			for(int j=0;j<10;j++) {
				endState[0][j] = -1;
			}
		}
		
		public boolean holeBelow() {
			for(int i=y;i<y+n;i++) {
				for(int j=x;j<x+n;j++) {
					if(i>=0&&j>=0&&i<19&&j<9&&endState[i][j]!=-1&&endState[i+1][j]==-1) return true;
				}
			}
			return false;
		}
		
		public void reset() {
			end = false;
			for(int i=0;i<20;i++)
				for(int j=0;j<10;j++)
					endState[i][j]= -1;
			T++;
			ran = Tminoes.get(T%7);
			n = ran==0||ran==1 ? 4 : 3;
			speed = 400;
			hold = true;   
			cnt=0;
			x=3;
			y=-n+3;
			score = 0;
			prev = false;
			hold = true;
			end = false;
			swap = -1;
			reset = false;
		}
}
