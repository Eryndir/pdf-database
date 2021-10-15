import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import scala.collection.JavaConverters._
import scala.io.StdIn.readLine

class DriveHandler:
  private val APPLICATION_NAME = "pdf-database"
  private val JSON_FACTORY = JacksonFactory.getDefaultInstance()
  private val TOKENS_DIRECTORY_PATH = "tokens"
  private val SCOPES =
    Collections.singletonList(DriveScopes.DRIVE_METADATA_READONLY)
  val CREDENTIALS_FILE_PATH = "/credentials.json"
  val HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()
  val service = new Drive.Builder(
    HTTP_TRANSPORT,
    JSON_FACTORY,
    getCredentials(HTTP_TRANSPORT)
  )
    .setApplicationName(APPLICATION_NAME)
    .build()

  def getCredentials(HTTP_TRANSPORT: NetHttpTransport): Credential = {
    val in = this.getClass.getResourceAsStream(CREDENTIALS_FILE_PATH)
    val clientSecrets =
      GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in))

    val flow = new GoogleAuthorizationCodeFlow.Builder(
      HTTP_TRANSPORT,
      JSON_FACTORY,
      clientSecrets,
      SCOPES
    ).setDataStoreFactory(
      new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH))
    ).setAccessType("offline")
      .build()

    val receiver = new LocalServerReceiver.Builder().setPort(8888).build()

    new AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
  }

  def quickstartScala: Unit = {

    val searchQuery = "Läsår"
    val boolFile = false

    val result = service
      .files()
      .list()
      .setQ(s"name contains '$searchQuery'")
      .setFields("nextPageToken, files(id, name)")
      .execute()

    val files = result.getFiles()
    val size = files.size
    val urlFilePrefix = "https://drive.google.com/file/d/"
    val urlFolderPrefix = "https://drive.google.com/drive/folders/"

    if files == null || files.isEmpty() then println("No files found.")
    else
      println("Files:")

      files.forEach(file =>
        if boolFile then
          println(s"${file.getName()}\n ${urlFilePrefix + file.getId}}\n")
        else println(s"${file.getName()}\n ${urlFolderPrefix + file.getId}}\n")
      )
  }

  def searchFile(searchTerm: String): List[File] =
    val result = service
      .files()
      .list()
      .setQ(
        s"name contains '$searchTerm' and not '1Kbu1VM-wNeyLeN4Yfu4sfsFUptuxZxp4' in parents"
      )
      .setFields("nextPageToken, files(id, name, webContentLink, spaces)")
      .execute()

    result.getFiles().asScala.toList

  def getFileLink(searchTerm: String): String =
    val files = searchFile(searchTerm)
    val urlFilePrefix = "https://drive.google.com/file/d/"
    val urlFolderPrefix = "https://drive.google.com/drive/folders/"

    if files.size > 1 then
      files.foreach(e =>
        println(
          s"${files.indexOf(e)}: ${e.getName} - ${e.getWebContentLink}\n\t${e.get}"
        )
      )
      println("Choose file")
      val choice = readLine().toInt
      return files(choice).getWebContentLink
    else if files.size == 1 then
      println("!!!!")
      val contentLink = files.head.getWebContentLink
      if contentLink == null then return (urlFolderPrefix + files.head.getId)
      else return contentLink
    else return "No files"

//@main
def quickstsrtMain: Unit =
  val info = new DriveHandler
  info.searchFile("Witcher")
