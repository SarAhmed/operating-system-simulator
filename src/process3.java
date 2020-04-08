
public class process3 extends Thread {
	long ID;
	int state;
	public process3(long ID) {
		// TODO Auto-generated constructor stub
		this.ID=ID;
	}
	
	public void run() {
		
		for (int i = 0; i <= 300; i++) {
			syscall.printOnScreen("process 3: "+i);
		}
	}
}
