package com.mycompany.shareexpense.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;


/**
 * DOCUMENT ME!
 *
 * @author Muthukumaran Swaminathan
 * @version $Revision$
  */
@Configuration
@Import(DatabaseConfig.class)
@EnableAspectJAutoProxy
@ComponentScan(basePackages =  {
    "com.mycompany.shareexpense.service", "com.mycompany.shareexpense.util", "com.mycompany.shareexpense.repository"}
)
public class RootConfig {
}
