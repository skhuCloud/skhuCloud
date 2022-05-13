package com.skhu.cloud.service;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public interface DownloadService {

    boolean before(HttpServletResponse httpServletResponse, Queue<String> que_path);

    void downloadOne(HttpServletResponse httpServletResponse, File file);

    void zipFile(HttpServletResponse httpServletResponse, Queue<String> que_path);

    void addFolder(ZipOutputStream zipOut, String relativePath) ;

    void addFile(File subFile, ZipOutputStream zipOut, String relativePath);

}
