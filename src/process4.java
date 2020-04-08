
public class process4 extends Thread {
	long ID;
	int state;
	public process4(long ID) {
		// TODO Auto-generated constructor stub
		this.ID=ID;
	}
	public void run() {
		for(int i=500; i<=1000;i++) {
			syscall.printOnScreen("process 4: "+i);
		}
	}
}
