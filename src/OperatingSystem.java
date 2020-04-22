import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class OperatingSystem {

	public static ArrayList<Thread> ProcessTable;
	public static Queue<Process> ReadyQueue;

	public static Semaphore readFile;
	public static Semaphore writeFile;
	public static Semaphore printText;
	public static Semaphore takeInput;

	// system calls:
	// 1- Read from File
	@SuppressWarnings("unused")
	public static String readFile(String name) {
		String Data = "";
		File file = new File(name);
		try {
			Scanner scan = new Scanner(file);
			while (scan.hasNextLine()) {
				Data += scan.nextLine() + "\n";
			}
			scan.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return Data;
	}

	// 2- Write into file
	@SuppressWarnings("unused")
	public static void writefile(String name, String data) {
		try {
			BufferedWriter BW = new BufferedWriter(new FileWriter(name));
			BW.write(data);
			BW.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

	// 3- print to console
	@SuppressWarnings("unused")
	public static void printText(String text) {

		System.out.println(text);

	}

	// 4- take input

	@SuppressWarnings("unused")
	public static String TakeInput() {
		Scanner in = new Scanner(System.in);
		String data = in.nextLine();
		return data;

	}

	private void createProcess(int processID) {
		Process p = new Process(processID);
		ProcessTable.add(p);
		Process.setProcessState(p, ProcessState.Ready);
		addElement(p);

	}

	// method used to remove and return an element from the ReadyQueue
	public Process removeElement() throws InterruptedException {
		synchronized (ReadyQueue) {

			// while the list is empty, wait (makes the dispatcher waiting till a process is
			// added)
			while (ReadyQueue.isEmpty()) {
				ReadyQueue.wait();
			}
			Process element = ReadyQueue.poll();

			return element;
		}
	}

	// method to add an element in the ReadyQueue
	public void addElement(Process element) {
		synchronized (ReadyQueue) {

			ReadyQueue.add(element);
			ReadyQueue.notify();
		}
	}

	static OperatingSystem os = new OperatingSystem();

	public static void main(String[] args) {
		ProcessTable = new ArrayList<Thread>();
		ReadyQueue = new LinkedList<Process>();
		initializeSemaphores();
		/*
		 * Because the os should be always waiting for any process to execute it. the
		 * dispatcher should not terminate after the ready queue is empty, may be a
		 * process will be created after some time.
		 * 
		 * If you want the dispatcher to stop execution whenever the ReadyQueue is empty
		 * please comment the next line -->dispatcher(); on line 119 and
		 * uncomment-->dispatcher2(); on line 126
		 * 
		 */
		dispatcher();
		os.createProcess(3);
		os.createProcess(4);
		os.createProcess(2);
		os.createProcess(1);
		os.createProcess(5);
//		dispatcher2();

	}

	public static void dispatcher() {
		Thread t1 = new Thread() {
			public void run() {
				synchronized (this) {

					while (true) {

						try {
							/*
							 * Remove the next process from the ReadyQueue based on the First Come First
							 * Serve concept
							 */
							Process p = os.removeElement();
							// If this the first time to execute the process invoke the .start() method
							if (!p.started) {
								p.status = ProcessState.Running;
								p.started = true;
								p.start();

							} else { // Otherwise resume execution of the process
								p.status = ProcessState.Running;
								p.resume();
							}
							/*
							 * The next loop assures that the dispatcher will no dispatch another process
							 * from the ReadyQueue till the current process either finishes
							 * execution(terminated) or gets blocked(waiting)
							 */
							// Comment the next while loop if you want to run all the threads in pararrel
							while (p.status == ProcessState.Running)
								;

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		};
		t1.start();
	}

	public static void dispatcher2() {

		while (!ReadyQueue.isEmpty()) {

			try {
				/*
				 * Remove the next process from the ReadyQueue based on the First Come First
				 * Serve concept
				 */
				Process p = os.removeElement();
				// If this the first time to execute the process invoke the .start() method
				if (!p.started) {
					p.status = ProcessState.Running;
					p.started = true;
					p.start();

				} else { // Otherwise resume execution of the process
					p.status = ProcessState.Running;
					p.resume();
				}
				/*
				 * The next loop assures that the dispatcher will no dispatch another process
				 * from the ReadyQueue till the current process either finishes
				 * execution(terminated) or gets blocked(waiting)
				 */
				// Comment the next while loop if you want to run all the threads in pararrel
				while (p.status == ProcessState.Running)
					;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private static void initializeSemaphores() {
		readFile = new Semaphore();
		takeInput = new Semaphore();
		printText = new Semaphore();
		writeFile = new Semaphore();
	}

	public static void semPrintWait(Process task) throws InterruptedException {
		printText.semWait(task);

	}

	public static void semPrintPost(Process task) throws InterruptedException {
		printText.semPost(task);

	}

	public static void semReadFileWait(Process task) throws InterruptedException {
		readFile.semWait(task);

	}

	public static void semReadFilePost(Process task) throws InterruptedException {
		readFile.semPost(task);

	}

	public static void semScreenInputWait(Process task) throws InterruptedException {
		takeInput.semWait(task);

	}

	public static void semScreenInputPost(Process task) throws InterruptedException {
		takeInput.semPost(task);

	}

	public static void semWriteFileWait(Process task) throws InterruptedException {
		writeFile.semWait(task);

	}

	public static void semWriteFilePost(Process task) throws InterruptedException {
		writeFile.semPost(task);

	}

	// class semaphore
	static class Semaphore {
		private Queue<Process> blockedQueue;
		private int ID;
		private boolean available;

		public Semaphore() {
			blockedQueue = new LinkedList<Process>();
			available = true;
		}

		private boolean isAvailable() {
			return available;
		}

		public boolean semWait(Process process) throws InterruptedException {
			int processID = process.processID;
			if (isAvailable()) {
				available = false;
				ID = processID;
				return true;
			} else {
				blockedQueue.add(process);
				Process.setProcessState(process, ProcessState.Waiting);
				
				if (!isAvailable()) {
					process.suspend();
				}
			}
			return false;
		}

		public boolean semPost(Process process) {
			int processID = process.processID;

			if (ID == processID) {
				if (!blockedQueue.isEmpty()) {
					Process p = blockedQueue.poll();
					ID = p.processID;
					Process.setProcessState(p, ProcessState.Ready);
					os.addElement(p);
				} else {
					setAvailable();

				}
				return true;
			}
			return false;
		}

		private void setAvailable() {
			available = true;
		}
	}

}
