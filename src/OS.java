
public class OS {
	static long IDassigner=1;
	public static void main(String[] args) {
		
		process1 p1 = new process1(IDassigner++);
		process2 p2 = new process2(IDassigner++);
		process3 p3 = new process3(IDassigner++);
		process4 p4 = new process4(IDassigner++);
		process5 p5 = new process5(IDassigner++);
		p1.start();
		p2.start();
		p4.start();
		p3.start();
		p5.start();
	
		
	}
}
