package Game.Start;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StartFrame extends JFrame{
	//定义世界高度和宽度
	public static final int WIDTH = 400;
	public static final int HEIGHT = 350;
	private boolean flag;

	//一个玩家时的监听器
	class SinglePlayerListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			new GameClient(false).lauchFrame();
			setVisible(false);
		}
	}

	//两个玩家时候的监听器
	class DoublePlayerListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			new GameClient(true).lauchFrame();
			setVisible(false);
		}
	}


	class WindowDestroyer extends WindowAdapter{
		public void windowClosing(WindowEvent e){

		}
	}


	public StartFrame(){
		//设置窗口
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		addWindowListener(new WindowDestroyer());
		setTitle("Zombie Crsis");
		Container contentPane = getContentPane();
		contentPane.setBackground(Color.LIGHT_GRAY);

		//按钮
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.WHITE);

		contentPane.setLayout(new FlowLayout());
		//将图片添加到按钮的方法
		ImageIcon singleIcon = new ImageIcon(StartFrame.class.getResource("/images/single.jpg"));
		//设置图片位置
		singleIcon.setImage(singleIcon.getImage().getScaledInstance(380,140,Image.SCALE_DEFAULT));
		JButton singleButton = new JButton(singleIcon);
		singleButton.setIcon(singleIcon);
		singleButton.addActionListener(new SinglePlayerListener());
		//将功能添加到面板上
		contentPane.add(singleButton);

		//同理
		ImageIcon doubleIcon = new ImageIcon(StartFrame.class.getResource("/images/double.jpg"));
		doubleIcon.setImage(doubleIcon.getImage().getScaledInstance(380,140,Image.SCALE_DEFAULT));
		JButton doubleButton = new JButton(doubleIcon);
		doubleButton.setIcon(doubleIcon);
		doubleButton.addActionListener(new DoublePlayerListener());
		contentPane.add(doubleButton);
		//设置可见
		setVisible(true);
	}
}
