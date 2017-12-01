package com.icbc.devp.tool.utilZip;

import java.io.Closeable;
import java.io.IOException;
 
/**
 * IO��������
 * 
 * @author kfzx-yanyj
 * 
 */
public class IOUtil {
    /**
     * �ر�һ������������
     * 
     * @param closeables
     *            �ɹرյ��������б�
     * @throws IOException
     */
    public static void close(Closeable... closeables) throws IOException {
        if (closeables != null) {
            for (Closeable closeable : closeables) {
                if (closeable != null) {
                    closeable.close();
                }
            }
        }
    }
 
    /**
     * �ر�һ������������
     * 
     * @param closeables
     *            �ɹرյ��������б�
     */
    public static void closeQuietly(Closeable... closeables) {
        try {
            close(closeables);
        } catch (IOException e) {
            // do nothing
        }
    }
}