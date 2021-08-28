/** 
 * name: Yuqing Chang
 * student number: 1044862
 * username: yuqchang
 */
import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JTextArea;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JLabel;
import java.awt.Font;
import java.io.IOException;
import javax.swing.JScrollPane;
import java.awt.Color;

public class ServerGUI {

	private JFrame frame;
	public static JTextArea textArea;
	private static DictionaryServer server;
	private JScrollPane scrollPane;
	private static int port;
	private static String filePath; 							// The path of dictionary's JSON file
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		if(args.length != 2) {
			System.out.println("Please enter correct number of parameters.\n");
		}
		else {
		    try{
	            port = Integer.parseInt(args[0]);
	          }
	        catch (NumberFormatException ex){
	        	System.out.println("Please enter correct format of port.\n");
	            ex.printStackTrace();
	        }
		    filePath = args[1];
		}
		textArea= new JTextArea();
		textArea.setEditable(false);
		textArea.setRows(5);
		textArea.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerGUI window = new ServerGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		textArea.append("Waiting for client connection-");
		try {
			server = new DictionaryServer(port, filePath);
			server.runServer(server);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

	/**
	 * Create the application.
	 */
	public ServerGUI() {
		initialize();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setSize(700,500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {30, 30, 30, 30, 0, 30};
		gridBagLayout.rowHeights = new int[] {30, 30, 30, 30, 30};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JLabel lblNewLabel = new JLabel("You can see clients that are connected to server now:");
		lblNewLabel.setForeground(new Color(0, 0, 0));
		lblNewLabel.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 16));
		
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridheight = 2;
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.gridwidth = 4;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		frame.getContentPane().add(lblNewLabel, gbc_lblNewLabel);
		
		
        
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 2;
		//frame.getContentPane().add(scrollPane, gbc_scrollPane);
		
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		JScrollPane scrollableTextArea = new JScrollPane(textArea);  
		  
        scrollableTextArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);  
        scrollableTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);  
        frame.getContentPane().add(scrollableTextArea);  
        
        GridBagConstraints gbc_scrollableTextArea = new GridBagConstraints();
        gbc_scrollableTextArea.fill = GridBagConstraints.BOTH;
        gbc_scrollableTextArea.gridwidth = 5;
        gbc_scrollableTextArea.gridx = 0;
        gbc_scrollableTextArea.gridheight = 2;
        gbc_scrollableTextArea.gridy = 2;
        frame.getContentPane().add(scrollableTextArea, gbc_scrollableTextArea);  
		gbc_textArea.insets = new Insets(0, 0, 0, 5);
		gbc_textArea.gridheight = 2;
		gbc_textArea.gridwidth = 3;
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.gridx = 1;
		gbc_textArea.gridy = 2;
	}

}
