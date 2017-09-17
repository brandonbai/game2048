package game;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * ������
 */
public class MainPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	//�������ϵ�ģʽ��ť
	private MyButton dropButton, classicButton;
	//����ģʽ���
	public ClassicPanel classicPanel;
	//����ģʽ���
	private DropPanel dropPanel;
	//2048��־
	private JLabel iconLabel;
	//����
	private static JFrame frame;
	//��Ϸ�����
	private static MainPanel mainPanel;
	private static final String DROP_STYLE = "2048/����ģʽ";
	private static final String CLASSIC_STYLE = "2048/����ģʽ";
	public static void main(String[] args) {
		//�½��������
		frame = new JFrame("2048");
		frame.setIconImage(new ImageIcon(MainPanel.class.getResource("gameicon.png")).getImage());
		//�����С
		frame.setSize(460, 680);
		//�������
		frame.setLocationRelativeTo(null);
		//�����С���ɱ�
		frame.setResizable(false);
		//Ĭ�ϵĹرղ���
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//�½������ ����
		mainPanel = new MainPanel();
		//ȥ�����Ĭ�ϲ���
		mainPanel.setLayout(new FlowLayout(1, 200, 90));
		//��屳����ɫ
		mainPanel.setBackground(new Color(247, 239, 230));
		//�������ӵ�������
		frame.add(mainPanel);
		//����ɼ�
		frame.setVisible(true);
		//������Ŀ��Ʒ���
		mainPanel.run();
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					mainPanel.classicPanel.scoreSave();
					mainPanel.classicPanel.numberWrite();
					mainPanel.dropPanel.topScore();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
	}
	/**
	 * ���췽��
	 */
	public MainPanel() {

		Color color = new Color(206, 170, 132);
		
		dropButton = new MyButton("�� �� ģ ʽ", color);
		classicButton = new MyButton("�� �� ģ ʽ", color);
		
		dropPanel = new DropPanel();
		classicPanel = new ClassicPanel();
		zujian();
	}
	/**�����*/
	public void zujian() {
		
		iconLabel = new JLabel("2048");
		iconLabel.setFont(new Font("Impact",Font.BOLD,108));
		iconLabel.setForeground(new Color(173, 113, 66));
		add(iconLabel);
		Font font = new Font("΢���ź�", Font.PLAIN, 27);
		dropButton.addActionListener(this);
		dropButton.setFont(font);
		dropPanel.setBounds(0, 0, 460, 680);
		add(dropButton);

		classicButton.addActionListener(this);
		classicButton.setFont(font);
		classicPanel.setBounds(0, 0, 460, 680);
		add(classicButton);
		
	}
	/**��Ϸ���м����Ʒ���*/
	public void run() {
		//����������
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				back();
			}
		}, 0, 20);
	}
	/**����������ķ���*/
	public void back() {
		if(classicPanel.getState() == 0 || dropPanel.getState() == 0) {
			frame.setTitle("2048");
			removeAll();
			add(classicButton);
			add(dropButton);
			add(iconLabel);
			classicPanel.setState(1);
			if(dropPanel.getState() == 0)
				dropPanel.setState(2);
			repaint();
		}
	}
	/**��ť����*/
	public void actionPerformed(ActionEvent e) {
		removeAll();
		if(e.getSource() == classicButton) {
			frame.setTitle(CLASSIC_STYLE);
			add(classicPanel);
			if(classicPanel.getState() == 1) {
				classicPanel.go();
			}
		}else if(e.getSource() == dropButton) {
			frame.setTitle(DROP_STYLE);
			add(dropPanel);
			dropPanel.requestFocus();
			if(dropPanel.getState() == 1) {
				dropPanel.run();
			}
		}
		repaint();
	}

}
