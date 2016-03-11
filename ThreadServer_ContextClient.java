import java.io.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.Thread;

public class ThreadServer_ContextClient {
	

	public static void main(String[] args) {
		try {
			
			ServerSocket server = new ServerSocket(4444); // 4444
			
			
			
			while (true) {
				Socket client = server.accept();
				EchoHandler handler = new EchoHandler(client);
				handler.start();
			}
		} catch (Exception e) {
			System.err.println("Exception caught:" + e);
		}
	} // main
	
	
	
}//ThreadServer_ContextClient



class EchoHandler extends Thread implements Runnable  {


	
	String userInput = "hrj"; 
    Socket client;
	
	
    String hostName="194.47.44.145"; //same cheng
	
	int portNumber = 8080; // ......................................................uncoimmment
							
	
	
	Socket echoSocket = null; 
	PrintWriter outToContext = null; 
	BufferedReader in = null;
	BufferedReader stdIn = null;

	InetAddress ip = null;

	
	EchoHandler(Socket  client) {
		this.client =  client;
	}
	
	
	public void run(){
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					client.getInputStream()));
			PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
			while (true) { 
				String line = reader.readLine(); // enter
			
			
				String REGEX_Android = "A"; // A: !!
				String REGEX_Kinect = "K";

				Pattern pA = Pattern.compile(REGEX_Android);
				Matcher mA = pA.matcher(line);

				Pattern pK = Pattern.compile(REGEX_Kinect);
				Matcher mK = pK.matcher(line);

				String ToContextServer = "no gesture yet"; // hex/ binary value

				double[] android_accelerometer = new double[10]; // 100 > 10!!

				if (mA.find()) {
					System.out.println("Android discovered");// A:x y z# THEY NEED "\n" AT THE END, readline, doc
					String[] token0 = line.split("A|:| |#");
					System.out
							.println("android data start......................");
					for (int q = 0; q < token0.length; q++) {
						System.out.println(token0[q]);
					}

				
					System.out
							.println("end of android data................................");
					
					android_accelerometer[3] = Double.parseDouble(token0[3]); 
					if (android_accelerometer[3] >= 6.1 && android_accelerometer[3] <= 7.1) {
						System.out.println(android_accelerometer[3]);
						System.out.println("mobile up");
						ToContextServer = "h";
					}
					if (android_accelerometer[3] <= -6.1 && android_accelerometer[3] >= -7.1) {
						System.out.println(android_accelerometer[3]);
						System.out.println("mobile down");
						ToContextServer = "j";
					}

					
					System.out.println("end of x coordinates");

				}//if for android

				// to-do..................... right-left if

				double[] coordinate_y = new double[100]; 

				
				if (mK.find()) {
					System.out.println("Kinect discovered");
					// K: s r x1 y1 z1: e r x2 y2 z2: w r x3 y3 z3:#

					String[] token1 = line.split("K| |:|#");
					

					System.out
							.println("kinect data start......................"); 
					// print whole index
					for (int k = 0; k < token1.length; k++) { 
						System.out.println(token1[k] + ";"); // don't count ;,
																
					}
					//
					System.out
							.println("end of Kinect data.................................");
					System.out.println("***********************");
					System.out.println("x");

					for (int v = 6; v < token1.length; v += 6) {
						System.out.println(token1[v]);
						
						coordinate_y[v + 1] = Double.parseDouble(token1[v + 1]);
						System.out.println(token1[v + 1]);

						
					}// for
					
					if ((coordinate_y[19] > coordinate_y[13])
							&& (coordinate_y[13] > coordinate_y[7])) {
						System.out.println("right hand up");
						
		
						ToContextServer = "h";
						
					}

					if ((coordinate_y[19] < coordinate_y[13])
							&& (coordinate_y[13] < coordinate_y[7])) {
						System.out.println("right hand down");
						
						ToContextServer = "j";
					}

				}// if for kinect

				
				System.out.println("HHHHHH 1"); 

				
				System.out.println("HHHHHH 2");
				if (echoSocket == null) { 
					
				
					try {
						
						echoSocket = new Socket(hostName, portNumber);
						
						outToContext = new PrintWriter(
								echoSocket.getOutputStream(), true);
						in = new BufferedReader(new InputStreamReader(
								echoSocket.getInputStream()));
						
					} catch (IOException e) {
						System.err.println("err " + e.getMessage());
					}// try catch
				}
				
				System.out.println("HHHHHH 3");
				try {
					while (userInput != ToContextServer) { 
						System.out
								.println("server listening on 8080 will get this:"); // client
																						
						outToContext.println(ToContextServer);
						
						System.out.println("in.readline");

						System.out.println("echo: " + in.readLine());
						
						System.out.println("ToContextServer..........."+ToContextServer);
						userInput = ToContextServer;
					}// while
				} catch (IOException e) { // added by IDE!
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
				System.out.println("HHHHHH 4");
				String[] token0 = line.split("A|K|:| |#"); // == parseInt ; Data
				
		
				System.out.println("HHHHHH 5");
				
			}
		} catch (Exception e) {
			System.err.println("Exception caught: client disconnected."); 
			System.err.println(e.getMessage()); 
		} finally {
			try {
				client.close();
			} catch (Exception e) {
				;
			}
		}
	} // run
}// EchoHandler
