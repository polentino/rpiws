package org.polentino911.rpiws.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.{ResourceHandlerRegistry, ViewControllerRegistry, WebMvcConfigurerAdapter}


/**
  * Configuration class that binds view controllers with their corresponding view names, and the resources handler.
  *
  * @author Diego Casella
  */
@Configuration
class ClientWebConfig extends WebMvcConfigurerAdapter {

  override def addViewControllers(registry: ViewControllerRegistry): Unit = {
    super.addViewControllers(registry)
    registry.addViewController("/").setViewName("main_page")
    registry.addViewController("/login").setViewName("login")
  }

  override def addResourceHandlers(registry: ResourceHandlerRegistry): Unit = {
    registry.addResourceHandler("/resources/**").addResourceLocations("/resources/")
  }
}
