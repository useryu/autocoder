package cn.agilecode.autocoder.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {

	public static String genPackagePath(String modelPackageName) {
		String[] dirs = modelPackageName.split("\\.");
		StringBuilder sb = new StringBuilder();
		for(String dir:dirs) {
			sb.append(File.separator).append(dir);
		}
		return sb.toString();
	}

	public static void wirtToFile(String fullDirPath, String fileName, String content, boolean delOldFile) throws IOException {
		wirtToFile(fullDirPath, fileName, content, delOldFile, "java");
	}
	
	public static void wirtToFile(String fullDirPath, String fileName, String content, boolean delOldFile, String ext) throws IOException {
		File dir = new File(fullDirPath);
		if (!dir.exists())
			dir.mkdirs();
		
		String target = fullDirPath + File.separator + fileName + "." + ext;
		
		File file = new File(target);
		if (file.exists()) {
			if(delOldFile) {
				file.delete();
			} else {
				return;
			}
		}
		
		FileWriter fw = new FileWriter(file);
		try {
			fw.write(content);
		}
		finally {
			fw.close();
		}
	}
	
}
