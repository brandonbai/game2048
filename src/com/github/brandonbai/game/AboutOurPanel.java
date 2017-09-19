package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * �������ǽ���������
 * 
 * @author Lv Wenyuan
 */
public class AboutOurPanel extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	//���ذ�ť
	private MyButton backButton;
	//������Ϸ״̬
	private int state = 1;
	//����ͼƬ
	private Image image;
	/**
	 * ���췽��
	 */
	public AboutOurPanel(){
		image = new ImageIcon(AboutOurPanel.class.getResource("JL.png")).getImage();
		setBackground(new Color(154,72,0));
		Color color = new Color(206, 170, 132);
		backButton = new MyButton("��    ��",color);
		backButton.setBounds(250, 550, 150, 40);
		backButton.addActionListener(this);
		this.add(backButton);
	}
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.YELLOW);
		g.setFont(new Font("΢���ź�",Font.BOLD,30));
		String xiaobai = "����ģʽ������� : ���ɻ� ";
		String lwy = "����ģʽ������� : ����Ԩ " ;
		g.drawString(xiaobai, 30, 150);
		g.drawString(lwy, 30, 200);
		g.drawImage(image, 153, 300, this);
	}
	public void actionPerformed(ActionEvent e) {
		state = 0;
	}
	
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public MyButton getBackButton() {
		return backButton;
	}
	public void setBackButton(MyButton backButton) {
		this.backButton = backButton;
	}
}
