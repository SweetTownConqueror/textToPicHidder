package textToImage;

import java.awt.EventQueue;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Main {

	public static void main(String[] args) {
		//Frame1 f = new Frame1();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame1 window = new Frame1();
					window.getFrame().setVisible(true);
					//on recupere l'image à encrypter
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}
	//TODO: Crypter.getinitialisationVector : faire en sorte que meme une image noire marche
	//TODO: utiliser une clé personnalisable
	//TODO:  faire en sorte que si un message est trop long on le coupe pour le faire passer
	//dans limage

}
