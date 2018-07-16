package com.cgnpc.dnmc.tool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.cgnpc.dnmc.Constants;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

public class XStreamTool {

    public static <T> T generateBeanFromXml(String path, Class<T> cls) {
        if (path == null || path.isEmpty()) {
            return null;
        }
        try {
            File dataXml = new File(path);
            XStream stream = new XStream(new DomDriver());
            stream.autodetectAnnotations(true);
            // 忽略不需要的节点
            stream.ignoreUnknownElements();
            // 对指定的类使用Annotations 进行序列化
            stream.processAnnotations(cls);
            T obj = (T) stream.fromXML(dataXml);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> void generateXmlFromBean(String path,Class<T> cls, Object obj) {
        XStream xStream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        xStream.processAnnotations(cls);
        String xml = xStream.toXML(obj);
        StringBuffer sb = new StringBuffer();
        sb.append(Constants.TARGET_FILE_XML_PREFIX);
        sb.append(xml);
        System.out.println(cls.getSimpleName());
        System.out.println(xml);
        generateFile(path,cls.getSimpleName(), sb.toString());

    }

    private static void generateFile(String path,String fileNamePrefix, String xmlData) {
        String filename = fileNamePrefix + "_" + getFileDate() + Constants.FILENAME_SUFFIX;
        String finalpath = path + filename;
        boolean fileFlag = createFileToLocal(path);
        System.out.println(fileFlag);
        if (fileFlag) {
            try {
                FileOutputStream fos = new FileOutputStream(finalpath);
                fos.write(xmlData.getBytes());
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static String getFileDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
        // 获取String类型的时间
        String createdate = sdf.format(date);
        return createdate;
    }

    private static boolean createFileToLocal(String localPath) {
        try {
            File localFile = new File(localPath);
            if (!localFile.exists()) {
                localFile.mkdirs();
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
