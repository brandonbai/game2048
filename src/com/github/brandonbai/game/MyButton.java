package game;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.border.TitledBorder;

/**
 * �Զ��尴ť��
 */
public class MyButton extends JButton {
	private static final long serialVersionUID = 1L;
	
	/**
	 * ���췽��
	 */
	public MyButton(String text, Color backColor) {

		super(text);
		setForeground(Color.white);
		setBackground(backColor);
		setBorder(new TitledBorder(""));
		setFont(new Font("΢���ź�", Font.PLAIN, 20));
	}
}
