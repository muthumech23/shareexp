/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.shareexpense.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 *
 * @author Muthukumaran Swaminathan
 */
public class WebAppInitializer implements WebApplicationInitializer{

    @Override
    public void onStartup(ServletContext sc) throws ServletException {
        
        AnnotationConfigWebApplicationContext webApplicationContext = new AnnotationConfigWebApplicationContext();
        webApplicationContext.register(RootConfig.class);
        webApplicationContext.refresh();
        
        AnnotationConfigWebApplicationContext mvcWebApplicationContext = new AnnotationConfigWebApplicationContext();
        mvcWebApplicationContext.register(MvcConfig.class);
        
        sc.addListener(new ContextLoaderListener(webApplicationContext));
        sc.setInitParameter("defaultHtmlEscape", "true");
        
        ServletRegistration.Dynamic dispatcher = sc.addServlet("dispatcherServlet", new DispatcherServlet(mvcWebApplicationContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/api/*");
        
    }
    
}
