import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.random.*;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener{

	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25; // how big apple and snake 
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;  //calculates how many units can fit on screen ? 
	static final int DELAY = 75; // dictates how fast the game is running 
	final int x [] = new int[GAME_UNITS]; // x and y for snake, used game_units cuz snake wont be bigger than game 
	final int y [] = new int[GAME_UNITS];
	int bodyParts = 6; // initial snake body parts 
	int applesEaten; 
	int appleX; 
	int appleY; 
	char direction = 'R';
	boolean running = false; 
	Timer timer; 
	Random random; 
	
	GamePanel() {
		random = new Random(); 
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame(); 
		
		
		
		
	}
	
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g); // still don't understand this 
		draw(g);
		
	}
	
	public void draw(Graphics g) {
		
		if(running) {
			// makes a grid to see where items will be will delete later 
			/*
			for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT); 
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);	
			}
			*/
			//drawing in the apple 
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); 
		
			//Drawing in the snake 
			for(int i = 0; i< bodyParts; i++) {
				if(i==0) { // 0 means its the head apparently
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else {
					g.setColor(new Color(45,180,0)); //this is the body, slightly different green rgb value ig 
					g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			g.setColor(Color.red);
			g.setFont(new Font("Ink Free", Font.BOLD,40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
		}
		else {
			gameOver(g); 
		}
	}
	
	public void newApple() {
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE ; // i think u divide unit size so it fits in one of the squares 
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE)) * UNIT_SIZE;
		
	}
	
	public void move() {
		for(int i=bodyParts; i > 0; i--) {
			x[i] = x[i-1]; // shifting over by one spot 
			y[i] = y[i-1];
		}
		
		switch(direction) {
		case'U':
			y[0] = y[0] - UNIT_SIZE; 
			break;
		case'D':
			y[0] = y[0] + UNIT_SIZE; 
			break;
		case'L':
			x[0] = x[0] - UNIT_SIZE; 
			break;
		case'R':
			x[0] = x[0] + UNIT_SIZE; 
			break;
		}
	}
	public void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			newApple(); 
		}
	}
	public void checkCollisions() {
		//Checks if the head hits the body 
		for(int i=bodyParts; i>0; i--) {
			if((x[0] == x[i])&&(y[0]== y[i])) {  
				running = false; 
			}
		}
		//Check if head touches left border 
		if(x[0]<0) {
			running = false; 
		}
		//Check if head touches right border 
		if(x[0]>SCREEN_WIDTH) {
			running = false; 
				}
		//Check if head touches top border 
		if(y[0] < 0) {
			running = false; 
		}
		//Check if head touches bottom border
		if(y[0] > SCREEN_HEIGHT) {
			running = false;
		}
		
		if(!running) {
			timer.stop(); 
		}
	}
	public void gameOver(Graphics g) {
		//Score 
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD,40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
		//Game Over text
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD,75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(running) {
			move(); 
			checkApple(); 
			checkCollisions(); 
			
		}
		repaint(); 
	}

	public class MyKeyAdapter extends KeyAdapter {
		@Override 
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
				
			}
		}
	}
}
