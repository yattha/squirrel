

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/*
 * Derek Moore & Heather Pedersen
 */

public class Main {
	static String output = "Select the text file for compression. \nDecompress will open two dialogues, first compresssed file, second codes.";
	static long txtSize;
	static JTextArea textDisplay = new JTextArea();
	public static void main(String[] args) {
		textDisplay.setText(output);
		StringBuilder text = new StringBuilder("");	
		final JFrame compression = new JFrame("342 Huffman");
		final JPanel mainArea = new JPanel();
		compression.add(mainArea, BorderLayout.NORTH);
		compression.setVisible(true);JButton compB = new JButton("Compress");
		
		
		
		
		
		//Generate compress button, will prompt to open text file to be compressed.
		//Outputs to compressed.txt
		compB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fp = new JFileChooser(".");
				fp.setDialogTitle("Open file to be compressed");
				fp.showOpenDialog(null);
				
				File textFile = fp.getSelectedFile();		
				try {
					long start = System.currentTimeMillis();
					@SuppressWarnings("resource")
					FileInputStream stream = new FileInputStream(textFile);
					int charRead = 1;
					while(charRead > 0) {
						charRead = stream.read();
						text.append(((char) charRead));
					}
					txtSize = textFile.length()/1024;
					output = "Compressing " + textFile.getName()
							+ " (" + textFile.length()/1024 + "KB)" + "\n Time elapsed: " + (System.currentTimeMillis()-start)/1000 + "s";		
					textDisplay.setText(output);
					
					CodingTree textTree = new CodingTree(text.toString());
					generateOutput(textTree);		
					compression.pack();
					stream.close();	
				} catch (IOException | NullPointerException e1) {
					//Do nothing
				}	
			}	 
		});
		mainArea.add(compB);
		


		JButton uncompB = new JButton("DeCompress");
		uncompB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Map<Character, String> inCodes = new HashMap<Character, String>();
				StringBuilder inBits = new StringBuilder(), tempByte = new StringBuilder();
				JFileChooser fp = new JFileChooser(".");
				fp.setDialogTitle("Choose compressed file");
				fp.showOpenDialog(null);
				File textFile = fp.getSelectedFile();		
				try {
					long start = System.currentTimeMillis();
					@SuppressWarnings("resource")
					FileInputStream stream = new FileInputStream(textFile);
					int byteRead = 1, numberread = 0;
					
					while(byteRead >= 0) {
						byteRead = stream.read();
						tempByte.delete(0, tempByte.length());
						tempByte.append(Integer.toBinaryString(byteRead));
						while(tempByte.length()<8) tempByte.insert(0, '0');
						
						
						inBits.append(tempByte.toString());
					
					}
					stream.close();			
					
					
				} catch (IOException | NullPointerException e1) {
					//Do nothing
				}	
				
				
				fp.setDialogTitle("Open code file:");
				fp.showOpenDialog(null);
				
				File codeFile = fp.getSelectedFile();
				StringBuilder codesSB = new StringBuilder();
				long start= 0;
				try {
					
					start = System.currentTimeMillis();
					@SuppressWarnings("resource")
					FileInputStream stream = new FileInputStream(codeFile);
					int charRead = 1;
					
					
					
					while(charRead > 0) {
						charRead = stream.read();
						codesSB.append(((char) charRead));
					}
					
					
					
					String codeString = codesSB.substring(1, codesSB.length()-2);
					String[] charWithCodeArray = codeString.split(", ");
					int i = 0;
					while(charWithCodeArray.length> i) {
						
						inCodes.put(charWithCodeArray[i].charAt(0), charWithCodeArray[i].substring(2));
						i++;
					}
					
					
					
					
								
					
						
				} catch (IOException | NullPointerException e1) {
					//Do nothing
				}	
				
				PrintStream outputFile;		
				
				try {
					
					outputFile = new PrintStream(new File("./decompressed.txt"));
					outputFile.append(CodingTree.decode(inBits.toString(), inCodes));
					outputFile.close();
					textDisplay.setText("Decompression finished: decompressed.txt produced.\nTime Elapsed: " + (System.currentTimeMillis()-start)/1000 +"s");
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}	 

			

		});
		
		mainArea.setPreferredSize(new Dimension(400,100));
		mainArea.add(uncompB);
		compression.add(textDisplay, BorderLayout.CENTER);
		compression.pack();


	
	}





	private static void generateOutput(CodingTree textTree) {
		PrintStream outputFile;		
		try {
			outputFile = new PrintStream(new File("./codes.txt"));
			outputFile.append(textTree.codeToString());
			outputFile.close();			

			OutputStream out = new BufferedOutputStream(new FileOutputStream("./compressed.txt"));
			for(int i = 0 ; i < textTree.bits.size(); i++) out.write(textTree.bits.get(i).byteValue());			
			out.close();			
			File compressedFile = new File("./compressed.txt");
			output+= "\ncompressed.txt (" + compressedFile.length()/1024 + "KB)    Compression ratio : " + ((compressedFile.length()/1024.0)/txtSize)*100.0 +"%";
			textDisplay.setText(output);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}


}
