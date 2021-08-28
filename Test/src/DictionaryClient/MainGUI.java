package DictionaryClient;
/** 
 * name: Yuqing Chang
 * student number: 1044862
 * username: yuqchang
 */
import java.awt.EventQueue;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;
import javax.swing.JTextField;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.ImageIcon;
import java.awt.Font;
import javax.swing.JScrollBar;
import javax.swing.SwingConstants;
import java.awt.SystemColor;
import java.awt.Color;

/**
 * Constructor of MainGUI class
 */
public class MainGUI {

	private JFrame frame;
	private JTextField textField;
	private JTextArea textArea;
	private static DicClient DictClient;									// Declare a instance of DicClient
	// IP and port
	private static String ip;
	private static int port;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		if(args.length != 2) {
			System.out.println("Please enter correct number of parameters.\n");
		}
		else {
			ip = args[0];
		    try{
	            port = Integer.parseInt(args[1]);
	          }
	        catch (NumberFormatException ex){
	        	System.out.println("Please enter correct format of port.\n");
	            ex.printStackTrace();
	        }
		    
		}
		// Create a instance of DicClient
		DictClient = new DicClient(port, ip);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGUI window = new MainGUI();
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
	public MainGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 617, 496);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {30, 30, 0, 0, 0, 30, 30, 30, 30, 30, 15, 30, 30, 30, 30, 0, 30, 30, 15, 0, 30, 30, 30};
		gridBagLayout.rowHeights = new int[] {30, 30, 30, 0, 30, 30, 15, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JTextArea txtrdictionaryInstructionspleaseEnter = new JTextArea();
		txtrdictionaryInstructionspleaseEnter.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		txtrdictionaryInstructionspleaseEnter.setEditable(false);
		txtrdictionaryInstructionspleaseEnter.setBackground(SystemColor.activeCaption);
		txtrdictionaryInstructionspleaseEnter.setText("Dictionary instructions:\r\nPlease enter word in smaller input field, enter meanings \r\nin larger field.\r\nYou can see operation results in larger text field.");
		GridBagConstraints gbc_txtrdictionaryInstructionspleaseEnter = new GridBagConstraints();
		gbc_txtrdictionaryInstructionspleaseEnter.gridwidth = 15;
		gbc_txtrdictionaryInstructionspleaseEnter.insets = new Insets(0, 0, 5, 5);
		gbc_txtrdictionaryInstructionspleaseEnter.fill = GridBagConstraints.BOTH;
		gbc_txtrdictionaryInstructionspleaseEnter.gridx = 2;
		gbc_txtrdictionaryInstructionspleaseEnter.gridy = 0;
		frame.getContentPane().add(txtrdictionaryInstructionspleaseEnter, gbc_txtrdictionaryInstructionspleaseEnter);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBackground(SystemColor.activeCaption);
		lblNewLabel.setForeground(SystemColor.activeCaption);
		lblNewLabel.setIcon(new ImageIcon("C:\\Users\\zys91\\Desktop\\dictionary.jfif"));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridwidth = 5;
		gbc_lblNewLabel.fill = GridBagConstraints.BOTH;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 17;
		gbc_lblNewLabel.gridy = 0;
		frame.getContentPane().add(lblNewLabel, gbc_lblNewLabel);
		
		JButton btnQuery = new JButton("Query");
		btnQuery.setBackground(new Color(216, 191, 216));
		btnQuery.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 13));
		btnQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText(null);
				/*
				 * Send instruction and parameters to server
				 */
				String content = textField.getText();
				String sendData = "Query " + content;
				String acc = "";
				String recieve = DictClient.send(sendData);
				
				/*
				 * Output all meanings and each of it is on a separate line 
				 */
				StringTokenizer st = new StringTokenizer(recieve, "!");
				while(st.hasMoreTokens()) {
					acc = acc + st.nextToken() + "\n" ;
                 }
				textArea.append(acc);
			}
		});
		GridBagConstraints gbc_btnQuery = new GridBagConstraints();
		gbc_btnQuery.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnQuery.gridwidth = 4;
		gbc_btnQuery.insets = new Insets(0, 0, 5, 5);
		gbc_btnQuery.gridx = 2;
		gbc_btnQuery.gridy = 1;
		frame.getContentPane().add(btnQuery, gbc_btnQuery);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.setBackground(new Color(216, 191, 216));
		btnAdd.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 13));
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * Send instruction and parameters to server
				 */
				String content = textArea.getText();
				String c = content.replace("\n", "");
				String sendData = "Add " + textField.getText() + " " + c;
				String recieve = DictClient.send(sendData);
				textArea.setText(null);
				textArea.append(recieve);
			}
		});
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnAdd.gridwidth = 3;
		gbc_btnAdd.insets = new Insets(0, 0, 5, 5);
		gbc_btnAdd.gridx = 9;
		gbc_btnAdd.gridy = 1;
		frame.getContentPane().add(btnAdd, gbc_btnAdd);
		
		JButton btnRemove = new JButton("Remove");
		btnRemove.setBackground(new Color(216, 191, 216));
		btnRemove.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 13));
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText(null);
				/*
				 * Send instruction and parameters to server
				 */
				String sendData = "Remove " + textField.getText();
				
				String recieve = DictClient.send(sendData);
				textArea.setText(null);
				textArea.append(recieve);
			}
		});
		GridBagConstraints gbc_btnRemove = new GridBagConstraints();
		gbc_btnRemove.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnRemove.gridwidth = 2;
		gbc_btnRemove.insets = new Insets(0, 0, 5, 5);
		gbc_btnRemove.gridx = 14;
		gbc_btnRemove.gridy = 1;
		frame.getContentPane().add(btnRemove, gbc_btnRemove);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.setBackground(new Color(216, 191, 216));
		btnUpdate.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 13));
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * Send instruction and parameters to server
				 */
				String content = textArea.getText();
				String c = content.replace("\n", "");
				String sendData = "Update " + textField.getText() + " " + c;
				String recieve = DictClient.send(sendData);
				textArea.setText(null);
				textArea.append(recieve);
			}
		});
		GridBagConstraints gbc_btnUpdate = new GridBagConstraints();
		gbc_btnUpdate.fill = GridBagConstraints.VERTICAL;
		gbc_btnUpdate.insets = new Insets(0, 0, 5, 5);
		gbc_btnUpdate.gridx = 19;
		gbc_btnUpdate.gridy = 1;
		frame.getContentPane().add(btnUpdate, gbc_btnUpdate);
		
		textField = new JTextField();
		textField.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 18));
		textField.setToolTipText("Please enter the word");
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.gridwidth = 17;
		gbc_textField.gridheight = 2;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.BOTH;
		gbc_textField.gridx = 3;
		gbc_textField.gridy = 2;
		frame.getContentPane().add(textField, gbc_textField);
		textField.setColumns(10);
		
		textArea = new JTextArea();
		textArea.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 18));
		textArea.setToolTipText("Enter meanings & OutPut");
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.insets = new Insets(0, 0, 5, 5);
		gbc_textArea.gridwidth = 17;
		gbc_textArea.gridheight = 7;
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.gridx = 3;
		gbc_textArea.gridy = 5;
		frame.getContentPane().add(textArea, gbc_textArea);
		
		JButton btnExit = new JButton("Exit");
		btnExit.setBackground(new Color(176, 196, 222));
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sendData = "Exit ";
				DictClient.send(sendData);
				System.exit(0);
			}
		});
		btnExit.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 13));
		GridBagConstraints gbc_btnExit = new GridBagConstraints();
		gbc_btnExit.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnExit.insets = new Insets(0, 0, 5, 5);
		gbc_btnExit.gridx = 19;
		gbc_btnExit.gridy = 12;
		frame.getContentPane().add(btnExit, gbc_btnExit);
	}
}


