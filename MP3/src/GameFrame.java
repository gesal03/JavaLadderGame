import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.print.DocFlavor.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GameFrame extends JFrame{
	MyPanel mp = new MyPanel();
	public GameFrame() {
		super("벚꽃톤");
		setLayout(null);
		setContentPane(mp);
		setSize(1000,800);
		setVisible(true);
	}
	
	class MyPanel extends JPanel{
		private final Point START = new Point(100,100);
		private final Point END = new Point(700,700);
		private final Point IMGPOS = new Point(75, 50);
		
		private Vector<Point> horizonList = new Vector<Point>();
		private Vector<Integer> verticalList = new Vector<Integer>();
		private int line = 11; // 이걸 수
		private int interval;
		private Font font = new Font("G마켓 산스 TTF", Font.BOLD, 10);
		
		private UserLabel [] userLabel;
		private JLabel hideScreen;
		private JTextField [] nameField;
//		private JTextField [] resultField;
		private JLabel [] rankingLabel;
		
		public MyPanel() {
			setLayout(null);
//			Color c = new Color(213, 238, 251); // 배경색
			Color c = new Color(220, 240, 255); // 배경색
			this.setBackground(c);
			makeVertical();
			makeHorizon();
			init();
			inputText();
			inputBtn();
		}
		private void inputText() { // 위 아래 이름 넣을 것
			nameField = new JTextField[line];
//			resultField = new JTextField[line];
			rankingLabel = new JLabel[line];
			for(int i=0; i<nameField.length; i++) {
				nameField[i] = new JTextField(10);
				nameField[i].setFont(new Font("G마켓 산스 TTF", Font.BOLD, 10));
				nameField[i].setHorizontalAlignment(JTextField.CENTER);
				nameField[i].setSize(60, 20);
				nameField[i].setLocation(70 + interval*i, 20);
				add(nameField[i]);
				
//				resultField[i] = new JTextField(10);
//				resultField[i].setFont(new Font("Abalone Smile", Font.BOLD, 20));
//				resultField[i].setHorizontalAlignment(JTextField.CENTER);
//				resultField[i].setSize(60, 20);
//				resultField[i].setLocation(70 + interval*i, 725);
//				add(resultField[i]);
				
				rankingLabel[i] = new JLabel(Integer.toString(i + 1));
				rankingLabel[i].setFont(new Font("G마켓 산스 TTF", Font.BOLD, 20));
				rankingLabel[i].setHorizontalAlignment(JLabel.CENTER);
				rankingLabel[i].setSize(60, 30);
				rankingLabel[i].setLocation(70 + interval*i, 740);
				add(rankingLabel[i]);
			}
		}
		
		private void inputBtn() {
			JButton submitBtn = new JButton("시작");
			submitBtn.setFont(new Font("G마켓 산스 TTF", Font.BOLD, 40));
			submitBtn.setOpaque(false);
			submitBtn.setBorderPainted(false);
			submitBtn.setForeground(new Color(255, 210, 225));
			submitBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					remove(hideScreen);
					//JLabel [] resultLabel = new JLabel[line];
					for(int i=0; i<line; i++) {
						String name = nameField[i].getText();
						userLabel[i].setUserName(name);
						
//						String result = resultField[i].getText();
//						resultLabel[i] = new JLabel(result);
//						resultLabel[i].setSize(80, 30);
//						resultLabel[i].setLocation(85 + interval*i, 725);
//						resultLabel[i].setFont(new Font("Abalone Smile", Font.BOLD, 20));
//						add(resultLabel[i]);
						
						remove(nameField[i]);
						//remove(resultField[i]);
						remove(submitBtn);
						
						repaint();
					}
				}
			});
			submitBtn.setSize(200, 70);
			submitBtn.setLocation(750,  350);
			add(submitBtn);
		}
		private void init() {
			//가림판 생성
			java.net.URL url = this.getClass().getClassLoader().getResource("bgimg.jpg");
			hideScreen = new JLabel(new ImageIcon(url));
			hideScreen.setSize(650, 620);
			hideScreen.setLocation(70, 93);
			add(hideScreen);
			
			userLabel = new UserLabel[line];
			for(int i=0; i<userLabel.length; i++) {
				//ImageIcon imageIcon = new ImageIcon("image"+i+".png");
				java.net.URL url2 = this.getClass().getClassLoader().getResource(i+1+".png");
				ImageIcon imageIcon = new ImageIcon(url2);
				Image img = imageIcon.getImage();
				int x = IMGPOS.x + interval*i;
				userLabel[i] = new UserLabel(img, x, START.x + i*interval);
				userLabel[i].setSize(50,70);
				userLabel[i].setLocation(x, IMGPOS.y);
				userLabel[i].addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						UserLabel user = (UserLabel)e.getSource();
						MovingAnimalThread th = new MovingAnimalThread(user);
						th.start();
					}
				});
				add(userLabel[i]);
			}
		}
		
		private void makeHorizon() {
			int distance = (END.y - START.y) / 30; 
			for(int i=START.y + 20; i<END.y; i+=distance) {
				int index = (int)(Math.random()*(verticalList.size()-1));
				int x = verticalList.get(index);
				
				horizonList.add(new Point(x,i));
				horizonList.add(new Point(x+interval, i));
			}
		}
		
		private void makeVertical() {
			int count = line - 1;
			interval = (END.x - START.x) / count;
			for(int i=START.x; i<=END.x; i+=interval)
				verticalList.add(i);
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D)g; 
			for(int i=0; i<verticalList.size();i++) {
				Integer spot = verticalList.get(i);
				g2.setColor(new Color(172, 162, 165));
				g2.setStroke(new BasicStroke(5));
				g2.drawLine(spot, START.y, spot , END.y);
			}
			if(!horizonList.isEmpty()) {
				for(int i=0; i<horizonList.size(); i+=2) {
					Point startPoint = horizonList.get(i);
					Point endPoint = horizonList.get(i+1);
					g.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
				}
			}
		}
		
		class UserLabel extends JLabel{
			private Vector<Integer> left = new Vector<Integer>();
			private Vector<Integer> right = new Vector<Integer>();
			
			private String name="";
			private Image img;
			private int x; // 이동하는 이미지
			private int lineX; // 선 그은거 
			private int destinationX;
			private int y = 30;
			private int lineY = 50; // 좌표 따라
			private boolean isRight = false;
			private boolean isLeft = false;
			private boolean isStop = false;
			

			
			public UserLabel(Image img, int x, int lineX) {
				this.img = img;
				this.x = x;
				this.lineX = lineX;
				checkLine();
			}
			
			public void checkLine() {
				left.removeAllElements();
				right.removeAllElements();
				for(int i=0; i<horizonList.size(); i++) {
					Point spot = horizonList.get(i);
					if(lineX == spot.x) {
						if(i % 2 != 0) {
							left.add(spot.y);
							System.out.println("CheckLine(left): " + spot.y);
						}
						else {
							right.add(spot.y);
							System.out.println("CheckLine(right): " +spot.y);
							
						}
					}
				}
				System.out.println();
			}
			synchronized public void move() {
				if(lineY == 700) {
					try {
						isStop=true;
						wait();
					}catch(InterruptedException e) {}
				}
				y++;
				lineY++;
				if(left.contains(lineY)) {
					isLeft = true;
					destinationX = lineX - interval;
					System.out.println(lineY);
				}
				else if(right.contains(lineY)) {
					isRight = true;
					destinationX = lineX + interval;
					System.out.println(lineY);
				}
				repaint();
				notify();
			}
			synchronized public void moveRight() {
				x++;
				lineX++;
				if(lineX == destinationX) {
					System.out.println("오른쪽 이동 완료 " + lineX);
					checkLine();
					isRight = false;
				}
				repaint();
			}
			synchronized public void moveLeft() {
				x--;
				lineX--;
				if(lineX == destinationX) {
					System.out.println("왼쪽 이동 완료 " + lineX);
					checkLine();
					isLeft = false;
				}
				repaint();
			}
			
			public int getSpotX() { return x; }
			public int getSpotY() { return y; }
			public int getLineY() { return lineY;}
			public boolean getIsRight() { return isRight; }
			public boolean getIsLeft() { return isLeft; }
			public void setUserName(String name) { this.name = name; }		
			public boolean getIsStop() {return isStop;}
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(img, 0, 0, 50, 50, this);
				g.setFont(new Font("G마켓 산스 TTF", Font.BOLD, 10));
				g.drawString(name, 14, 60);
			}
		}
		class MovingAnimalThread extends Thread {
			private UserLabel label;			
			public MovingAnimalThread(UserLabel label) { 
				this.label = label; 
			}
			
			@Override
			public void run() {
				while(true) {
					try {
						sleep(3);
						if(label.getIsRight()) {
							label.moveRight();
						}
						else if(label.getIsLeft()) {
							label.moveLeft();
						}
						else {
							label.move();
						}
						label.setSize(50,70);
						label.setLocation(label.getSpotX(), label.getSpotY());
					}catch(InterruptedException e) {}
				}
			}
		}
	}
}
