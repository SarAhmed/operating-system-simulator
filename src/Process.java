//import java.util.concurrent.Semaphore;

public class Process extends Thread {

	public int processID;
	ProcessState status = ProcessState.New;

	public Process(int m) {
		processID = m;
	}

	@Override
	public void run() {
		try {

			switch (processID) {
			case 1:
				process1();
				break;
			case 2:
				process2();
				break;
			case 3:
				process3();
				break;
			case 4:
				process4();
				break;
			case 5:
				process5();
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void process1() throws InterruptedException {
		// semwait printText
		OperatingSystem.semPrintWait(this);
		OperatingSystem.semReadFileWait(this);
		OperatingSystem.semScreenInputWait(this);

		OperatingSystem.printText("Enter File Name: ");

		// semwait takeInput, ReadFile
		OperatingSystem.printText(OperatingSystem.readFile(OperatingSystem.TakeInput()));
		// semPost printText, takeInput, ReadFile

		setProcessState(this, ProcessState.Terminated);

		OperatingSystem.semPrintPost(this);
		OperatingSystem.semReadFilePost(this);
		OperatingSystem.semScreenInputPost(this);
	}

	private void process2() throws InterruptedException {
		// semwait print
		OperatingSystem.semPrintWait(this);
		OperatingSystem.semScreenInputWait(this);
		OperatingSystem.semWriteFileWait(this);

		OperatingSystem.printText("Enter File Name: ");
		// semwait takeInput
		String filename = OperatingSystem.TakeInput();
		OperatingSystem.printText("Enter Data: ");
		// sempost print
		String data = OperatingSystem.TakeInput();
		// sempost takeInput
		// semwait writeFile
		OperatingSystem.writefile(filename, data);
		// sempost writeFile
		setProcessState(this, ProcessState.Terminated);

		OperatingSystem.semPrintPost(this);
		OperatingSystem.semScreenInputPost(this);
		OperatingSystem.semWriteFilePost(this);

	}

	private void process3() throws InterruptedException {
		OperatingSystem.semPrintWait(this);

		int x = 0;
		while (x < 301) {
			OperatingSystem.printText(x + "\n");
			x++;
		}
		setProcessState(this, ProcessState.Terminated);

		OperatingSystem.semPrintPost(this);

	}

	private void process4() throws InterruptedException {
		OperatingSystem.semPrintWait(this);

		int x = 500;
		while (x < 1001) {
			OperatingSystem.printText(x + "\n");
			x++;
		}
		setProcessState(this, ProcessState.Terminated);
		
		OperatingSystem.semPrintPost(this);


	}

	private void process5() throws InterruptedException {
		OperatingSystem.semScreenInputWait(this);
		OperatingSystem.semPrintWait(this);
		OperatingSystem.semWriteFileWait(this);

		OperatingSystem.printText("Enter LowerBound: ");
		String lower = OperatingSystem.TakeInput();
		OperatingSystem.printText("Enter UpperBound: ");
		String upper = OperatingSystem.TakeInput();
		int lowernbr = Integer.parseInt(lower);
		int uppernbr = Integer.parseInt(upper);
		String data = "";

		while (lowernbr <= uppernbr) {
			data += lowernbr++ + "\n";
		}
		OperatingSystem.writefile("P5.txt", data);
		setProcessState(this, ProcessState.Terminated);
		
		OperatingSystem.semScreenInputPost(this);
		OperatingSystem.semPrintPost(this);
		OperatingSystem.semWriteFilePost(this);

	}

	public static void setProcessState(Process p, ProcessState s) {
		p.status = s;
		if (s == ProcessState.Terminated) {
			OperatingSystem.ProcessTable.remove(OperatingSystem.ProcessTable.indexOf(p));
		}
	}

	public static ProcessState getProcessState(Process p) {
		return p.status;
	}
}
