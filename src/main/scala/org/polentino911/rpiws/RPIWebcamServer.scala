package org.polentino911.rpiws

//import org.apache.catalina.Context
//import org.apache.catalina.connector.Connector
//import org.apache.coyote.http11.Http11NioProtocol
//import org.apache.tomcat.util.descriptor.web.{SecurityCollection, SecurityConstraint}
import io.undertow.Undertow
import io.undertow.server.HttpServerExchange
import io.undertow.servlet.api._
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory
import org.springframework.context.annotation.Bean

/**
  * Nothing interesting here, just the application entry point
  */
object RPIWebcamServer extends App {
  SpringApplication.run(classOf[RPIWebcamServer])
}

/**
  * Configure Spring to redirect traffic from port 8080 t0 serverPort
  *
  * @author Diego Casella
  */
@SpringBootApplication(exclude = Array(
  classOf[DataSourceAutoConfiguration],
  classOf[JmxAutoConfiguration]
))
class RPIWebcamServer {

  @Value("${server.port}")
  private val serverPort: Int = 0

  @Bean
  def servletContainer: EmbeddedServletContainerFactory = {
    val undertow: UndertowEmbeddedServletContainerFactory = new UndertowEmbeddedServletContainerFactory
    undertow.addBuilderCustomizers((builder: Undertow.Builder) => builder.addHttpListener(8080, "0.0.0.0"))
    undertow.addDeploymentInfoCustomizers((deploymentInfo: DeploymentInfo) => {
      deploymentInfo.addSecurityConstraint(
        new SecurityConstraint()
          .addWebResourceCollection(
            new WebResourceCollection()
              .addUrlPattern("/*"))
          .setTransportGuaranteeType(TransportGuaranteeType.CONFIDENTIAL)
          .setEmptyRoleSemantic(SecurityInfo.EmptyRoleSemantic.PERMIT))
        .setConfidentialPortManager((exchange: HttpServerExchange) => serverPort)
    })
    undertow
  }

  //    val tomcat = new TomcatEmbeddedServletContainerFactory() {
  //
  //      override protected def postProcessContext(context: Context): Unit = {
  //        val securityConstraint = new SecurityConstraint
  //        securityConstraint.setUserConstraint("CONFIDENTIAL")
  //        val collection = new SecurityCollection
  //        collection.addPattern("/*")
  //        securityConstraint.addCollection(collection)
  //        context.addConstraint(securityConstraint)
  //      }
  //    }
  //    tomcat.addAdditionalTomcatConnectors(initiateHttpConnector)
  //    tomcat
  //  }
  //
  //  private def initiateHttpConnector = {
  //    val connector = new Connector(classOf[Http11NioProtocol].getName)
  //    connector.setScheme("http")
  //    connector.setPort(8080)
  //    connector.setSecure(false)
  //    connector.setRedirectPort(serverPort)
  //    connector
  //  }
}
