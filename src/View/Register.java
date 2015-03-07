package View;

public class Register extends JFrame{

	private JPanel contentPane;
	public static JTextField textName;
	private JTextField textEmail;
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;
	static Register frame = new Register();
	
	
/* Creating Frame */
	
	public void CloseJframe(){
        super.dispose();
        public Register() {
    		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    		setBounds(100, 100, 326, 300);
    		contentPane = new JPanel();
    		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    		setContentPane(contentPane);
    		contentPane.setLayout(null);
    		
    		JLabel lblName = new JLabel("Name");
    		lblName.setBounds(6, 21, 61, 16);
    		contentPane.add(lblName);
    		
    		JLabel lblEmail = new JLabel("Email:");
    		lblEmail.setBounds(6, 61, 61, 16);
    		contentPane.add(lblEmail);
    		
    		JLabel lblPassword = new JLabel("Password:");
    		lblPassword.setBounds(6, 112, 91, 16);
    		contentPane.add(lblPassword);
    		
    		textName = new JTextField();
    		textName.setBounds(98, 12, 200, 34);
    		contentPane.add(textName);
    		textName.setColumns(10);
    		
    		textEmail = new JTextField();
    		textEmail.setBounds(98, 52, 200, 34);
    		contentPane.add(textEmail);
    		textEmail.setColumns(10);
    		
    		passwordField = new JPasswordField();
    		passwordField.setBounds(98, 94, 200, 34);
    		contentPane.add(passwordField);
    	
    		JButton btnCreateUser = new JButton("Create User");
    		btnCreateUser.addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent arg0) {
    				PasswordChecker check= new PasswordChecker();
    				if(check.isValid(passwordField.getText()))
    				{
    				DBWrapper db= new DBWrapper();
    				db.saveUser(textName.getText(), textEmail.getText(), passwordField.getText());
    				CloseJframe();
    				}
    				else
    					JOptionPane.showMessageDialog(null, "The Password must have at least eight characters of only letters and digits,must contain at least two digits");
    				}
    		});
    		btnCreateUser.setBounds(115, 206, 117, 29);
    		contentPane.add(btnCreateUser);
    		
    		
    		JLabel lblRepassword = new JLabel("Re-Password");
    		lblRepassword.setBounds(6, 155, 82, 16);
    		contentPane.add(lblRepassword);
    		
    		passwordField_1 = new JPasswordField();
    		passwordField_1.setBounds(98, 140, 200, 34);
    		contentPane.add(passwordField_1);
    		
        }
	}
