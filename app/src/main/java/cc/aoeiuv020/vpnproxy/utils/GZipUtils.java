package cc.aoeiuv020.vpnproxy.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;  
import java.io.DataInputStream;
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.InputStream;  
import java.io.OutputStream;  
import java.util.zip.GZIPInputStream;  
import java.util.zip.GZIPOutputStream;  

//GZIP压缩解压缩工具 
public abstract class GZipUtils {  

    //数据压缩 
    public static byte[] compress(byte[] data) throws Exception {  
        ByteArrayInputStream bais = new ByteArrayInputStream(data);  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  

        // 压缩  
        compress(bais, baos);  

        byte[] output = baos.toByteArray();  

        baos.flush();  
        baos.close();  

        bais.close();  

        return output;  
    }  

    //文件压缩 
    public static void compress(File file) throws Exception {  
        compress(file, true);  
    }  

    //文件压缩 是否删除原始文件 
    public static void compress(File file, boolean delete) throws Exception {  
        FileInputStream fis = new FileInputStream(file);  
        FileOutputStream fos = new FileOutputStream(file.getPath() + ".gz");  

        compress(fis, fos);  

        fis.close();  
        fos.flush();  
        fos.close();  

        if (delete) {  
            file.delete();  
        }  
    }  

    //数据压缩 
    public static void compress(InputStream is, OutputStream os)  throws Exception {  

        GZIPOutputStream gos = new GZIPOutputStream(os);  

        int count;  
        byte data[] = new byte[1024];  
        while ((count = is.read(data, 0, 1024)) != -1) {  
            gos.write(data, 0, count);  
        }  

        gos.finish();  

        gos.flush();  
        gos.close();  
    }  

    //文件压缩  默认删除源文件
    public static void compress(String path) throws Exception {  
        compress(path, true);  
    }  

    //文件压缩 是否删除原始文件 
    public static void compress(String path, boolean delete) throws Exception {  
        File file = new File(path);  
        compress(file, delete);  
    }  

    //数据解压缩 
    public static byte[] decompress(byte[] data) throws Exception {  
        ByteArrayInputStream bais = new ByteArrayInputStream(data);  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  

        // 解压缩  

        decompress(bais, baos);  

        data = baos.toByteArray();  

        baos.flush();  
        baos.close();  

        bais.close();  

        return data;  
    }  

    //文件解压缩 
    public static void decompress(File file) throws Exception {  
        decompress(file, true);  
    }  

    //文件解压缩  是否删除原始文件
    public static void decompress(File file, boolean delete) throws Exception {  
        FileInputStream fis = new FileInputStream(file);  
        FileOutputStream fos = new FileOutputStream(file.getPath().replace(".gz",  ""));  
        decompress(fis, fos);  
        fis.close();  
        fos.flush();  
        fos.close();  

        if (delete) {  
            file.delete();  
        }  
    }  

    //数据解压缩 
    public static void decompress(InputStream is, OutputStream os)  throws Exception {  

        GZIPInputStream gis = new GZIPInputStream(is);  

        int count;  
        byte data[] = new byte[1024];  
        while ((count = gis.read(data, 0, 1024)) != -1) {  
            os.write(data, 0, count);  
        }  

        gis.close();  
    }  

    //文件解压缩 ,默认删除源文件
    public static void decompress(String path) throws Exception {  
        decompress(path, true);  
    }  

    //文件解压缩   是否删除原始文件 
    public static void decompress(String path, boolean delete) throws Exception {  
        File file = new File(path);  
        decompress(file, delete);  
    }  

} 
