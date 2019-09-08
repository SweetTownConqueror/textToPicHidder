package crypter;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Decrypter extends Cryptology{
	
	private String textToDecrypt = "";
	private String textDecrypted = "";
	private String key = "";
	private byte [] iv;
	
	public Decrypter(Image _image, String personnalKey) {
		//TODO on va recuperer les 1es 16 derniers bits des composantes RGB des 8 premiers pixels afin de creer un vecteur d'innitialisation de 128 bits (16bytes)
		iv= getInitializationVector(_image);
		//petit test de crypto avec le IV genere
		if(personnalKey.isEmpty()) {
			key = super.getKey();
		}else {
			key = personnalKey;
		}
		
		//System.out.println("Decrypter : ");
		//textToDecrypt = getBase64FromBinary(getTextToDecrypt(_image));//getTextFromBase64(getBase64FromBinary(getTextToDecrypt(_image)));
		textToDecrypt = getTextToDecrypt(_image, getMessageSizeToDecrypt(_image));
		System.out.println("Text to decrypt (2): "+textToDecrypt);
		textToDecrypt = getBase64FromBinary(textToDecrypt);
		System.out.println("Text to decrypt (b64): "+textToDecrypt);
		System.out.println("Text to decrypt size : "+getMessageSizeToDecrypt(_image));
		/*String s="";
		for(int i=0;i<24;i++) {
			s = s+textToDecrypt.charAt(i);
		}*/
		System.out.println("Text to decrypt : "+textToDecrypt);
		textDecrypted = getTextFromBase64(textToDecrypt);
		System.out.println("Text decrypted : "+textDecrypted);
		
		//textDecrypted = AESCrypter.decrypt(key, initVector, encrypted);
		//TODO on va mettre le texte encrypté sous forme binaire et injecter ces valeurs dans
		//les derniers bits des RGB à partir du 9 eme pixel
		//cryptMessageToImage(_image, textEncrypted);
		
		//System.out.println(AESCrypter.decrypt(key, iv,AESCrypter.encrypt(key, iv, _textToEncrypt)));
		
		// a.length = hauteur image
		//System.out.println("length: "+a.length);
		/*
		 for(int i=0; i<6 ; i++){
			System.out.println(a[i][0]);
			System.out.println("----------------");
		}*/
		
		//textEncrypted = AESCrypter.encrypt(key, initVector, value);
	}

	public String getTextToDecrypt(Image _image, int _bitnumber) {
		// TODO Auto-generated method stubBufferedImage image;
		//int[][] result;
		BufferedImage image;
		int bitNumber = 0;
		String binaryString = "";
		if (_image instanceof BufferedImage)
	    {
	        image = (BufferedImage) _image;
	        int width = image.getWidth();
		    int height = image.getHeight();
		    //result = new int[height][width];
		    
		    
		    outer: for (int row = 1; row < height; row++) {
		    	for (int col = 0; col < width; col++) {
		    		if(bitNumber > (_bitnumber-1)/3) {
		    			break outer;
		    		}
		    		if(bitNumber > (width*height)-width-1 ) {
		    			//TODO: message trop volumineux pour l'image
		    			//TODO: afficher le nombre de characteres restants
		    			//TODO si nombre de caractere restant atteint:
		    			//	on ne peux plus editer le text area
		    		}
		    		//result[row][col] = image.getRGB(col, row);
		    		Color mycolor = new Color(image.getRGB(col, row));
		    		//int red = mycolor.getRed();
		    		//int green = mycolor.getGreen();
		    		//int blue = mycolor.getBlue();
		    		//byte green = (byte) mycolor.getGreen();
		    		String redBinaryString = String.format("%8s", Integer.toBinaryString(mycolor.getRed())).replace(' ', '0');
		    		String greenBinaryString = String.format("%8s", Integer.toBinaryString(mycolor.getGreen())).replace(' ', '0');
		    		String blueBinaryString = String.format("%8s", Integer.toBinaryString(mycolor.getBlue())).replace(' ', '0');
		    		
		    		binaryString = binaryString + 
		    				redBinaryString.charAt(redBinaryString.length()-1)+
		    				greenBinaryString.charAt(greenBinaryString.length()-1)+
		    				blueBinaryString.charAt(blueBinaryString.length()-1) ;
		    		bitNumber++;
		    	}
		    		
		    }
		    	//return null;
	    }
			System.out.println(binaryString);
			/*while(binaryString.length()%16 != 0) {
		    	binaryString+="0";
				//binaryString.concat("0");
		    }*/
			/*String decodedString = "";
			String bytes = "";
			for(int i = 0; i<binaryString.length()-1 ; i++) {
				bytes=bytes+binaryString.charAt(i);
				if(i%8 == 0) {
					int charCode = Integer.parseInt(bytes, 2);
					decodedString = decodedString + new Character((char)charCode).toString();
				    bytes="";
				}
			}*/
		    
		    return binaryString;

	}
	public String getBase64FromBinary(String _binary) {
		String txt="";
		String str="";
		//ArrayList<String> byteArray = new ArrayList();
		//System.out.println(_binary.length());
		for(int i=0; i< _binary.length(); i++) {
			txt=txt+_binary.charAt(i);
			if(txt.length() == 8) {
				//byteArray.add(txt);
				//txt = "";
				int charCode = Integer.parseInt(txt, 2);
				str = str+new Character((char)charCode).toString();
				txt = "";
			}
		}
		return str;
	}
	
	public String getTextFromBase64(String _text){
		return AESCrypter.decrypt(key, iv, _text);
	}
	public int getMessageSizeToDecrypt(Image _image) {
		BufferedImage image;
		int messageSize=0;
		if (_image instanceof BufferedImage)
	    {
	        image = (BufferedImage) _image;
	        int width = image.getWidth();
		    int height = image.getHeight();
		    
		    Color mycolor = new Color(image.getRGB(width-1, height-1));

    		String redBinaryString = String.format("%8s", Integer.toBinaryString(mycolor.getRed())).replace(' ', '0');
    		String greenBinaryString = String.format("%8s", Integer.toBinaryString(mycolor.getGreen())).replace(' ', '0');
    		String blueBinaryString = String.format("%8s", Integer.toBinaryString(mycolor.getBlue())).replace(' ', '0');
    		
    		String binaryMessageSize="";
    		for(int i=redBinaryString.length()-5;i<redBinaryString.length();i++) {
    			binaryMessageSize=binaryMessageSize.concat(""+redBinaryString.charAt(i));
    		}
    		for(int i=greenBinaryString.length()-5;i<greenBinaryString.length();i++) {
    			binaryMessageSize=binaryMessageSize.concat(""+greenBinaryString.charAt(i));
    		}
			for(int i=blueBinaryString.length()-5;i<blueBinaryString.length();i++) {
				binaryMessageSize=binaryMessageSize.concat(""+blueBinaryString.charAt(i));
			}
			System.out.println("Decrypter -> binarymessagesize:"+binaryMessageSize);
		    messageSize = Integer.parseInt(binaryMessageSize, 2);
	    }
		return messageSize;
	}

	public String getTextDecrypted() {
		return textDecrypted;
	}

	public void setTextDecrypted(String textDecrypted) {
		this.textDecrypted = textDecrypted;
	}
	
	
}
