package com.aim.core.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @AUTO 文件工具
 * @Author AIM
 * @DATE 2021/10/21
 */
public class FileUtils {

    /**
     * 递归获取指定目录下面所有的Java文件,包括子目录中的
     *
     * @param file 文件目录
     * @return 所有java文件
     */
    public static List<String> getAllJavaFiles(File file) {
        if (!file.exists()) {
            return new ArrayList(0);
        }

        if (file.isFile()) {
            if (file.getName().lastIndexOf(".java") > 0) {
                List list = new ArrayList(1);
                list.add(file.getAbsolutePath());
                return list;
            } else {
                return new ArrayList(0);
            }
        }

        List list = new ArrayList();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    list.addAll(getAllJavaFiles(f));
                }
            }
        }
        return list;
    }

}
