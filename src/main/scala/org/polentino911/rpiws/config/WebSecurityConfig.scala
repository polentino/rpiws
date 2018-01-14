package org.polentino911.rpiws.config

import org.springframework.beans.factory.annotation.{Autowired, Value}
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.{EnableWebSecurity, WebSecurityConfigurerAdapter}

/**
  * Configuration class that describes who is allowed to access a given URL, and adds one user.
  *
  * @author Diego Casella
  */
@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Value("${auth.user.name}")
  private val username: String = null

  @Value("${auth.user.password}")
  private val password: String = null

  @throws[Exception]
  override protected def configure(http: HttpSecurity): Unit = {
    http
      .authorizeRequests()
      .antMatchers("/resources/css/**", "/resources/js/**").permitAll()
      .antMatchers("/", "/main_page").authenticated()
      .antMatchers("/webcam").authenticated()
      .and()
      .formLogin().loginPage("/login").permitAll()
      .and()
      .logout.logoutSuccessUrl("/login?logout")
  }

  @Autowired
  @throws[Exception]
  def configureGlobal(auth: AuthenticationManagerBuilder): Unit = {
    auth.inMemoryAuthentication.withUser(username).password(password).roles("USER")
  }
}
