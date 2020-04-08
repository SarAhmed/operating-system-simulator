import java.io.IOException;

public class process2 extends Thread {
	long ID;
	int state;
	public process2(long ID) {
		// TODO Auto-generated constructor stub
		this.ID=ID;
	}
	public void run() {
		

		syscall.printOnScreen("Enter File Name to create (with extension like file.txt)");
		String fileName = syscall.takeInputFromScreen();
		syscall.printOnScreen("Enter the data to write in the file all on one line");
		String data = syscall.takeInputFromScreen();
		try {
			syscall.createNewFile(fileName);
			syscall.writeIntoDisk(fileName, data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			syscall.printOnScreen("An error Occured");
		}

	}



}
