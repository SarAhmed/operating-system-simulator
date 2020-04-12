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

//	public static int activeProcess= 0;
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

	private  void createProcess(int processID) {
		Process p = new Process(processID);
		ProcessTable.add(p);
		Process.setProcessState(p, ProcessState.Ready);
		addElement(p);
//		p.start();
		

	}

	// method used to remove an element from the list
	public Process removeElement() throws InterruptedException {
		synchronized (ReadyQueue) {

			// while the list is empty, wait
			while (ReadyQueue.isEmpty()) {
				System.out.println("List is empty...");
				ReadyQueue.wait();
				System.out.println("Waiting...");
			}
			Process element = ReadyQueue.poll();

			return element;
		}
	}

	// method to add an element in the list
	public void addElement(Process element) {
		System.out.println("Opening...");
		synchronized (ReadyQueue) {

			// add an element and notify all that an element exists
			ReadyQueue.add(element);
			System.out.println("New Element:'" + element + "'");

			ReadyQueue.notify();
			System.out.println("notifyAll called!");
		}
		System.out.println("Closing...");
	}
	static Thread t1;

	public static void main(String[] args) {
		ProcessTable = new ArrayList<Thread>();
		ReadyQueue = new LinkedList<Process>();
		OperatingSystem os = new OperatingSystem();
		 t1 = new Thread() {
			public void run() {
				synchronized (this) {
					
				
				while (true) {
					/*
					 * Proces p =removeElement();
					 * 
					 * p.start() or p.notify()
					 * 
					 */
					try {
						Process p = os.removeElement();
						System.out.println("I got the process");
						p.start();
						System.out.println("the process is running");

//						while (Process.getProcessState(p) != ProcessState.Terminated) {
//							this.wait();
//						}
						p.join();

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			}
		};
		t1.start();
		// run method looping
		os.createProcess(3);
		os.createProcess(4);
		os.createProcess(2);
		os.createProcess(1);
//		
		os.createProcess(5);
//		os.createProcess(5);

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

	// class semaphore
	static class Semaphore {
		private Queue<Process> blockedQueue;
		private int ID;
		private boolean available;

		public Semaphore() {
			ID = -1;
			available = true;
		}

		private boolean isAvailable() {
			return available;
		}

		public synchronized boolean semWait(Process process) throws InterruptedException {
			int processID = process.processID;
			if (isAvailable()) {
				available = false;
				ID = processID;
				return true;
			} else {
				blockedQueue.add(process);
				Process.setProcessState(process, ProcessState.Waiting);
				while (!isAvailable()) { // ask merna
					process.wait();

				}
			}
			return false;
		}

		public synchronized boolean semPost(Process process) {
			int processID = process.processID;
			if (ID == processID) {
				if (!blockedQueue.isEmpty()) {
					Process p = blockedQueue.poll();
					ID = p.processID;
					Process.setProcessState(p, ProcessState.Ready);
					ReadyQueue.add(p);
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
