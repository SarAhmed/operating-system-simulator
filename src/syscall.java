
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class syscall {

	static void printOnScreen(String x) {
		System.out.println(x);
	}

	static String takeInputFromScreen() {
		Scanner takeInput = new Scanner(System.in);

		String Input = takeInput.nextLine();
		return Input;

	}

	static String takeInputFromFile(String fileName) throws IOException {
		String line = null;
		StringBuilder sb = new StringBuilder();

		/* FileReader reads text files in the default encoding */
		FileReader fileReader = new FileReader(fileName);

		/* always wrap the FileReader in BufferedReader */
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		while ((line = bufferedReader.readLine()) != null) {
			sb.append(line + "\n");
		}

		/* always close the file after use */
		bufferedReader.close();

		return sb.toString();
	}

	static void writeIntoDisk(String fileName, String data) throws IOException {

		FileWriter myWriter = new FileWriter(fileName);
		myWriter.write(data);
		myWriter.close();

	}

	static boolean createNewFile(String fileName) throws IOException {

		File myObj = new File(fileName);
		if (myObj.createNewFile()) {
			return true;
		}

		return false;
	}

}
