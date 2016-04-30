package njusoftware.noComment.SamurAI.base;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Helper {
	private static final File file = new File("log.txt");
	private static BufferedWriter bfw;
	static {
		try {
			bfw = new BufferedWriter(new FileWriter(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void write(String s) throws IOException {
		bfw.write(s);
		bfw.newLine();
	}
}
