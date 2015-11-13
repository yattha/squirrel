
import java.awt.BorderLayout;
import java.awt.Dimension;
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
import javax.swing.JPanel;
import javax.swing.JTextArea;

/*
 * Derek Moore & Heather Pedersen
 */

public class Main {
	static String output = "Currently compressing WarAndPeace.txt please wait.... ~15 seconds.";
	static long txtSize;
	static JFrame compression = new JFrame("342 Huffman");
	static JTextArea textDisplay = new JTextArea();
	public static void main(String[] args) {
		textDisplay.setText(output);
		final StringBuilder text = new StringBuilder("");	
		
		final JPanel mainArea = new JPanel();
		compression.add(mainArea, BorderLayout.NORTH);
		compression.setVisible(true);JButton compB = new JButton("Compress");
		
		
		
		
		
		//Generate compress button, will prompt to open text file to be compressed.
		//Outputs to compressed.txt
		compB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				text.delete(0, text.length());
				JFileChooser fp = new JFileChooser(".");
				fp.setDialogTitle("Open file to be compressed");
				fp.showOpenDialog(null);
				
				File textFile = fp.getSelectedFile();		
				try {
					long start = System.currentTimeMillis();
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
					//long start = System.currentTimeMillis();
					FileInputStream stream = new FileInputStream(textFile);
					int byteRead = 1;
					
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
		
		
		
		
		
		testWarAndPeace(text);
		
		
		
		
		
		// To test MyPriorityQueue:
		//testMyPriorityQueue();
	
	}


	private static void testWarAndPeace(StringBuilder text) {
		File textFile = new File("./WarAndPeace.txt");
		
		try {
			FileInputStream stream;
			stream = new FileInputStream("./WarAndPeace.txt");
			int charRead = 1;
			long start = System.currentTimeMillis();
			while(charRead > 0) {
				charRead = stream.read();
				text.append(((char) charRead));
			}
			txtSize = textFile.length()/1024;
			output = "Compressing WarAndPeace.txt"
					+ " (" + textFile.length()/1024 + "KB)" + "\n Time elapsed: " + (System.currentTimeMillis()-start)/1000 + "s";		
			textDisplay.setText(output);
			
			CodingTree textTree = new CodingTree(text.toString());
			generateOutput(textTree);
			stream.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}


	private static void generateOutput(CodingTree textTree) {
		PrintStream outputFile;		
		try {
			File codes = new File("./codes.txt");
			codes.delete();
			outputFile = new PrintStream(new File("./codes.txt"));
			
			outputFile.append(textTree.codeToString());
			outputFile.close();			
			
			
			File compresssed = new File("./compressed.txt");
			compresssed.delete();
			OutputStream out = new BufferedOutputStream(new FileOutputStream("./compressed.txt"));
			
			for(int i = 0 ; i < textTree.bits.size(); i++) out.write(textTree.bits.get(i).byteValue());			
			out.close();			
			File compressedFile = new File("./compressed.txt");
			output+= "\ncompressed.txt (" + compressedFile.length()/1024 + "KB)    Compression ratio : " + ((compressedFile.length()/1024.0)/txtSize)*100.0 +"%";
			textDisplay.setText(output);
			outputFile.close();
			compression.pack();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	private static void testMyPriorityQueue() {
		MyPriorityQueue<Integer> test = new MyPriorityQueue<Integer>();
		System.out.println("Testing add():");
		for (int i = 0; i < 10; i++) {
			int temp = (int) (Math.random()*100);
			while (test.contains(temp)) temp = (int) (Math.random()*10);
			test.add(temp);
		}
		System.out.println(test.toString() + "\n\n");
		
		System.out.println("Testing if MyPriorityQueue is filled. Passed:" +!(test.size() == 0 ) + "\n\n");
		
		System.out.println("Testing poll():");
		for (int i = 0; i < 10; i++) {
			test.poll();
			System.out.println(test.toString());
		}
		System.out.println("\n");
		
		
		System.out.println("Testing if MyPriorityQueue is now empty: " + (test.size() == 0) + "\n\n");
		
		System.out.println("Testing offer method (offers 42):");
		test.offer(42);
		System.out.println(test.toString() + "\n\n");
		
		System.out.println("Testing contains method (contans 42): " + test.contains(42) + "\n\n");
		
		System.out.println("Testing clear method: ");
		test.clear();
		System.out.println(test.toString() + "\n\n");
		
		System.out.println("MyPriorityQueue is now of size: " + test.size() + "\n\n\n\n");
	}

	
	
}
