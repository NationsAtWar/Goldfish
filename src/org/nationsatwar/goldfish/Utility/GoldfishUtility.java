package org.nationsatwar.goldfish.Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.nationsatwar.goldfish.Goldfish;


public class GoldfishUtility {
	
	static Logger log = Logger.getLogger("Minecraft");
	
	public static void saveInstance(String worldName) {
		
		if (Bukkit.getServer().getWorld(Goldfish.prototypePath + worldName) != null)
			Bukkit.getServer().getWorld(Goldfish.prototypePath + worldName).save();

	    File worldDir = new File(worldName + "\\");
	    File protoDir = new File(Goldfish.prototypePath + worldName + "\\");
	    
	    copyDirectory(worldDir, protoDir);
	    
	    File uidFile = new File(Goldfish.prototypePath + worldName + "\\uid.dat");
	    uidFile.delete();
	    
	    GoldfishPrototypeConfig.savePrototypeConfig(worldName);
	}
	
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
	            
        	} catch (Exception e) { log.info("Couldn't copy directory: " + e.getMessage()); }
        }
    }
 
    public static void deleteDirectory(File file) throws IOException {
 
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
        	   if (file.list().length ==0 )
        		   file.delete();
    		}
 
    	} else
    		file.delete();
    }
    
    public static String getPrototypeName(String instanceName) {
    	
    	String prototypeName = instanceName;
    	
    	if (instanceName.length() > Goldfish.prototypePath.length() && 
    			instanceName.substring(0, Goldfish.prototypePath.length()).equals(Goldfish.prototypePath))
    		prototypeName = instanceName.substring(Goldfish.prototypePath.length());
    	else if (instanceName.length() > Goldfish.instancePath.length() && 
    			instanceName.substring(0, Goldfish.instancePath.length()).equals(Goldfish.instancePath))
    		prototypeName = instanceName.substring(Goldfish.instancePath.length());
		
		for (int i = 0; i < prototypeName.length(); i++) {
			
			if (prototypeName.charAt(i) == '_')
				return prototypeName.substring(0, i);
		}
    	
    	return prototypeName;
    }
    
    public static int getInstanceID(String instanceName) {
		
		for (int i = 0; i < instanceName.length(); i++) {
			
			if (instanceName.charAt(i) == '_') {
				
				try { return Integer.parseInt(instanceName.substring(i + 1)); }
				catch (Exception e) { log.info("Trying to extract ID from static instance: " + e.getMessage()); }
			}
		}
    	
    	return 0;
    }
    
    public static boolean isPrototype(String checkName) {
    	
    	if (checkName.contains(Goldfish.prototypePath))
    		return true;
    	else
    		return false;
    }
    
    public static boolean isInstance(String checkName) {
    	
    	if (checkName.contains(Goldfish.instancePath))
    		return true;
    	else
    		return false;
    }
	
	public static String getExistingInstance(String instanceName, String userName) {
		
	    String[] folderList = new File(Goldfish.instancePath).list();
		
		for (String fileName : folderList) {
			
			if (fileName == null)
				continue;
			
			if (fileName.substring(0, instanceName.length()).equals(instanceName) &&
					fileName.charAt(instanceName.length()) == '_') {
				
				File dataFile = new File(Goldfish.instancePath + fileName + "\\" + "instancedata.yml");
				
				if (dataFile.exists()) {
					
					FileConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
					
					if (config.contains("user." + userName))
						return Goldfish.instancePath + fileName;
				}
			}
		}
		
		return null;
	}
}