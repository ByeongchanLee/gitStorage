import java.io.File;

public class test {
	public static void main(String args[]) {

		String path = "C:\\Users\\10310_LeeBC\\Desktop\\testMusic";
		File dirFile = new File(path);
		File[] fileList = dirFile.listFiles();
		for (File tempFile : fileList) {
			if (tempFile.isFile()) {
				String tempPath = tempFile.getParent();
				String tempFileName = tempFile.getName();
				System.out.println("Path=" + tempFile);
				System.out.println("FileName=" + tempFileName);
			}
		}
	}
}
