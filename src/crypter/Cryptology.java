package crypter;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Cryptology {
	private String key = "Bar12345Bar12345Bar12345Bar12345";
	
	public Cryptology() {
		
	}
	public static byte[] getInitializationVector(Image _image){
		//pour le vecteur dinitialisation je prend la composante RG des 8 premiers pixels
		// que je transforme en byte
		
		byte[] iv = new byte[128/8];
		//iv.length
		BufferedImage image;
		//int[][] result;
		if (_image instanceof BufferedImage)
	    {
	        image = (BufferedImage) _image;
	        int width = image.getWidth();
		    int height = image.getHeight();
		    //result = new int[height][width];
		    for (int row = 0; row < 1; row++) {
		    	for (int col = 0; col < 8; col++) {
		    		//result[row][col] = image.getRGB(col, row);
		    		Color mycolor = new Color(image.getRGB(col, row));
		    		byte red = (byte) mycolor.getRed();
		    		byte green = (byte) mycolor.getGreen();
		    		for(int i=0; i<iv.length; i++) {
		    			if(iv[i]==0) {
		    				iv[i]=red;
		    				iv[i+1]=green;
		    				break;
		    			}
		    		}
		    		/*
		    		for(int i=0; i<iv.length; i++) {
		    			System.out.println("la taille est de : "+iv[i]);
		    		}*/
		    	}
		    }
	    }
		return iv;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public  static boolean isValidKey(String _key) {
		if(!_key.isEmpty() && _key.length()==32) {
			return true;
		}else {
			return false;
		}
	}
	
}
