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
import java.util.List;

class Information:
  private val APPLICATION_NAME = "pdf-database"
  private val JSON_FACTORY = JacksonFactory.getDefaultInstance()
  private val TOKENS_DIRECTORY_PATH = "tokens"
  private val SCOPES =
    Collections.singletonList(DriveScopes.DRIVE_METADATA_READONLY)
  val CREDENTIALS_FILE_PATH = "/credentials.json";

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
    val HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()

    val service = new Drive.Builder(
      HTTP_TRANSPORT,
      JSON_FACTORY,
      getCredentials(HTTP_TRANSPORT)
    )
      .setApplicationName(APPLICATION_NAME)
      .build()

    val result = service
      .files()
      .list()
      .setPageSize(100)
      .setFields("nextPageToken, files(id, name)")
      .execute()

    val files = result.getFiles()
    val size = files.size

    if files == null || files.isEmpty() then println("No files found.")
    else
      println("Files:")
      files.forEach(file => println(file.getName() + "(" + file.getId + ")"))
  }

@main def quickstsrtMain: Unit =
  val info = new Information
  info.quickstartScala
