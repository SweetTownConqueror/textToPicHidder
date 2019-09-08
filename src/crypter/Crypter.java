package crypter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.*;


public class Crypter extends Cryptology{
	
	private String textEncrypted = "";
	private String key = "";
	
	public Crypter(Image _image, String _textToEncrypt, String personnalKey, String _fileName) {
		if(personnalKey.isEmpty()) {
			key = super.getKey();
		}else {
			key = personnalKey;
		}
		//TODO on va recuperer les 1es 16 derniers bits des composantes RGB des 8 premiers pixels afin de creer un vecteur d'innitialisation de 128 bits (16bytes)
		byte [] iv= getInitializationVector(_image);
		//petit test de crypto avec le IV genere
		//key = super.getKey();
		textEncrypted = AESCrypter.encrypt(key, iv, _textToEncrypt);
		//TODO on va mettre le texte encrypté sous forme binaire et injecter ces valeurs dans
		//les derniers bits des RGB à partir du 9 eme pixel
		System.out.println("text to encrypt : " +_textToEncrypt);
		System.out.println("text encrypted : " + textEncrypted);
		int cryptedMessageSizeInBits = cryptMessageToImage(_image, textEncrypted, _fileName);
		System.out.println("Crypter -> text to decrypt size : "+cryptedMessageSizeInBits);
		cryptMessageSizeToImage(_image, cryptedMessageSizeInBits, _fileName);
		
	}
	
	public static int cryptMessageToImage(Image _image, String _message, String _fileName){
		// il faut que la taille de liimage puisse faire passer le message
		// on convertit le message en binaire
		// pour chaque pixel on stocke dans le dernier bit de chaque composante RGB
		// un bit du message
		// dans les 2 derniers octets de limage on met la taille du message afin de recuperer
		//uniquement linformation que lon souhaite
		
		byte[] _messagesBytes = null;
		_messagesBytes = _message.getBytes();
		String _messageBinary = "";
		for (byte b : _messagesBytes) {
			//Integer.toBinaryString(b);
            //System.out.println("c:" + (char) b + "-> "
              //      + Integer.toBinaryString(b));
            String binaryString = String.format("%8s", Integer.toBinaryString(b)).replace(' ', '0');//Integer.toBinaryString(b);
            for(int i=0; i < binaryString.length(); i++){
            	//System.out.println(binaryString.charAt(i));
            	_messageBinary = _messageBinary + binaryString.charAt(i);
            	
            }
        }
		System.out.println("message binary : ");
		System.out.println(_messageBinary);
		
		/*Decrypter d=new Decrypter(_image);
		System.out.println("test decryptage : "+d.getBase64FromBinary(_messageBinary));
		System.out.println("test decryptage : "+d.getTextFromBase64(d.getBase64FromBinary(_messageBinary)));
		*/
		
		// on parcourt les donnes binaires du message et
		// on modifie les pixels a partir du 9 eme pixel :
		BufferedImage image;
		//int[][] result;
		int bitNumber = 0;
		if (_image instanceof BufferedImage)
	    {
	        image = (BufferedImage) _image;
	        int width = image.getWidth();
		    int height = image.getHeight();
		    //result = new int[height][width];
		    outer: for (int row = 1; row < height; row++) {
		    	for (int col = 0; col < width; col++) {
		    		//result[row][col] = image.getRGB(col, row);
		    		Color mycolor = new Color(image.getRGB(col, row));
		    		
		    		String binaryRedString = String.format("%8s", Integer.toBinaryString(mycolor.getRed())).trim();
		    		String binaryGreenString = String.format("%8s", Integer.toBinaryString(mycolor.getGreen())).trim();
		    		String binaryBlueString = String.format("%8s", Integer.toBinaryString(mycolor.getBlue())).trim();
		    		int rgb = 0;
		    		int red=0;
		    		int green=0;
		    		int blue=0;
		    		if(_messageBinary.length() > bitNumber) {
		    			//System.out.println(bitNumber);
		    			//System.out.println(_messageBinary.charAt(bitNumber));
		    			if(_messageBinary.charAt(bitNumber) == '1') {
		    				binaryRedString = binaryRedString.substring(0,binaryRedString.length()-1)+'1';
		    				//binaryGreenString = binaryGreenString.substring(0,binaryGreenString.length()-1)+'1';
		    				//binaryBlueString = binaryBlueString.substring(0,binaryBlueString.length()-1)+'1';
		    				//System.out.println("1 red");
		    			}else {
		    				binaryRedString = binaryRedString.substring(0,binaryRedString.length()-1)+'0';
		    				//binaryGreenString = binaryGreenString.substring(0,binaryGreenString.length()-1)+'0';
		    				//binaryBlueString = binaryBlueString.substring(0,binaryBlueString.length()-1)+'0';
		    				//System.out.println("0 red");
		    			}
		    			red = Integer.parseInt(binaryRedString, 2);
	    				//green = Integer.parseInt(binaryGreenString, 2);
	    				//blue = Integer.parseInt(binaryBlueString, 2);
	    				rgb = (red << 16 | green << 8 | blue);
	    				//System.out.println("red : "+binaryRedString);
		    			image.setRGB(col, row, rgb);
		    			
//		    			System.out.println("bitNumber : "+bitNumber);
//		    			System.out.println("binaryRedString");
//		    			System.out.println(binaryRedString);
//		    			System.out.println(String.format("%8s", Integer.toBinaryString(new Color(rgb).getRed())).trim());
//		    			System.out.println(String.format("%8s", Integer.toBinaryString(new Color(rgb).getGreen())).trim());
//		    			System.out.println(String.format("%8s", Integer.toBinaryString(new Color(rgb).getBlue())).trim());
		    			//col++;
		    		}else {
		    			break outer;
		    		}
		    		if(_messageBinary.length() > bitNumber+1) {
		    			//System.out.println(bitNumber+1);
		    			//System.out.println(_messageBinary.charAt(bitNumber+1));
		    			if(_messageBinary.charAt(bitNumber+1) == '1') {
		    				//binaryRedString = binaryRedString.substring(0,binaryRedString.length()-1)+'1';
		    				binaryGreenString = binaryGreenString.substring(0,binaryGreenString.length()-1)+'1';
		    				//binaryBlueString = binaryBlueString.substring(0,binaryBlueString.length()-1)+'1';
		    				//System.out.println("1 green");
		    			}else {
		    				//binaryRedString = binaryRedString.substring(0,binaryRedString.length()-1)+'0';
		    				binaryGreenString = binaryGreenString.substring(0,binaryGreenString.length()-1)+'0';
		    				//binaryBlueString = binaryBlueString.substring(0,binaryBlueString.length()-1)+'0';
		    				//System.out.println("0 green");
		    			}
		    			//red = Integer.parseInt(binaryRedString, 2);
	    				green = Integer.parseInt(binaryGreenString, 2);
	    				//blue = Integer.parseInt(binaryBlueString, 2);
	    				rgb = (red << 16 | green << 8 | blue);
	    				//System.out.println("green : "+binaryGreenString);
		    			image.setRGB(col, row, rgb);
//		    			System.out.println("binaryRedString");
//		    			System.out.println(binaryRedString);
//		    			System.out.println(String.format("%8s", Integer.toBinaryString(new Color(rgb).getRed())).trim());
//		    			System.out.println(String.format("%8s", Integer.toBinaryString(new Color(rgb).getGreen())).trim());
//		    			System.out.println(String.format("%8s", Integer.toBinaryString(new Color(rgb).getBlue())).trim());
		    			//col++;
		    		}else {
		    			break outer;
		    		}
		    		if(_messageBinary.length() > bitNumber+2) {
		    			//System.out.println(bitNumber+2);
		    			//System.out.println(_messageBinary.charAt(bitNumber+2));
		    			if(_messageBinary.charAt(bitNumber+2) == '1') {
		    				//binaryRedString = binaryRedString.substring(0,binaryRedString.length()-1)+'1';
		    				//binaryGreenString = binaryGreenString.substring(0,binaryGreenString.length()-1)+'1';
		    				binaryBlueString = binaryBlueString.substring(0,binaryBlueString.length()-1)+'1';
		    				//System.out.println("1 blue");
		    			}else {
		    				//binaryRedString = binaryRedString.substring(0,binaryRedString.length()-1)+'0';
		    				//binaryGreenString = binaryGreenString.substring(0,binaryGreenString.length()-1)+'0';
		    				binaryBlueString = binaryBlueString.substring(0,binaryBlueString.length()-1)+'0';
		    				//System.out.println("0 blue");
		    			}
		    			//red = Integer.parseInt(binaryRedString, 2);
	    				//green = Integer.parseInt(binaryGreenString, 2);
	    				blue = Integer.parseInt(binaryBlueString, 2);
	    				rgb = (red << 16 | green << 8 | blue);
	    				//System.out.println("blue : "+binaryBlueString);
		    			image.setRGB(col, row, rgb);
//		    			System.out.println("binaryRedString");
//		    			System.out.println(binaryRedString);
//		    			System.out.println(String.format("%8s", Integer.toBinaryString(new Color(rgb).getRed())).trim());
//		    			System.out.println(String.format("%8s", Integer.toBinaryString(new Color(rgb).getGreen())).trim());
//		    			System.out.println(String.format("%8s", Integer.toBinaryString(new Color(rgb).getBlue())).trim());
		    			//col++;
		    		}else {
		    			break outer;
		    		}
		    		bitNumber = bitNumber+3;
		    		
		    	}
		    	/*if( (bitNumber > _messageBinary.length()) || (bitNumber+1 > _messageBinary.length()) || (bitNumber+2 > _messageBinary.length()) ) {
	    			break;
	    		}else {System.out.println("je passe par la...");} */
		    }
		    String [] items = _fileName.split("\\.");
		    List<String> itemList = Arrays.asList(items);
		    //System.out.println(_fileName);
		    //System.out.println("list : "+itemList.get(1));
		    File outputfile = new File(_fileName);
		    if(itemList.size()>=0) {
    	    	outputfile = new File(itemList.get(0)+".png");
			}
			try {
				ImageIO.write(image, "png", outputfile);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }

		return _messageBinary.length();
	}
	
	public void cryptMessageSizeToImage(Image _image, int _messageSize, String _fileName){
		//on va mettre la taille du message binaire dans les 5 derniers bits 
		//des 3 derniers octets
		BufferedImage image;
		int bitNumber = 0;
		int rgb = 0;
		int red=0;
		int green=0;
		int blue=0;
		if (_image instanceof BufferedImage)
	    {
			image = (BufferedImage) _image;
	        int width = image.getWidth();
		    int height = image.getHeight();
		    
		    Color mycolor = new Color(image.getRGB(width-1, height-1));
    		String binaryMessageSize = String.format("%15s", Integer.toBinaryString(_messageSize)).replace(' ', '0');

		    System.out.println("Crypter -> message_size : "+binaryMessageSize);
    		String binaryRedString = String.format("%8s", Integer.toBinaryString(mycolor.getRed())).trim();
    		String binaryGreenString = String.format("%8s", Integer.toBinaryString(mycolor.getGreen())).trim();
    		String binaryBlueString = String.format("%8s", Integer.toBinaryString(mycolor.getBlue())).trim();
    		
    		while(binaryRedString.length() < 8) {
    			binaryRedString = "0"+binaryRedString;
    		}
    		while(binaryGreenString.length() < 8) {
    			binaryGreenString = "0"+binaryGreenString;
    		}
    		while(binaryBlueString.length() < 8) {
    			binaryBlueString = "0"+binaryBlueString;
    		}
    		
    		
    		String redBits="";
    		String greenBits="";
    		String blueBits="";
    		for(int i=0;i<binaryMessageSize.length();i++) {
    			if(i<5){
    				redBits = redBits+binaryMessageSize.charAt(i);
    			}
    			if(i>=5 && i<10){
    				greenBits = greenBits+binaryMessageSize.charAt(i);
    			}
    			if(i>=10 && i<15){
    				blueBits = blueBits+binaryMessageSize.charAt(i);
    			}
    			
    		}
    		
    		binaryRedString = binaryRedString.substring(0,binaryRedString.length()-5)+redBits;
    		binaryGreenString = binaryGreenString.substring(0,binaryGreenString.length()-5)+greenBits;
    		binaryBlueString = binaryBlueString.substring(0,binaryBlueString.length()-5)+blueBits;
    		//System.out.println("binaryRedString : "+binaryRedString);
    		//System.out.println("binaryGreenString : "+binaryGreenString);
    		//System.out.println("binaryBlueString : "+binaryBlueString);
    		red = Integer.parseInt(binaryRedString, 2);
    		green = Integer.parseInt(binaryGreenString, 2);
    		blue = Integer.parseInt(binaryBlueString, 2);
    		rgb = (red << 16 | green << 8 | blue);
    		image.setRGB(width-1, height-1, rgb);

    		String [] items = _fileName.split("\\.");
    	    List<String> itemList = Arrays.asList(items);
    	    File outputfile = new File(_fileName);
    	    if(itemList.size()>=0) {
    	    	outputfile = new File(itemList.get(0)+".png");
			}
    	    //System.out.println(_fileName);
    	    //System.out.println("list : "+itemList.get(1));
    		try {
    			ImageIO.write(image, "png", outputfile);
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
	    }
	    

	    
	}
	
	public static int[][] convertTo2DWithoutUsingGetRGB(String _filePath) {
			//BufferedImage image = new BufferedImage(new File(""));
		Image img = null;
		if (img instanceof BufferedImage)
	    {
			BufferedImage image= (BufferedImage) img;
	    }
		BufferedImage image;
		try {
			image = ImageIO.read(new File(_filePath));
			final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		      final int width = image.getWidth();
		      final int height = image.getHeight();
		      final boolean hasAlphaChannel = image.getAlphaRaster() != null;

		      int[][] result = new int[height][width];
		      if (hasAlphaChannel) {
		         final int pixelLength = 4;
		         for (int pixel = 0, row = 0, col = 0; pixel + 3 < pixels.length; pixel += pixelLength) {
		            int argb = 0;
		            argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
		            argb += ((int) pixels[pixel + 1] & 0xff); // blue
		            argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
		            argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
		            result[row][col] = argb;
		            col++;
		            if (col == width) {
		               col = 0;
		               row++;
		            }
		         }
		      } else {
		         final int pixelLength = 3;
		         for (int pixel = 0, row = 0, col = 0; pixel + 2 < pixels.length; pixel += pixelLength) {
		            int argb = 0;
		            argb += -16777216; // 255 alpha
		            argb += ((int) pixels[pixel] & 0xff); // blue
		            argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
		            argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
		            result[row][col] = argb;
		            col++;
		            if (col == width) {
		               col = 0;
		               row++;
		            }
		         }
		      }

		      return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      return new int[0][0];
	   }
	
	public int[][] getPixelFromPicture() {
		int[][] arr = new int[1][1];
		return arr;
	}
}
