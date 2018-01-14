package org.polentino911.rpiws.listeners

import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.util.concurrent.CopyOnWriteArrayList
import javax.imageio.ImageIO

import com.github.sarxos.webcam.{WebcamEvent, WebcamListener}
import org.slf4j.LoggerFactory
import org.springframework.web.socket.{BinaryMessage, CloseStatus, WebSocketSession}

import scala.util.{Failure, Success, Try}

/**
  * Class that implements [[com.github.sarxos.webcam.WebcamListener WebcamListener]] in order to react to webcam events,
  * sending the frames to each active [[org.springframework.web.socket.WebSocketSession WebSocketSession]].
  *
  * @author Diego Casella
  */
class WebcamListenerManager extends WebcamListener {

  private val LOGGER = LoggerFactory.getLogger(classOf[WebcamListener])

  private val managedSessions: CopyOnWriteArrayList[WebSocketSession] = new CopyOnWriteArrayList[WebSocketSession]()

  final def addSession(webSocketSession: WebSocketSession): Unit = {
    LOGGER.info(s"adding a new listener for session ${webSocketSession.getId}")
    managedSessions.add(webSocketSession)
  }

  final def removeSession(webSocketSession: WebSocketSession): Unit = {
    LOGGER.info(s"removing listener for session ${webSocketSession.getId}")
    managedSessions.remove(webSocketSession)
  }

  final def sessionCount(): Int = managedSessions.size()

  override def webcamImageObtained(webcamEvent: WebcamEvent): Unit = sendImage(webcamEvent)

  override def webcamOpen(webcamEvent: WebcamEvent): Unit = sendImage(webcamEvent)

  override def webcamClosed(webcamEvent: WebcamEvent): Unit = managedSessions.forEach(_.close(CloseStatus.NORMAL))

  override def webcamDisposed(webcamEvent: WebcamEvent): Unit = managedSessions.forEach(_.close(CloseStatus.SERVER_ERROR))

  private def sendImage(webcamEvent: WebcamEvent): Unit = {
    eventToBufferedData(webcamEvent) match {
      case Some(data) =>
        managedSessions.forEach(webSocketSession => {
          if (webSocketSession.isOpen) {
            webSocketSession.sendMessage(new BinaryMessage(data))
          }
        })
      case _ =>
    }
  }

  private def eventToBufferedData(webcamEvent: WebcamEvent): Option[ByteBuffer] = {
    Option(webcamEvent.getImage) match {
      case Some(image) =>
        val out = new ByteArrayOutputStream
        Try(ImageIO.write(image, "png", out)) match {
          case Success(_) => Some(ByteBuffer.wrap(out.toByteArray))
          case Failure(_) => None
        }

      case _ => None
    }
  }
}
