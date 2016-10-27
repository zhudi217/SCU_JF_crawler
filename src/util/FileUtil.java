package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class FileUtil {

	public static boolean writeIntoFile(String content, String filePath,  
            boolean isAppend) {  
		
        boolean isSuccess = true;  
        // 先过滤掉文件名  
        int index = filePath.lastIndexOf("/");  
        String dir = filePath.substring(0, index);  
        // 创建除文件的路径  
        File fileDir = new File(dir);  
        if(!fileDir.exists())
        	fileDir.mkdirs();  
        // 再创建路径下的文件  
        File file = null;  
        try {  
            file = new File(filePath);  
            file.createNewFile();  
        } catch (IOException e) {  
            isSuccess = false;  
            e.printStackTrace();  
        }  
        // 写入文件  
        OutputStreamWriter out = null;
       // FileWriter fileWriter = null;  
        try { 
        	out = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
//            fileWriter = new FileWriter(file, isAppend);  
//            fileWriter.write(content);  
//            fileWriter.flush();  
        	out.write(content);
        	out.flush();
        	
        } catch (IOException e) {  
            isSuccess = false;  
            e.printStackTrace();  
        } finally {  
            try {  
                if (out != null)  
                    out.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        //System.out.println("Output succeeded:" + isSuccess);
        return isSuccess;  
    } 
}
