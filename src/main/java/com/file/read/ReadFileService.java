package com.file.read;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReadFileService {
    public ReadFileService() {
    }

    /**
     * 读取某个文件夹下的所有文件(支持多级文件夹)
     */
    public List<File> readfile(String filepath) throws IOException {
            ArrayList<File> files = new ArrayList<File>();
        try {

            File file = new File(filepath);
            if (!file.isDirectory()) {
                    files.add(file);
//                System.out.println("文件");
//                System.out.println("path=" + file.getPath());
//                System.out.println("absolutepath=" + file.getAbsolutePath());
//                System.out.println("name=" + file.getName());

            } else if (file.isDirectory()) {
                System.out.println("文件夹");
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File readfile = new File(filepath + "\\" + filelist[i]);
                    if (!readfile.isDirectory()) {
                            files.add(readfile);
//                        System.out.println("path=" + readfile.getPath());
//                        System.out.println("absolutepath=" + readfile.getAbsolutePath());
//                        System.out.println("name=" + readfile.getName());

                    } else if (readfile.isDirectory()) {
                        readfile(filepath + "\\" + filelist[i]);
                    }
                }

            }

        } catch (FileNotFoundException e) {
            System.out.println("readfile()   Exception:" + e.getMessage());
        }
        return files;
    }

//    public static void main(String[] args) {
//        try {
//            readfile("C:\\Users\\Administrator\\OneDrive\\桌面\\123");
//            // deletefile("D:/file");
//        } catch (FileNotFoundException ex) {
//                ex.printStackTrace();
//        } catch (IOException ex) {
//                ex.printStackTrace();
//        }
//        System.out.println("ok");
//    }
}