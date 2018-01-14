package org.polentino911.rpiws.config

import org.polentino911.rpiws.handlers.WebcamWebSocketHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.{EnableWebSocket, WebSocketConfigurer, WebSocketHandlerRegistry}

/**
  * Configuration class that enables websockets to a specific URL.
  *
  * @author Diego Casella
  */
@Configuration
@EnableWebSocket
class WebSocketConfig extends WebSocketConfigurer {

  @Autowired
  private val webcamWebSocketHandler: WebcamWebSocketHandler = null

  override def registerWebSocketHandlers(webSocketHandlerRegistry: WebSocketHandlerRegistry): Unit = {
    Option(webcamWebSocketHandler).foreach(webSocketHandlerRegistry.addHandler(_, "/webcam"))
  }
}
