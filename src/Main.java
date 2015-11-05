import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.nio.channels.WritableByteChannel;
import java.util.Scanner;

import javax.swing.JFileChooser;


public class Main {

	public static void main(String[] args) {
		StringBuilder text = new StringBuilder("");
		
		readTxt(text);
		CodingTree textTree = new CodingTree(text.toString());
		
		generateOutput(textTree);
		
		//System.out.println(text.toString());
	}
	

	private static void readTxt(StringBuilder theText) {
		JFileChooser fp = new JFileChooser(".");
		fp.showOpenDialog(null);
		File textFile = fp.getSelectedFile();
		
		try {
			long start = System.currentTimeMillis();
			@SuppressWarnings("resource")
			FileInputStream stream = new FileInputStream(textFile);
			int charRead = 1;
			while(charRead > 0) {
				charRead = stream.read();
				theText.append(((char) charRead));
			}
			stream.close();
			
			System.out.println((double)(System.currentTimeMillis()-start)/1000 + "s");
		} catch (IOException | NullPointerException e) {
		
		}	
	}
	 

	private static void generateOutput(CodingTree textTree) {
		PrintStream outputFile;
		
		try {
			outputFile = new PrintStream(new File("./codes.txt"));
			outputFile.append(textTree.codeToString());
			outputFile.close();
			
			//OutputStream outFile = new BufferedOutputStream(new FileOutputStream("./compressed.txt")); 
			
			OutputStream out = new BufferedOutputStream(new FileOutputStream("./compressed.txt"));
			for(int i = 0 ; i < textTree.bits.size(); i++) out.write(textTree.bits.get(i).byteValue());
			
			out.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
