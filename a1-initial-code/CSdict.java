
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
        byte cmdString[] = new byte[MAX_LEN];
	int len;

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

	//establishes loop until user leaves	
	int loopcondition = 0;

	while (loopcondition == 0) {

	// Example code to read command line input and extract arguments.
	
        try {
	    System.out.print("csdict> ");
	    System.in.read(cmdString);

	    // Convert the command string to ASII
	    String inputString = new String(cmdString, "ASCII");
	    
	    // Split the string into words
	    String[] inputs = inputString.trim().split("( |\t)+");
	    // Set the command
	    command = inputs[0].toLowerCase().trim();
	    // Remainder of the inputs is the arguments. 
	    arguments = Arrays.copyOfRange(inputs, 1, inputs.length);
		
		/*
	    System.out.println("The command is: " + command);
	    len = arguments.length;
	    System.out.println("The arguments are: ");
	    for (int i = 0; i < len; i++) {
		System.out.println("    " + arguments[i]);
	    }
	    System.out.println("Done."); 
		*/


		////////////

		switch (command) {
			case "open": 
				String hostName = arguments[0];
				int portNumber =  Integer.parseInt(arguments[1]);

				Socket echoSocket = new Socket(hostName, portNumber);  

				PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);

				BufferedReader in =new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
				BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

				System.out.println("success with socket implementation");
				break;

			case "dict":
				System.out.println("command = dict");
				break;
			case "set":
				System.out.println("command = set");
				break;
			case "define":
				System.out.println("command = define");
				break;
			case "match":
				System.out.println("command = match");
				break;
			case "prefixmatch":
				System.out.println("command = prefixmatch");
				break;
			case "close":
				System.out.println("command = close");
				break;
			case "quit":
				System.out.println("command = quit");
				System.exit(-1);
			default :
				System.out.println("900 Invalid command");
				break;
		}

		///////////
	    
	} catch (IOException exception) {
	System.err.println("998 Input error while reading commands, terminating.");
            System.exit(-1);
	}

    }
	}
}
