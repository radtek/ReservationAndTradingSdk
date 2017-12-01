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
 * 通过Java的Zip输入输出流实现压缩和解压文件
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
     * 压缩文件
     * 
     * @param filePath
     *            待压缩的文件路径
     * @return 压缩后的文件
     */
    public File zip(String filePath) {
        File target = null;
        File source = new File(filePath);
        if (source.exists()) {
            // 压缩文件名=源文件名.zip
            String zipName = source.getName() + ".zip";
            target = new File(source.getParent(), zipName);
            if (target.exists()) {
                target.delete(); // 删除旧的文件
            }
            FileOutputStream fos = null;
            ZipOutputStream zos = null;
            try {
                fos = new FileOutputStream(target);
                zos = new ZipOutputStream(new BufferedOutputStream(fos));
                // 添加对应的文件Entry
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
	 * 扫描添加文件Entry
	 * 
	 * @param base
	 *            基路径
	 * 
	 * @param source
	 *            源文件
	 * @param zos
	 *            Zip文件输出流
	 * @throws IOException
	 */
	private void addEntry(String base, File source, ZipOutputStream zos)
			throws IOException {
		// 按目录分级，形如：/aaa/bbb.txt
		String entry = base + source.getName();
		if (source.isDirectory()) {
			for (File file : source.listFiles()) {
				// 递归列出目录下的所有文件，添加文件Entry
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
     * 解压文件
     * 
     * @param filePath
     *            压缩文件路径
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
                        // 创建文件父目录
                        target.getParentFile().mkdirs();
                    }
                    // 写入文件
                    bos = new BufferedOutputStream(new FileOutputStream(target));
                    int read = 0;
                    byte[] buffer = new byte[1024 * 10];
                    while ((read = zis.read(buffer, 0, buffer.length)) != -1) {
                        bos.write(buffer, 0, read);
                    }
                    bos.flush();
                 // 获取解压缩文件路径
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