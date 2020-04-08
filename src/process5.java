import java.io.IOException;

public class process5 extends Thread {
	long ID;
	int state;
	public process5(long ID) {
		// TODO Auto-generated constructor stub
		this.ID=ID;
	}
	
	public void run() {

		syscall.printOnScreen("Please enter the lower limit: ");
		int lowerLimit = Integer.parseInt(syscall.takeInputFromScreen());
		syscall.printOnScreen("Please enter the upper limit: ");
		int upperLimit = Integer.parseInt(syscall.takeInputFromScreen());
		String counter = "";
		for (int i = lowerLimit; i <= upperLimit; i++) {
			counter += i + "\n";
		}
		String fileName = "rangeBetween_" + lowerLimit + "_" + upperLimit;
		try {
			syscall.createNewFile(fileName);
		} catch (IOException e) {
			syscall.printOnScreen("An error occured while creating a file");
		}
		try {
			syscall.writeIntoDisk(fileName, counter);
		} catch (IOException e) {
			syscall.printOnScreen("An error occured while writing into the file");
		}

	}



}
