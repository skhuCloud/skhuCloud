package com.skhu.cloud.service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.zip.ZipOutputStream;

public interface DownloadService {

    void downloadOne(HttpServletResponse httpServletResponse, String path);

    void zipFile(HttpServletResponse httpServletResponse, String path) throws IOException;

    void addFolder(ZipOutputStream zipOut, String relativePath) throws IOException;

    void addFile(File subFile, ZipOutputStream zipOut, String relativePath) throws IOException;
}
