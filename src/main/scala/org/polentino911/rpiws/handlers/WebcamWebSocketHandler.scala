package org.polentino911.rpiws.handlers

import com.github.sarxos.webcam.Webcam
import com.github.sarxos.webcam.ds.v4l4j.V4l4jDriver
import org.polentino911.rpiws.listeners.WebcamListenerManager
import org.springframework.stereotype.Component
import org.springframework.web.socket.handler.BinaryWebSocketHandler
import org.springframework.web.socket.{CloseStatus, WebSocketSession}

/**
  * Spring Bean that implements [[org.springframework.web.socket.handler.BinaryWebSocketHandler BinaryWebSocketHandler]]
  * in order to open/close the connection to the webcam as soon as a socket connection is estabilished/closed.
  *
  * @author Diego Casella
  */
@Component
class WebcamWebSocketHandler extends BinaryWebSocketHandler {

  // webcam setup
  Webcam.setDriver(new V4l4jDriver)
  private val webcam = Webcam.getDefault
  private val webcamListener = new WebcamListenerManager
  webcam.addWebcamListener(webcamListener)

  @throws[Exception]
  override def afterConnectionEstablished(session: WebSocketSession): Unit = {
    webcamListener.addSession(session)
    if (webcamListener.sessionCount() == 1) {
      webcam.open(true)
    }
  }

  @throws[Exception]
  override def afterConnectionClosed(session: WebSocketSession, status: CloseStatus): Unit = {
    webcamListener.removeSession(session)
    if (webcamListener.sessionCount() == 0) {
      webcam.close()
    }
  }
}
