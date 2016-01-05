package ecbpenguinjava;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author bedirhanurgun
 */
public class Ecbpenguinjava {


    public static void main(String[] args) throws Exception 
    {
        File f = new File("tux.bmp");
        if(!f.exists()) { 
            System.out.println("Make sure tux.bmp exists in the current folder");
        }

        FileInputStream fs = new FileInputStream("tux.bmp");
        
        int HEADER_LENGTH = 14;  // 14 byte bmp header
        byte header [] = new byte[HEADER_LENGTH];
        fs.read(header, 0, HEADER_LENGTH);
        
        int INFO_HEADER_LENGTH = 40; // 40-byte bmp info header
        byte infoheader [] = new byte[INFO_HEADER_LENGTH];
        fs.read(infoheader, 0, INFO_HEADER_LENGTH);
        
        byte[] content = new byte[fs.available()];
        fs.read(content);
        
        writeToFile(header, 
                    infoheader, 
                    encrypt(content, "AES"), 
                    "tux_ecb.bmp");
        
        writeToFile(header, 
                    infoheader, 
                    encrypt(content, "AES/ECB/NoPadding"), 
                    "tux_ecb2.bmp");
        
        writeToFile(header, 
                    infoheader, 
                    encrypt(content, "AES/CBC/PKCS5Padding"), 
                    "tux_cbc.bmp");
         
        fs.close();        
    }
    
    public static byte [] encrypt(byte [] input, String transformation) throws Exception
    {
        Cipher cipher = Cipher.getInstance(transformation);
        Key secretKey = new SecretKeySpec("I don't have CAT".getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(input);        
    }
    
    public static void writeToFile(byte[] header, byte [] infoheader, byte [] content, String fileToWrite) throws Exception
    {
        FileOutputStream fos = new FileOutputStream(fileToWrite);
        fos.write(header);
        fos.write(infoheader);
        fos.write(content);
        fos.flush();
        fos.close();    
    }
    
}
