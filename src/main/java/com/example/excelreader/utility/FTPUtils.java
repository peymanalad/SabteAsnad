package com.example.excelreader.utility;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

@Slf4j
public class FTPUtils {

  private FTPClient ftpClient = null;

  private void showServerReply(FTPClient ftpClient) {
    String[] replies = ftpClient.getReplyStrings();
    if (replies != null && replies.length > 0) {
      for (String aReply : replies) {
        log.info("FTP SERVER: " + aReply);
      }
    }
  }

  public boolean connect(String server, int port) {
    ftpClient = new FTPClient();
    try {
      ftpClient.connect(server, port);
      showServerReply(ftpClient);
      int replyCode = ftpClient.getReplyCode();
      if (!FTPReply.isPositiveCompletion(replyCode)) {
        log.info("Operation failed. Server reply code: " + replyCode);
        ftpClient.disconnect();
        return false;
      }
      return true;
    } catch (IOException ex) {
      log.error("Oops! Something wrong happened for connect to the server");
      ex.printStackTrace();
    }
    return false;
  }

  public boolean login(String username, String password) {
    try {
      this.ftpClient.enterLocalPassiveMode();
      boolean success = ftpClient.login(username, password);
      showServerReply(ftpClient);
      if (!success) {
        log.info("Could not login to the server");
        ftpClient.disconnect();
        return false;
      } else {
        log.info("LOGGED IN SERVER");
        return true;
      }
    } catch (IOException ex) {
      log.error("Oops! Something wrong happened for login to the server");
      ex.printStackTrace();
    }
    return false;
  }

  public void disconnect() {
    if (this.ftpClient.isConnected()) {
      try {
        this.ftpClient.logout();
        this.ftpClient.disconnect();
      } catch (IOException ex) {
        log.error("Oops! Something wrong happened for disconnect action");
        ex.printStackTrace();
      }
    }
  }

  public boolean downloadFile(String remoteFilePath, String localFilePath) {
    try {
      /*this.ftpClient.enterLocalPassiveMode();*/
      this.ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
      log.info("Start downloading file");
      try (FileOutputStream fos = new FileOutputStream(localFilePath)) {
        boolean success = this.ftpClient.retrieveFile(remoteFilePath, fos);
        if (success) {
          log.info("File {} has been downloaded successfully.", remoteFilePath);
          return true;
        } else {
          log.info("can not download file.");
          return false;
        }
      }
    } catch (IOException e) {
      log.error("Oops! Something wrong happened for download file");
      e.printStackTrace();
    }
    return false;
  }

  public boolean uploadFile(String localFileFullName, String remoteFileName, String hostDir) {
    try {
      this.ftpClient.enterLocalPassiveMode();
      log.info("Start uploading file");
      try (InputStream input = new FileInputStream(localFileFullName)) {
        boolean success = this.ftpClient.storeFile(hostDir + remoteFileName, input);
        if (success) {
          log.info("File {} is uploaded successfully.", localFileFullName);
          return true;
        } else {
          log.info("can not upload file.");
          return false;
        }
      }
    } catch (IOException e) {
      log.error("Oops! Something wrong happened for upload file");
      e.printStackTrace();
    }
    return false;
  }

}
