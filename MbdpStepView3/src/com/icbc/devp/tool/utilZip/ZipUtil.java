package com.icbc.devp.tool.utilZip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
 


import com.icbc.devp.tool.utilZip.IOUtil;
 
/**
 * ͨ��Java��Zip���������ʵ��ѹ���ͽ�ѹ�ļ�
 * 
 * @author kfzx-yanyj
 * 
 */
public  class ZipUtil {
 
	public ZipUtil() {
        // empty
    }
 
	private String getDBTableName(String str){
		int idx = str.lastIndexOf("\\");
		if(idx==-1)return "";
		str = str.substring(idx+1);	
		for(int i=0;i<str.length();++i)
			if(Character.isAlphabetic(str.charAt(i)))
				{
				   idx = i;break;
				}
		int idx2=idx;
		if(idx==-1)return "";
		while(idx2<str.length()){
			if(Character.isDigit(str.charAt(idx2)))break;
			idx2++;
		}
	  System.out.println(str.substring(idx,idx2));
	  return str.substring(idx,idx2);
	}
    /**
     * ѹ���ļ�
     * 
     * @param filePath
     *            ��ѹ�����ļ�·��
     * @return ѹ������ļ�
     */
    public File zip(String filePath) {
        File target = null;
        File source = new File(filePath);
        if (source.exists()) {
            // ѹ���ļ���=Դ�ļ���.zip
            String zipName = source.getName() + ".zip";
            target = new File(source.getParent(), zipName);
            if (target.exists()) {
                target.delete(); // ɾ���ɵ��ļ�
            }
            FileOutputStream fos = null;
            ZipOutputStream zos = null;
            try {
                fos = new FileOutputStream(target);
                zos = new ZipOutputStream(new BufferedOutputStream(fos));
                // ��Ӷ�Ӧ���ļ�Entry
                addEntry("/", source, zos);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                IOUtil.closeQuietly(zos, fos);
            }
        }
        return target;
    }
 
	/**
	 * ɨ������ļ�Entry
	 * 
	 * @param base
	 *            ��·��
	 * 
	 * @param source
	 *            Դ�ļ�
	 * @param zos
	 *            Zip�ļ������
	 * @throws IOException
	 */
	private void addEntry(String base, File source, ZipOutputStream zos)
			throws IOException {
		// ��Ŀ¼�ּ������磺/aaa/bbb.txt
		String entry = base + source.getName();
		if (source.isDirectory()) {
			for (File file : source.listFiles()) {
				// �ݹ��г�Ŀ¼�µ������ļ�������ļ�Entry
				addEntry(entry + "/", file, zos);
			}
		} else {
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			try {
				byte[] buffer = new byte[1024 * 10];
				fis = new FileInputStream(source);
				bis = new BufferedInputStream(fis, buffer.length);
				int read = 0;
				zos.putNextEntry(new ZipEntry(entry));
				while ((read = bis.read(buffer, 0, buffer.length)) != -1) {
					zos.write(buffer, 0, read);
				}
				zos.closeEntry();
			} finally {
				IOUtil.closeQuietly(bis, fis);
			}
		}
	}
 
    /**
     * ��ѹ�ļ�
     * 
     * @param filePath
     *            ѹ���ļ�·��
     */
    public void unzip(String filePath) {
        File source = new File(filePath);
        if (source.exists()) {
            ZipInputStream zis = null;
            BufferedOutputStream bos = null;
            try {
                zis = new ZipInputStream(new FileInputStream(source));
                ZipEntry entry = null;
                while ((entry = zis.getNextEntry()) != null
                        && !entry.isDirectory()) {
                    File target = new File(source.getParent(), entry.getName());
                    fileNames.add(target.getPath());
                    if (!target.getParentFile().exists()) {
                        // �����ļ���Ŀ¼
                        target.getParentFile().mkdirs();
                    }
                    // д���ļ�
                    bos = new BufferedOutputStream(new FileOutputStream(target));
                    int read = 0;
                    byte[] buffer = new byte[1024 * 10];
                    while ((read = zis.read(buffer, 0, buffer.length)) != -1) {
                        bos.write(buffer, 0, read);
                    }
                    bos.flush();
                 // ��ȡ��ѹ���ļ�·��
                    tableNames.add(getDBTableName(target.getAbsolutePath()));
                }
                zis.closeEntry();            
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                IOUtil.closeQuietly(zis, bos);
            }
        }
        
    }
    
    public static void main(String[] args) {
        //String targetPath = "C:\\Users\\kfzx-yanyj\\Desktop\\SikuliX_Excel";
        //File file = ZipUtil.zip(targetPath);
       // System.out.println(file);
        new ZipUtil().unzip("D:\\oracle_ddl.zip");
    }
    
    private HashSet<String> tableNames = new HashSet<String>();
    public HashSet<String> getTableNames() {
		return tableNames;
	}

	public void setTableNames(HashSet<String> tableNames) {
		this.tableNames = tableNames;
	}

	private HashSet<String> fileNames = new HashSet<String>();
    
    public HashSet<String> getFileNames() {
		return fileNames;
	}

	public void setFileNames(HashSet<String> fileNames) {
		this.fileNames = fileNames;
	}
}