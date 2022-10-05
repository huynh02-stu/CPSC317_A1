
// You can use this file as a starting point for your dictionary client
// The file contains the code for command line parsing and it also
// illustrates how to read and partially parse the input typed by the user. 
// Although your main class has to be in this file, there is no requirement that you
// use this template or hav all or your classes in this file.

import java.lang.System;
import java.io.IOException;
import java.util.Arrays;
import java.io.BufferedReader;

import java.io.*;
import java.lang.Object.*;
import java.net.*;

//
// This is an implementation of a simplified version of a command
// line dictionary client. The only argument the program takes is
// -d which turns on debugging output. 
//

public class CSdict {
    static final int MAX_LEN = 255;
    static Boolean debugOn = false;
    
    private static final int PERMITTED_ARGUMENT_COUNT = 1;
    private static String command;
    private static String[] arguments;
    
    public static void main(String [] args) {
		int len;

		Socket dictSocket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		BufferedReader stdIn = null;
		String inputLine = null;
		DataInputStream inputDATA = null;
		DataOutputStream outputDATA = null;
		String setDictionary= "";

		// Verify command line arguments
        if (args.length == PERMITTED_ARGUMENT_COUNT) {
            debugOn = args[0].equals("-d");
            if (debugOn) {
                System.out.println("Debugging output enabled");
            } else {
                System.out.println("997 Invalid command line option - Only -d is allowed");
                return;
            }
        } else if (args.length > PERMITTED_ARGUMENT_COUNT) {
            System.out.println("996 Too many command line options - Only -d is allowed");
            return;
        }

		// establishes loop until user leaves	
		Boolean loop = true;

		do {
			// Example code to read command line input and extract arguments.
			try {
				System.out.print("csdict> ");
				byte cmdString[] = new byte[MAX_LEN];

				System.in.read(cmdString);

				// Convert the command string to ASII
				String inputString = new String(cmdString, "ASCII");
				
				// Split the string into words
				String[] inputs = inputString.trim().split("( |\t)+");

				// Set the command
				command = inputs[0].toLowerCase().trim();
				// Remainder of the inputs is the arguments. 
				arguments = Arrays.copyOfRange(inputs, 1, inputs.length);

				switch (command) {
					case "quit":
						if (dictSocket == null) {
							System.exit(-1);
							break;
						}
						out.println("QUIT");
						System.exit(-1);
						break;
					case "open": 
						String hostName = arguments[0];

						try {
							Integer.parseInt(arguments[1]);
						} catch (Exception e) {
							System.out.println("902 Invalid argument");
							break;
						}

						int portNumber = Integer.parseInt(arguments[1]);

						// dictSocket = new Socket(hostName, portNumber); 
						try {
							dictSocket = new Socket();
							int TIMEOUT = 30000;
							dictSocket.setSoTimeout(TIMEOUT);
							dictSocket.connect(new InetSocketAddress(hostName, portNumber));
						} catch (Exception e) {
							System.out.println("920 Control connection to " + hostName + " on port " + portNumber +  " failed to open. ");
							break;
						}

						out = new PrintWriter(dictSocket.getOutputStream(), true);
						in = new BufferedReader(new InputStreamReader(dictSocket.getInputStream()));
						
						stdIn = new BufferedReader(new InputStreamReader(System.in));

						inputLine = in.readLine();
						System.out.println(inputLine);

						break;
					case "dict":
						if (dictSocket == null) {
							System.out.println("903 Supplied command not expected at this time.");
							break;
						} else {
							out = new PrintWriter(dictSocket.getOutputStream(), true);
							out.println("SHOW DB");
							while ((inputLine = in.readLine()) != ".") {
								System.out.println(inputLine);
							}
							System.out.println(inputLine);
						}
						break;
					case "set":
						if (dictSocket == null) {
							System.out.println("903 Supplied command not expected at this time.");
							break;
						}

						String chosenDict = arguments[0];

						if (chosenDict.equals("*") || chosenDict.equals("")) {
							setDictionary = "*";
						} else {
							setDictionary = chosenDict;
						}
						break;
					case "define":
					// based off set, will find word in all selected dictionaries
						if ((dictSocket == null) || (setDictionary == "")) {
							System.out.println("903 Supplied command not expected at this time.");
							break;					
						}
						String chosenWord = arguments[0];
						out.println("DEFINE " + setDictionary + " " + chosenWord);
						//inputLine = in.readLine();
						//System.out.println(inputLine);
						if (!setDictionary.equals("*")) {
							while((inputLine = in.readLine()) != ".") {
								System.out.println(inputLine);
							}
							break;	
						} 
						out.println("DEFINE " + "*" + " " + chosenWord);
							while((inputLine = in.readLine()) != ".") {
								System.out.println(inputLine);
							} 
						break;
					case "match":
					// print all exact matches for word in set dictionaries
						if (dictSocket == null) {
							System.out.println("903 Supplied command not expected at this time.");
						}
						chosenWord = arguments[0];
						if (setDictionary == "") {
							out.println("MATCH " +" * . " + chosenWord);
							while((inputLine = in.readLine()) != ".") {
								System.out.println(inputLine);
							} 
						} else {
							out.println("MATCH " + setDictionary + "exact" + chosenWord);
							while((inputLine = in.readLine()) != ".") {
								System.out.println(inputLine);
							} 
						}
						break;
					case "prefixmatch":
					//Retrieve and print all the prefix matches. for WORD. 
					//WORD is looked up in the dictionary or dictionaries as specified through the set command.
						if (dictSocket == null) {
							System.out.println("903 Supplied command not expected at this time.");
						}
						chosenWord = arguments[0];
						out.println("MATCH " + setDictionary + "prefix" + chosenWord);
						if (setDictionary == "") {
							out.println("MATCH " + "* " + "prefix " + chosenWord);
							while((inputLine = in.readLine()) != ".") {
								System.out.println(inputLine);
							} 
						} else {
							out.println("MATCH " + setDictionary + "prefix" + chosenWord);
							while((inputLine = in.readLine()) != ".") {
								System.out.println(inputLine);
							} 
						}
						break;
					case "close":
						if (dictSocket == null) {
							System.out.println("903 Supplied command not expected at this time.");
							break;
						}
						out.close();
						stdIn.close();
						in.close();
						dictSocket.close();
						break;
					default :
						System.out.println("900 Invalid command");
						break;
				}
			} catch (IOException exception) {
				System.err.println("998 Input error while reading commands, terminating.");
				System.exit(-1);
			}
		} while (loop);
	}
}

    
    
