package org.nationsatwar.goldfish.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {
	
	public static void copyDirectory(File sourceLocation, File targetLocation) {
		
		if (sourceLocation.isDirectory()) {
			
			if (!targetLocation.exists())
				targetLocation.mkdir();
			
			String[] children = sourceLocation.list();
			
			for (int i=0; i<children.length; i++)
				copyDirectory(new File(sourceLocation, children[i]), new File(targetLocation, children[i]));
			
		} else {
			
			try {
				InputStream in = new FileInputStream(sourceLocation);
				OutputStream out = new FileOutputStream(targetLocation);
				
				// Copy the bits from instream to outstream
				byte[] buf = new byte[1024];
				int len;
				
				while ((len = in.read(buf)) > 0)
					out.write(buf, 0, len);
				
				in.close();
				out.close();
			} catch (Exception e) { System.out.println("Couldn't copy directory: " + e.getMessage()); }
		}
	}
	
	public static void deleteDirectory(File file) {
		
		if (file.isDirectory()) {
			
			// If directory is empty, then delete it
			if (file.list().length == 0)
				file.delete();
			else {
				
				// Lists all the directory contents
				String files[] = file.list();
				
				for (String temp : files) {
					
					// Construct the file structure
					File fileDelete = new File(file, temp);
					
					// Recursive delete
					deleteDirectory(fileDelete);
				}
				
				// Check the directory again, delete if empty
				if (file.list().length == 0)
					file.delete();
			}
		} else
			file.delete();
	}
}