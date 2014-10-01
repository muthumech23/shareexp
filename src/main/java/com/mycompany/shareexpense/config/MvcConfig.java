package com.mycompany.shareexpense.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.EnableWebMvc;


/**
 * DOCUMENT ME!
 *
 * @author Muthukumaran Swaminathan
 * @version $Revision$
  */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages =  {
    "com.mycompany.shareexpense.controller"}
)
public class MvcConfig {
}
