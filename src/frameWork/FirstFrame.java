package frameWork;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import app.Params;
import monitor.MonTask;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FirstFrame {

	private JFrame frame;
	private JTextField txtAbv;
	private File selectedFile;
	private JButton btnNewButton_1;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FirstFrame window = new FirstFrame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public FirstFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setFont(new Font("Dialog", Font.PLAIN, 25));
		frame.setBounds(100, 100, 450, 197);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnNewButton = new JButton("\u9009\u62E9\u626B\u63CF\u6587\u4EF6\u5939");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				
				System.out.println("mouse clicked");
				JFileChooser fc = new JFileChooser("C:");
				fc.setMultiSelectionEnabled(false);
				fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
//				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fc.setFileHidingEnabled(false);
				fc.setAcceptAllFileFilterUsed(false);
//				fc.setFileFilter(new MyFileFilter("xls"));
				int returnValue = fc.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					selectedFile = fc.getSelectedFile();
					System.out.println(selectedFile);
					txtAbv.setText(selectedFile.getPath());
				}
				
				
				
				
			}
		});
		
		btnNewButton.setBounds(-1, 10, 123, 33);
		frame.getContentPane().add(btnNewButton);
		
		txtAbv = new JTextField();
		txtAbv.setHorizontalAlignment(SwingConstants.LEFT);
		txtAbv.setBounds(132, 10, 292, 33);
		frame.getContentPane().add(txtAbv);
		txtAbv.setColumns(10);
		
		btnNewButton_1 = new JButton("\u542F\u52A8\u626B\u63CF");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				Params.interval = 5*100L;
				Params.scanFolderPath=txtAbv.getText();
				Params.uploadUrl="http://192.168.1.110:8080/WebPage/upload";
				Params.tempFile = "temp.txt";
				
				MonTask task = new MonTask();
				task.start();
			}
		});
		btnNewButton_1.setBounds(157, 126, 102, 23);
		frame.getContentPane().add(btnNewButton_1);
	}
}
