package textToImage;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import crypter.Crypter;
import crypter.Decrypter;

import javax.swing.JFormattedTextField;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.ScrollPane;
import java.awt.FlowLayout;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;


import javax.swing.JCheckBox;
import javax.swing.JTextField;

public class Frame1 {
	//TODO: rendre le textfield modifiable pour mettre lien fichier en dur si on veut
	private JFrame frame;
	private Panel panel_1;
	private JFormattedTextField jftPathToFile;
	private TextArea textArea;
	private JButton btnCrypt;
	private JButton btnDecrypt;
	private String textToEncrypt;
	private boolean isImageSelected = false;
	private Image image;
	private JCheckBox chckbxNewCheckBox;
	private JTextField jtfEnterKey;
	private String fileName="";

	/**
	 * Launch the application.
	 */
	

	/*
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame1 window = new Frame1();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the application.
	 */
	public Frame1() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Panel panel = new Panel();
		panel.setPreferredSize(new Dimension(10, 50));
		panel.setBackground(Color.BLUE);
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		FlowLayout fl_panel = new FlowLayout(FlowLayout.CENTER, 5, 5);
		panel.setLayout(fl_panel);
		
		jftPathToFile = new JFormattedTextField();
		jftPathToFile.setEditable(false);
		jftPathToFile.setText("C:\\\\path_to_file");
		panel.add(jftPathToFile);
		
		Button button = new Button("Choose Image");
		panel.add(button);
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
		        int returnValue = fileChooser.showOpenDialog(null);
		        if (returnValue == JFileChooser.APPROVE_OPTION) {
		          File selectedFile = fileChooser.getSelectedFile();
		          //setTextLabel_1(selectedFile.getAbsolutePath	());
		          fileName=selectedFile.getName();
		          System.out.println("fileName: "+fileName);
		          //setFilePath(selectedFile.getAbsolutePath());
		          jftPathToFile.setText(selectedFile.getAbsolutePath());
		          //File f = new File(jftPathToFile.getText());
		          System.out.println(jftPathToFile.getText());
					
		          boolean valid;
		          try {
		        	  Image image = ImageIO.read(selectedFile);
					   if (image == null) {
					        valid = false;
					        isImageSelected=false;
					        String labelErrorText="The file "+selectedFile.getAbsolutePath()+"could not be opened , it is not an image";
					        System.out.println("DEBUG : "+labelErrorText);
					        JOptionPane.showMessageDialog(null, labelErrorText, "Invalid File", JOptionPane.ERROR_MESSAGE);
					        //labelError.setText(labelErrorText);
					   }else {
					    	//c'est une image le boolean a true permet dautoriser le cryptage
						    valid = true;
					        isImageSelected=true;
					    	System.out.println("DEBUG : image ok");
					    	//labelError.setText("");
					    	setFilePath(selectedFile.getAbsolutePath());
					    	setImage(image);
					    }
					} catch(IOException ex) {
					    valid = false;
					    isImageSelected=false;
					    String labelErrorText = "The file"+selectedFile.getAbsolutePath()+"could not be opened , an error occurred.";
					    System.out.println("DEBUG : "+labelErrorText);
					    JOptionPane.showMessageDialog(null, labelErrorText, "Invalid File", JOptionPane.ERROR_MESSAGE);
					    //labelError.setText(labelErrorText);
					}
					
		          System.out.println("File path setetd!!! : "+selectedFile.getAbsolutePath());
		          //System.out.println(selectedFile.getAbsolutePath());
		        }
			}
		});
		button.setBackground(Color.LIGHT_GRAY);
		
		chckbxNewCheckBox = new JCheckBox("key");
		chckbxNewCheckBox.setSelected(true);
		chckbxNewCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(jtfEnterKey.isVisible() == true) {
					jtfEnterKey.setVisible(false);
				}else {
					jtfEnterKey.setVisible(true);
				}
			}
		});
		panel.add(chckbxNewCheckBox);
		
		jtfEnterKey = new JTextField();
		jtfEnterKey.setText("enter key");
		jtfEnterKey.setToolTipText("enter key");
		panel.add(jtfEnterKey);
		jtfEnterKey.setColumns(10);
		
		textArea = new TextArea();
		frame.getContentPane().add(textArea, BorderLayout.CENTER);
		
		panel_1 = new Panel();
		frame.getContentPane().add(panel_1, BorderLayout.SOUTH);
		
		btnCrypt = new JButton("Crypt!");
		btnCrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//setTextToEncrypt(textArea.getText());
				//System.out.println("Test to encrypt: "+textArea.getText());
				//si une image est selectionnee on cree un crypter
				if(isImageSelected==true) {
					//labelError.setText("");
					//Decrypter decrypter = new Decrypter(image, "");
					if((chckbxNewCheckBox.isSelected()) && Crypter.isValidKey(jtfEnterKey.getText())) {
						Crypter crypter = new Crypter(image, textArea.getText(), jtfEnterKey.getText(), fileName );
					}else if((chckbxNewCheckBox.isSelected()) && Crypter.isValidKey(jtfEnterKey.getText())==false ){
						//TODO : message window alert: non valid key (32 bytes key needed)
						String invalidKey = "Invalid key: must be a 32 bytes key!";
						System.out.println(invalidKey);
						JOptionPane.showMessageDialog(null, invalidKey, "Invalid Key", JOptionPane.ERROR_MESSAGE);
					}else {
						Crypter crypter = new Crypter(image, textArea.getText(), "", fileName);
					}
				}else {
					String invalidPic = "Invalid picture: please select a valid picture!";
					JOptionPane.showMessageDialog(null, invalidPic, "Invalid Picture", JOptionPane.ERROR_MESSAGE);
					//labelError.setText("Please select a picture!");
				}
			}
		});
		panel_1.add(btnCrypt);
		
		btnDecrypt = new JButton("Decrypt!");
		btnDecrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//setTextToEncrypt(textArea.getText());
				//System.out.println("Test to encrypt: "+textArea.getText());
				//si une image est selectionnee on cree un crypter
				if(isImageSelected==true) {
					//labelError.setText("");
					Decrypter decrypter = new Decrypter(image, "");
					if((chckbxNewCheckBox.isSelected()) && Decrypter.isValidKey(jtfEnterKey.getText())) {
						decrypter = new Decrypter(image, jtfEnterKey.getText());
						textArea.setText(decrypter.getTextDecrypted());
					}else if((chckbxNewCheckBox.isSelected()) && Decrypter.isValidKey(jtfEnterKey.getText())==false ){
						//TODO : message window alert: non valid key (32 bytes key needed)
						String invalidKey = "Invalid key: must be a 32 bytes key!";
						System.out.println(invalidKey);
						JOptionPane.showMessageDialog(null, invalidKey, "Invalid Key", JOptionPane.ERROR_MESSAGE);
						//System.out.println("invalid keyyyy!");
					}else {
						textArea.setText(decrypter.getTextDecrypted());
					}
					//textArea.append(decrypter.getTextDecrypted());
					//decrypter.getTextDecrypted();
					//System.out.println("decrypted str: ");
					//String s = decrypter.getBase64FromBinary("0101000001101100011000110101010001110100011001000011000101001001011000100011000000111000011010110010101101011001010110010101001101111001011100000110101001110001011011100111100100110011011100100100010101001000001100010100100001100110011011110011010100110111011011110110111001001111011100110101001001100011010110000011001101001000001101000110011100111101");
					//System.out.println(decrypter.getTextFromBase64(s));
				}else {
					String invalidPic = "Invalid picture: please select a valid picture!";
					JOptionPane.showMessageDialog(null, invalidPic, "Invalid File", JOptionPane.ERROR_MESSAGE);
					//labelError.setText(invalidPic);
					System.out.println(invalidPic);
					JOptionPane.showMessageDialog(null, invalidPic, "Invalid Picture", JOptionPane.ERROR_MESSAGE);

				}
			}
		});
		panel_1.add(btnDecrypt);
		
	}
	
	public JFrame getFrame() {
		return frame;
	}
	public Panel getPanel_1() {
		return panel_1;
	}

	public void setPanel_1(Panel panel_1) {
		this.panel_1 = panel_1;
	}

	
	public String getFilePath() {
		return jftPathToFile.getText();
	}

	public void setFilePath(String _text) {
		this.jftPathToFile.setText(_text);
	}
	
	public void setImage(Image _image) {
		image = _image;
	}
	
	public Image getImage() {
		return image;
	}
}
