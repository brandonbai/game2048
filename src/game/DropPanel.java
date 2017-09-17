package game;

import game.MyButton;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

/**
 * ����ģʽ�����
 * 
 */
public class DropPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	//���ذ�ť����ͣ��ť
	private MyButton backButton, pauseButton;
	//��Ϸ��������
	private Cell[][] cells = new Cell[8][6];
	//��һ�����µķ���
	private Cell nextCell;
	//��ǰ��Ϸ����
	private int nowScore;
	//������Ϸ��߷���
	private int topScore;
	//��Ϸ���е�����״̬
	private static final int RUNNING = 1;
	private static final int PAUSE = 2;
	private static final int GAMEOVER = 3;
	//��Ϸ״̬
	private int state = 1;
	/**
	 * ���췽��
	 */
	public DropPanel() {
		
		Color backColor = new Color(209, 174, 115);
		
		backButton = new MyButton("back", backColor);
		backButton.setBounds(277, 79, 60, 40);
		backButton.addActionListener(this);
		this.add(backButton);

		pauseButton = new MyButton("pause", backColor);
		pauseButton.setBounds(110, 79, 60, 40);
		pauseButton.addActionListener(this);
		this.add(pauseButton);
		
		for(int i = 0; i < cells.length; i++){
			for(int j = 0; j<cells[i].length; j++){
				cells[i][j] = new Cell(0, i, j);
			}
		}
		try {
			topScore();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**��Ϸ���еķ���*/
	public void run() {
		//���̼���
		keyListener();
		//������һ�����µķ���
		createAction();
		//���������Ϸ��
		enterAction();
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				public void run() {
						//�������估�ϲ�
						addAction();
				}
			}, 100, 20);
	}
	/**���̼���*/
	private void keyListener() {
		//��ȡ����
		this.requestFocus();
		//���̼���
		this.addKeyListener(new KeyAdapter() {
			 public void keyPressed(KeyEvent e) {
				 int code = e.getKeyCode();
				 if(state == RUNNING) {
					 if(code == 37) {
						 //��������
						 leftAciton();
					 }else if(code == 39) {
						 //��������
						 rightAciton();
					 }else if(code == 40) {
						 //��������
						 downAction();
					 }
					 repaint();
				 }else if(state == GAMEOVER && code == KeyEvent.VK_SPACE) {
					 nowScore = 0;
					 for(int i = 0; i < cells.length; i++) {
						 for(int j = 0; j<cells[i].length; j++) {
							 cells[i][j] = new Cell(0, i, j);
						 }
					 }
					 state = RUNNING;
				 }
			 }
		});
	}
	/**�������Ƶķ���*/
	private void leftAciton() {
		for(int i = 0; i < cells.length-1; i++) {
			for(int j = 1; j < cells[i].length; j++) {
				if(cells[i][j].getNumber() != 0) {
					if(cells[i][j-1].getNumber() == 0 && cells[i+1][j].getNumber() == 0) {
						cells[i][j-1]=new Cell(cells[i][j].getNumber(), i, j-1);
						cells[i][j] = new Cell(0, i, j);
						return ;
					}
				}
			}
		}
	}
	/**�������Ƶķ���*/
	private void rightAciton() {
		for(int i = 0; i < cells.length-1; i++) {
			for(int j = 0; j < cells[i].length-1; j++) {
				if(cells[i][j].getNumber() != 0) {
					if(cells[i][j+1].getNumber() == 0 && cells[i+1][j].getNumber() == 0) {
						cells[i][j+1] = new Cell(cells[i][j].getNumber(), i, j+1);
						cells[i][j] = new Cell(0, i, j);
						return ;
					}
				}
			}
		}
	}
	/**�������Ƶķ���*/
	private void downAction() {
		for(int i = 0; i < cells.length-1; i++) {
			for(int j = 0; j < cells[i].length; j++) {
				if(cells[i][j].getNumber() != 0 && cells[i+1][j].getNumber() == 0) {
					cells[i+1][j] = new Cell(cells[i][j].getNumber(), i+1, j);
					cells[i][j] = new Cell(0, i, j);
					return ;
				}
			}
		}
	}
	/**�������估�ϲ��ķ���*/
	private void addAction() {
		for(int j = 0; j < cells[0].length; j++) {
			for(int i = 0; i < cells.length; i++) {
				if(cells[i][j].getNumber() != 0 && state == RUNNING) {
					if(i < cells.length-1 && cells[i+1][j].getNumber() == 0) {
						cells[i+1][j]=new Cell(cells[i][j].getNumber(), i+1, j);
						cells[i][j] = new Cell(0, i, j);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						repaint();
						continue;
					}
					//��һ��ϲ����
					if(i < cells.length-1 && j > 0 && j < cells[i].length-1) {
						//�����ֺϲ����
						if(cells[i][j].getNumber() == cells[i][j-1].getNumber()
								&& cells[i][j].getNumber() != cells[i][j+1].getNumber()
								&& cells[i][j].getNumber() != cells[i+1][j].getNumber()) {
							cells[i][j] = new Cell(cells[i][j-1].getNumber()*2, i, j);
							cells[i][j-1] = new Cell(0, i, j-1);
							nowScore += cells[i][j].getNumber();
						}
						//�����ֺϲ����
						if(cells[i][j].getNumber() != cells[i][j-1].getNumber()
								&& cells[i][j].getNumber() == cells[i][j+1].getNumber()
								&& cells[i][j].getNumber() != cells[i+1][j].getNumber()) {
							cells[i][j] = new Cell(cells[i][j+1].getNumber()*2, i, j);
							cells[i][j+1] = new Cell(0, i, j+1);
							nowScore += cells[i][j].getNumber();
						}
						//�ڶ��ֺϲ����
						if(cells[i][j].getNumber() == cells[i][j-1].getNumber()
								&& cells[i][j].getNumber() == cells[i][j+1].getNumber()
								&& cells[i][j].getNumber() != cells[i+1][j].getNumber()) {
							cells[i][j] = new Cell(cells[i][j-1].getNumber()*2*2, i, j);
							cells[i][j-1] = new Cell(0, i, j-1);
							cells[i][j+1] = new Cell(0, i, j+1);
							nowScore += cells[i][j].getNumber();
						}
						//�����ֺϲ����
						if(cells[i][j].getNumber() != cells[i][j-1].getNumber()
								&& cells[i][j].getNumber() != cells[i][j+1].getNumber()
								&& cells[i][j].getNumber() == cells[i+1][j].getNumber()) {
							cells[i+1][j] = new Cell(cells[i][j].getNumber()*2, i+1, j);
							cells[i][j] = new Cell(0, i, j);
							nowScore += cells[i+1][j].getNumber();
						}
						//�����ֺϲ����
						if(cells[i][j].getNumber() == cells[i][j-1].getNumber()
								&& cells[i][j].getNumber() != cells[i][j+1].getNumber()
								&& cells[i][j].getNumber() == cells[i+1][j].getNumber()) {
							cells[i+1][j] = new Cell(cells[i][j].getNumber()*2*2, i+1, j);
							cells[i][j] = new Cell(0, i, j);
							cells[i][j-1] = new Cell(0, i, j-1);
							nowScore += cells[i+1][j].getNumber();
						}
						//�����ֺϲ����
						if(cells[i][j].getNumber() != cells[i][j-1].getNumber()
								&& cells[i][j].getNumber() == cells[i][j+1].getNumber()
								&& cells[i][j].getNumber() == cells[i+1][j].getNumber()) {
							cells[i+1][j] = new Cell(cells[i][j].getNumber()*2*2, i+1, j);
							cells[i][j] = new Cell(0, i, j);
							cells[i][j+1] = new Cell(0, i, j+1);
							nowScore += cells[i+1][j].getNumber();
						}
						//��һ�ֺϲ����
						if(cells[i][j].getNumber() == cells[i][j-1].getNumber()
								&& cells[i][j].getNumber() == cells[i][j+1].getNumber()
								&& cells[i][j].getNumber() == cells[i+1][j].getNumber()) {
							cells[i+1][j] = new Cell(cells[i][j].getNumber()*2*2*2, i+1, j);
							cells[i][j] = new Cell(0, i, j);
							cells[i][j-1] = new Cell(0, i, j-1);
							cells[i][j+1] = new Cell(0, i, j+1);
							nowScore += cells[i+1][j].getNumber();
						}
					//�ڶ���ϲ����	
					}else if(i < cells.length-1 && j == cells[i].length-1) {
						//�ڰ��ֺϲ���� 
						if(cells[i][j].getNumber() == cells[i][j-1].getNumber()
								&& cells[i][j].getNumber() == cells[i+1][j].getNumber()) {
							cells[i+1][j] = new Cell(cells[i][j].getNumber()*2*2, i+1, j);
							cells[i][j] = new Cell(0, i, j);
							cells[i][j-1] = new Cell(0, i, j-1);
							nowScore += cells[i+1][j].getNumber();
						 }//�ھ��ֺϲ����
						 if(cells[i][j].getNumber() == cells[i][j-1].getNumber()
								&& cells[i][j].getNumber() != cells[i+1][j].getNumber()) {
							cells[i][j] = new Cell(cells[i][j-1].getNumber()*2, i, j);
							cells[i][j-1] = new Cell(0, i, j-1);
							nowScore += cells[i][j].getNumber();
						 }//��ʮ�ֺϲ����
						 if(cells[i][j].getNumber() != cells[i][j-1].getNumber()
								&& cells[i][j].getNumber() == cells[i+1][j].getNumber()) {
							cells[i+1][j] = new Cell(cells[i][j].getNumber()*2, i+1, j);
							cells[i][j] = new Cell(0, i, j);
							nowScore += cells[i+1][j].getNumber();
						 }
					//������ϲ����	 
					}else if(i < cells.length-1 && j == 0) {
						//��ʮһ�ֺϲ����
						if(cells[i][j].getNumber() == cells[i][j+1].getNumber()
								&& cells[i][j].getNumber() == cells[i+1][j].getNumber()) {
							cells[i+1][j] = new Cell(cells[i][j].getNumber()*2*2, i+1, j);
							cells[i][j] = new Cell(0, i, j);
							cells[i][j+1] = new Cell(0, i, j+1);
							nowScore += cells[i+1][j].getNumber();
						}//��ʮ���ֺϲ����
						if(cells[i][j].getNumber() == cells[i][j+1].getNumber()
								&& cells[i][j].getNumber() != cells[i+1][j].getNumber()) {
							cells[i][j] = new Cell(cells[i][j+1].getNumber()*2, i, j);
							cells[i][j+1] = new Cell(0, i, j+1);
							nowScore += cells[i][j].getNumber();
						}//��ʮ���ֺϲ����
						if(cells[i][j].getNumber() != cells[i][j+1].getNumber()
								&& cells[i][j].getNumber() == cells[i+1][j].getNumber()) {
							cells[i+1][j] = new Cell(cells[i][j].getNumber()*2, i+1, j);
							cells[i][j] = new Cell(0, i, j);
							nowScore += cells[i+1][j].getNumber();
						}
					//������ϲ����	
					}else if(i == cells.length-1 &&  j > 0 && j < cells[i].length-1) {
						//��ʮ���ֺϲ����
						if(cells[i][j].getNumber() == cells[i][j-1].getNumber()
								&& cells[i][j].getNumber() == cells[i][j+1].getNumber()) {
							cells[i][j] = new Cell(cells[i][j-1].getNumber()*2*2, i, j);
							cells[i][j-1] = new Cell(0, i, j-1);
							cells[i][j+1] = new Cell(0, i, j+1);
							nowScore += cells[i][j].getNumber();
						}//��ʮ���ֺϲ����
						if(cells[i][j].getNumber() != cells[i][j-1].getNumber()
								&& cells[i][j].getNumber() == cells[i][j+1].getNumber()) {
							cells[i][j] = new Cell(cells[i][j+1].getNumber()*2, i, j);
							cells[i][j+1] = new Cell(0, i, j+1);
							nowScore += cells[i][j].getNumber();
						}//��ʮ���ֺϲ����
						if(cells[i][j].getNumber() == cells[i][j-1].getNumber()
								&& cells[i][j].getNumber() != cells[i][j+1].getNumber()) {
							cells[i][j] = new Cell(cells[i][j-1].getNumber()*2, i, j);
							cells[i][j-1] = new Cell(0, i, j-1);
							nowScore += cells[i][j].getNumber();
						}
					//������ϲ����	
					}else if(i == cells.length-1 && j == cells[i].length-1) {
						//��ʮ���ֺϲ����
						if(cells[i][j].getNumber() == cells[i][j-1].getNumber()) {
							cells[i][j] = new Cell(cells[i][j-1].getNumber()*2, i, j);
							cells[i][j-1] = new Cell(0, i, j-1);
							nowScore += cells[i][j].getNumber();
						}
					//������ϲ����	
					}else if(i == cells.length-1 && j == 0) {
						//��ʮ���ֺϲ����
						if(cells[i][j].getNumber() == cells[i][j+1].getNumber()) {
							cells[i][j] = new Cell(cells[i][j+1].getNumber()*2, i, j);
							cells[i][j+1] = new Cell(0, i, j+1);
							nowScore += cells[i][j].getNumber();
						}
					}
				}
			}
		}
		repaint();
		//�������ȫ������������µķ���
		if(!isNoDroped())
			enterAction();
		if(isGameOver())
			state = GAMEOVER;
		repaint();
	}
	/**�ж��Ƿ���δ���µķ���*/
	private boolean isNoDroped() {
		for(int i = 0; i < cells.length-1; i++) {
			for(int j = 0; j < cells[i].length; j++) {
				if(cells[i][j].getNumber() != 0) {
					if( cells[i+1][j].getNumber() == 0) {
						return true;  //isNoDroped = true;
					}else if(cells[i][j].getNumber() == cells[i+1][j].getNumber()) {
						return true;
					}
				}
			}
		}
		return false;
	}
	/**��һ�����������Ϸ���ķ���*/
	private void enterAction() {
		if(cells[0][3].getNumber() == 0) {
			cells[0][3] = new Cell(nextCell.getNumber(), 0, 3);
			createAction();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			repaint();
		}
	}
	/**������һ�����µķ���*/
	private void createAction() {
		Random random = new Random();
		int num = random.nextInt(6);
		nextCell = new Cell((int)(Math.pow(2, num+1)));
	}
	/**�ж���Ϸ�Ƿ����*/
	private boolean isGameOver() {
		for(int i = 0; i < cells.length; i++) {
			if(cells[i][3].getNumber() == 0 || i < cells.length-1 
					&& cells[i][3].getNumber() == cells[i+1][3].getNumber()) {
				try {
					topScore();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return false; //isGameOver = false;
			}
		}
		return true;
	}
	public void paint(Graphics g) {
		super.paint(g);
		//������
		paintBG(g);
		//������
		paintCell(g);
		if(state == GAMEOVER) {
			paintGameOver(g) ;
		}
	}
	/**����Ϸ����*/
	public void paintGameOver(Graphics g) {
		g.setColor(new Color(255, 255, 255, 150));
		g.fillRect(0, 130, 460, 680);
		g.setColor(new Color(230, 89 , 58));
		g.setFont(new Font("Impact", Font.BOLD, 70));
		g.drawString("Game Over", 39, 360);
	}
	/**������*/
	public void paintBG(Graphics g) {
		g.setColor(new Color(181, 170 ,156));
		//����ʾ��һ������ķ��񱳾�
		g.fillRect(193, 63, 59, 59);
		//����ǰ������
		g.fillRoundRect(30, 5, 110, 70, 11, 11);
		//����߷ֿ�
		g.fillRoundRect(310, 5, 110, 70, 11, 11);
		g.setColor(new Color(247, 247, 247));
		g.setFont(new Font("΢���ź�", Font.PLAIN, 17));
		g.drawString("��ǰ����", 49, 30);
		g.drawString("" + nowScore, 53, 57);
		g.drawString("��߷�", 339, 30);
		g.drawString("" + topScore, 339, 57);
		//����Ϸ��ı���
		g.setColor(new Color(181, 170 ,156));
		g.fillRect(41, 140, 361, 481);
		//��С����ı���
		g.setColor(new Color(206, 190, 181));
		for(int i = 0; i<8; i++) {
			for(int j = 0; j<6; j++) {
				g.fillRect(42+j*60, 141+i*60, Cell.SIZE, Cell.SIZE);
			}
		}
	}
	/**�������ֵ�С����*/
	private void paintCell(Graphics g) {
		//����һ�����µķ���
		g.setColor(nextCell.getColor());
		g.fillRect(nextCell.getX(), nextCell.getY(), nextCell.getWidth(), nextCell.getHeight());
		g.setColor(new Color(20, 20 ,20));
		g.setFont(new Font("����", Font.PLAIN, 23));
		g.drawString(""+nextCell.getNumber(), nextCell.getX()+getNumLen(nextCell.getNumber()),
				nextCell.getY()+nextCell.getHeight()*2/3);
		//����Ϸ���ķ���
		for(int i = 0; i < cells.length; i++) {
			for(int j = 0; j < cells[i].length; j++) {
				Cell cell = cells[i][j];
				int num = cell.getNumber();
				if(num != 0) {
					g.setColor(cell.getColor());
					g.fillRect(cell.getX(), cell.getY(), cell.getWidth(), cell.getHeight());
					g.setColor(new Color(10, 10, 10));
					g.setFont(new Font("����", Font.PLAIN, 23));
					g.drawString(""+cell.getNumber(), cell.getX()+getNumLen(num), 
							cell.getY()+cell.getHeight()*2/3);
				}
			}
		}
	}
	/**���־��еķ���*/
	private int getNumLen(int num) {
		int len = 59*2/5 ;
		if(num/1000 != 0) {
			len = 59/11-5;
		} else if(num/100 !=0) {
			len = 59/5;
		} else if(num/10 !=0) {
			len = 59/3-3;
		}
		return len;
	}
	/**��ť����*/
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == backButton) {
			setState(0);
		}else if(e.getSource() == pauseButton) {
			if(state == RUNNING) {
				state = PAUSE;
			}else if(state == PAUSE) {
				state = RUNNING;
				this.requestFocus();
			}
		}
	}
	/**
	 * ��ȡ�ļ��б������߷�
	 * �ж���߷ֺ͵�ǰ�����Ĵ�С������ǰ���������滻
	 */
	public void topScore() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("gamedata/topScore.txt"));
		String str = br.readLine();
		if(str != null) {
			topScore = Integer.parseInt(str);
			if(nowScore > topScore) {
				br.close();
				PrintWriter pw = new PrintWriter(new FileWriter("gamedata/topScore.txt"));
				topScore = nowScore;
				pw.write("" + nowScore);
				pw.close();
			}
		}
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
}