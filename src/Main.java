import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
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
			@SuppressWarnings("resource")
			FileInputStream stream = new FileInputStream(textFile);
			int charRead = 1;
			while(charRead > 0) {
				charRead = stream.read();
				theText.append(((char) charRead));
			}
			stream.close();
		} catch (IOException | NullPointerException e) {
		
		}
		
	}
	

	private static void generateOutput(CodingTree textTree) {
		PrintStream outputFile;
		try {
			outputFile = new PrintStream(new File("codes.txt"));
			outputFile.append(textTree.toString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}