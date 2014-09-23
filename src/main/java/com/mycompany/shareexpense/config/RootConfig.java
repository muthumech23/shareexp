/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.shareexpense.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

/**
 *
 * @author Muthukumaran Swaminathan
 */

@Configuration
@Import(DatabaseConfig.class)
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"com.mycompany.shareexpense.service", "com.mycompany.shareexpense.util", "com.mycompany.shareexpense.repository"})
public class RootConfig {
    
}
