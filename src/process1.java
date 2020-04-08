
import java.io.*;

public class process1 extends Thread {
	long ID;
	int state;
	public process1(long ID) {
		// TODO Auto-generated constructor stub
		this.ID=ID;
	}
	public  void run() {
        
        
        /* enter filename with extension to open and read its content */
        
        syscall.printOnScreen("Enter File Name to Open (with extension like file.txt) : ");
       String fname = syscall.takeInputFromScreen();
        String data;
		try {
			data = syscall.takeInputFromFile(fname);
		} catch (IOException e) {
			data ="An error occured while reading input from a file";
		}
        syscall.printOnScreen(data);
        
        /* this will reference only one line at a time */
        
       
	}
	
	
}
