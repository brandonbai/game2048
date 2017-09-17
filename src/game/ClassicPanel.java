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
 */
public class ClassicPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private MyButton backButton,restartButton;
	Random random = new Random();
	//״̬
	private int state = 1;
	//���ذ�ť�����¿�ʼ��ť
	//����2��4���ɵ�����
	private int APPEAR = 0;
	private int appear = 0;
	//���ڴ�ŷ����ϵ����ֵ�����
	private int  [][] number ;
	//�����ж��Ƿ��ƶ���仯������
	private int [][] NUMBER;
	//���ڴ�ű�����ɫ������
	Color[][] colors;
	Cell cell ;
	/**��̨���Ŀ��*/
	public static final int PANEL_WIDTH = 424;
	public static final int PANEL_HEIGHT = 424;
	/**С����Ŀ��*/
	public static final int CELL_WIDTH = 100;
	public static final int CELL_HEIGHT = 100;
	//���ڿ��Ʒ��������
	public static final int UP = 1;
	public static final int DOWN =2;
	public static final int RIGHT =3;
	public static final int LEFT = 4;
	int dir = 0;
	//����������ӵ�����
	public static final int UP_ADD=5;
	public static final int DOWN_ADD=6;
	public static final int LEFT_ADD=7;
	public static final int RIGHT_ADD=8;
	int number_add = 0;
	//��ǰ����
	private int score = 0;
	//��߷�
	int mostScore;
	//number����ķ������ܺ�
	private int sums ;
	/**
	 * ���췽��
	 */
	public ClassicPanel() {
		
			Color backColor = new Color(255, 158, 90);
			
			backButton = new MyButton("����", backColor);
			backButton.setBounds(310, 100, 110, 30);
			backButton.addActionListener(this);
			
			restartButton = new MyButton("���¿�ʼ", backColor);
			restartButton.setBounds(180, 100, 110, 30);
			restartButton.addActionListener(this);
			this.add(backButton);
			this.add(restartButton);
			number = new int[4][4];
			NUMBER = new int[4][4];
		
	}
	/**
	 * ����������
	 */
	public void paint(Graphics g) {
		super.paint(g);
		paintScore(g);
		paintCellArea(g);
		if(state == 2){
			//����Ϸ����ʱ�Ļ���
			paintGameOver(g);
		}
	}

	/**���������ı���*/
	public void paintCellArea(Graphics g) {
		
			//���ñ����������ɫ
			g.setColor(new Color(227, 179, 155));
			//�����������
			g.fillRect(11, 138, PANEL_WIDTH, PANEL_HEIGHT);
			//���ñ���С������ɫ
			for(int i = 0; i < 4; i++) {
				for(int j = 0; j < 4; j++) {
					g.setColor(new Color(243,223,214));
					//��С���񱳾�
					if(number[i][j]!=0){
						cell= new Cell(number[i][j]);
						g.setColor(cell.getColor());
						g.fillRoundRect(15+i*105, 142+j*105, CELL_WIDTH, CELL_HEIGHT, 10, 10);
						String str  = "" + number[i][j];
						g.setColor(Color.BLACK);
						//���ִ�С
						g.setFont(new Font("������κ", Font.PLAIN, 30));
						int l = str.length();
						g.drawString(str, 15+i*105+(5-l)*10,142+j*105+60);
						
					}else{
						g.fillRoundRect(15+i*105, 142+j*105, CELL_WIDTH, CELL_HEIGHT, 10, 10);
					}
					
				}
			}
		
	}
	/**����������ʾ��Ϣ*/
	public void paintScore(Graphics g) {
		g.setColor(new Color(227, 179, 155));
		//����ǰ������
		g.fillRoundRect(180, 3, 110, 70, 11, 11);
		//����߷ֿ�
		g.fillRoundRect(310, 3, 110, 70, 11, 11);
		g.setColor(new Color(239, 198, 0));
		//��ͼ��
		g.fillRoundRect(25, 3, 130, 120, 12, 12);
		g.setColor(Color.white);
		g.setFont(new Font("΢���ź�",Font.PLAIN,20));
		g.drawString("��ǰ����", 195, 30);
		
		g.drawString(""+score, 200, 57);
		g.drawString("��߷�", 335, 30);
		g.drawString(""+mostScore, 335, 57);
		g.setFont(new Font("Impact",Font.PLAIN,48));
		g.drawString("2048", 40, 50);
		g.setFont(new Font("Impact",Font.BOLD,36));
		g.drawString("4 * 4", 50, 90);
	}
	//����Ϸ�����ķ���
	public void paintGameOver(Graphics g) {
		g.setColor(new Color(255, 255, 255, 150));
		g.fillRect(11, 138, PANEL_WIDTH, PANEL_HEIGHT);
		g.setColor(new Color(148, 117, 99));
		g.setFont(new Font("΢���ź�",Font.BOLD,59));
		g.drawString("Game Over !", 39, 320);
	}
	/**
	 * �����ķ���
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==backButton ) {
			try {
				this.scoreSave();
				this.numberWrite();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			setState(0);
		}else if(e.getSource()==restartButton ) {
			state = 1;
			//һ��Ϊ�յ�����
			number = new int[4][4];
			APPEAR=0;
			appear=0;
			sums  = 0;
			try {
				numberWrite();
				scoreSave();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			go();
			score=0;
			dir = 0;
			number_add =0;
		}
	}
	/**
	 * ������Ϸ��������
	 */
	public void go(){
		try {
			numberRead();
			scoreRead();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		this.requestFocus();
		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				APPEAR=0;
				if(e.getKeyCode()==37){
					dir= LEFT;
					number_add = LEFT_ADD;
				}
				if(e.getKeyCode()==38){
					dir = UP;
					number_add = UP_ADD;
				}
				if(e.getKeyCode()==39){
					dir = RIGHT;
					number_add = RIGHT_ADD;
				}
				if(e.getKeyCode()==40){
					dir = DOWN;
					number_add = DOWN_ADD;
				}
			}
		});
		Timer timer = new Timer();
		timer.schedule(new TimerTask(){
			public void run() {
				//����2����4
				appearAction();
				//��number��ֵ��NUMBER
				giveAction();
				//��һ��
				stepAction();
				//�ж��Ƿ����仯
				judgeAction();
				try {
					bigestScore();
				} catch (IOException e) {
					e.printStackTrace();
				}
				//�ж���Ϸ�Ƿ����
				gameOverAction();
				repaint();
			}
		}, 0, 20);
	}
	/**
	 * �������2��4�ķ���
	 */
	public void appearAction(){
		int i = random.nextInt(4);
		int j = random.nextInt(4);	
		int a = random.nextInt(3);
		//�ж��Ƿ���ص�
		if(APPEAR==0&&appear==0){
			if(this.isExit(i, j)==true){
				//���aΪ0������4,����������2
				if(a==0||a==1){
					number[i][j]=2;
				}else{
					number[i][j]=4;
					
				}
				APPEAR=1;
				appear=1;
			}
		}
		
	}
	/**
	 * �ж�С�����Ƿ����Ѿ��з���ĵط�����
	 */
	public boolean isExit(int i ,int j) {
		boolean IsExit = false;
		if(number[i][j]==0){
			IsExit = true;
		}
		return IsExit;
	}
	/**
	 * ��ֵ
	 */
	public void giveAction(){
		for(int i =0;i<4;i++){
			for(int j = 0;j<4;j++){
				NUMBER[i][j]=number[i][j];
			}
		}
	}
	/**
	 * �ж��Ƿ����仯
	 */
	public void judgeAction(){
		for(int i = 0 ;i<4;i++){
			for(int j = 0 ; j<4 ;j++){
				if(NUMBER[i][j]!=number[i][j]){
					appear=0;
				}
			}
		}
	}
	/**
	 * ���ƶ� �����  ���ƶ�
	 */
	//������һ��
	public void step(){
		/**�����ƶ�*/
		//��
		if(dir==UP){
			for(int i = 0;i<4;i++){
				for(int j =0;j<3;j++){
					if(number[i][j]==0){
						number[i][j]=number[i][j+1];
						number[i][j+1]=0;
					}
				}
			}
		}//��
		else if(dir == DOWN) {
			for(int i = 3;i>-1;i--){
				for(int j = 3;j>0;j--){
					if(number[i][j]==0){
						number[i][j]=number[i][j-1];
						number[i][j-1]=0;
					}
				}
			}
		}//��
		else if(dir ==LEFT){
			for(int i =0;i<3;i++){
				for(int j = 0;j<4;j++){
					if(number[i][j]==0){
						number[i][j]=number[i+1][j];
						number[i+1][j]=0;
					}
				}
			}
		}//��
		else if(dir == RIGHT) {
			for(int i = 3;i>0;i--){
				for(int j =3;j>-1;j--){
					if(number[i][j]==0){
						number[i][j]=number[i-1][j];
						number[i-1][j]=0;
					}
				}
			}
		}
	}
	//��������ӷ�װ��һ������
	public void numberAdd(){
		/**�������*/
		//����ʱ
		if(number_add==UP_ADD){
			for(int i = 0;i<4;i++){
				for(int j =0;j<3;j++){
					if(number[i][j]==number[i][j+1]){
						number[i][j]=2*number[i][j+1];
						number[i][j+1]=0;
						score += number[i][j];
					}
				}
			}
		}//������ʱ
		else if(number_add == DOWN_ADD) {
			for(int i = 3;i>-1;i--){
				for(int j = 3;j>0;j--){
					if(number[i][j]==number[i][j-1]){
						number[i][j] = 2*number[i][j-1];
						number[i][j-1]=0;
						score += number[i][j];
					}
				}
			}
		}
		//������ʱ
		else if(number_add ==LEFT_ADD){
			for(int i =0;i<3;i++){
				for(int j = 0;j<4;j++){
					if(number[i][j]==number[i+1][j]){
						number[i][j]=2*number[i+1][j];
						number[i+1][j]=0;
						score += number[i][j];
					}
				}
			}
		}
		//������ʱ
		else if(number_add == RIGHT_ADD) {
			for(int i = 3;i>0;i--){
				for(int j =3;j>-1;j--){
					if(number[i][j]==number[i-1][j]){
						number[i][j]=2*number[i-1][j];
						number[i-1][j]=0;
						score += number[i][j];
					}
				}
			}
		}
		number_add = 0;
	}
	//����һ���ƶ���װ��һ������
	public void firstMove(){
		step();
		step();
		step();
	}
	//���ڶ����ƶ���װ��һ������
	public void secondMove(){
		firstMove();
		dir = 0;
	}
	//����������������װ��һ������
	public int [][] stepAction(){
		try {
			Thread.sleep(70);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		/**���ƶ�*/
		firstMove();
		/**�����*/
		numberAdd();
		/**���ƶ�*/
		secondMove();
		return number;
	}
	
/**
 * ��ȡ�ļ��б������߷�
 * �ж���߷ֺ͵�ǰ�����Ĵ�С������ǰ���������滻
 */
	public void bigestScore() throws IOException{
		BufferedReader br = new BufferedReader(new FileReader("gamedata/mostScore.txt"));
		String str = br.readLine();
		if(str!=null){
			mostScore = Integer.parseInt(str);
			if(score>mostScore){
				br.close();
				PrintWriter pw = new PrintWriter(new FileWriter("gamedata/mostScore.txt"));
				mostScore = score;
				pw.write(""+score);
				pw.close();
			}
		}
	}
	/**
	 * ���浱ǰ����
	 * @throws IOException 
	 */
	public void scoreSave() throws IOException{
		PrintWriter pw = new PrintWriter(new FileWriter("gamedata/score.txt"));
		pw.write(score+"");
		pw.close();
	}
	/**
	 *��ȡ�ļ��еķ��� 
	 * @throws IOException 
	 */
	public void scoreRead() throws IOException{
		BufferedReader br = new BufferedReader(new FileReader("gamedata/score.txt"));
		String str = null;
		if((str = br.readLine()) != null ) {
			score = Integer.parseInt(str);
			br.close();
		}
	}
	/**
	 * ��numberд��Number.Txt�ļ���
	 * @throws IOException 
	 */
	public void numberWrite() throws IOException{
		PrintWriter pw = new PrintWriter(new FileWriter("gamedata/Number.txt"));
		StringBuffer sb = new StringBuffer();
		for(int i = 0;i<number.length;i++){
			for(int j = 0 ;j<number[i].length;j++){
				sb.append(number[i][j]).append(",");
			}
		}
		pw.write(sb.toString());
		pw.close();
	}
	/**
	 * ��Number.txt�е�ֵ��ȡ��������ֵ��number
	 */
	public void numberRead() throws IOException{
		BufferedReader bf = new BufferedReader(new FileReader("gamedata/Number.txt"));
		String str = null ;
		if((str = bf.readLine())!=null){
			String [] s = str.split(",");
			for(int j = 0 ;j<4;j++){
				int su0 = Integer.parseInt(s[j]);
				number[0][j] = su0 ; 
				int su1 = Integer.parseInt(s[j+4]);
				number[1][j] = su1;
				int su2 = Integer.parseInt(s[j+8]);
				number[2][j] = su2;
				int su3 = Integer.parseInt(s[j+12]);
				number[3][j] = su3;
			}
		}
		bf.close();
		//�ж��Ƿ�����2��4
		this.isAllZero();
	}
	/**
	 * ͨ���ж��Ƿ�number�ĸ�������Ϊ0�����ж��Ƿ���Ҫ����2��4
	 */
	public void isAllZero(){
		
		for(int i = 0 ;i<4;i++){
			for(int j = 0;j<4;j++){
				int sum = sums;
				sums = number[i][j] + sum;
			}
		}
		if(sums == 0 ){
			APPEAR = 0;
			appear = 0;
		}else {
			APPEAR = 1;
			appear = 1;
		}
	}
	/**
	 *�ж���Ϸ�Ƿ�����ķ��� 
	 */
	public void gameOverAction(){
		if(this.upAndDown() == true && this.leftAndRight() == true){
			state = 2;
		}
	}
	//�ж�����֮��֪�����
	public boolean upAndDown(){
		boolean isNotEqual = true; 
		for(int i = 0;i<number.length-1;i++){
			for(int j = 0;j<number[i].length;j++){
				if(number[i][j]==number[i+1][j] || number[i][j] == 0 || number[i+1][j]==0){
					isNotEqual = false;
				}
			}
		}
		return isNotEqual;
	}
	//�ж�����֮���Ƿ����
	public boolean leftAndRight(){
		boolean isNotEqual = true;
		for(int i =0;i<number.length;i++){
			for(int j =0;j<number[i].length-1;j++){
				if(number[i][j] == number[i][j+1] || number[i][j] == 0 || number[i][j+1]==0){
					isNotEqual = false;
				}
			}
		}
		return isNotEqual;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
}
