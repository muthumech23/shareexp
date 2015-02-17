package com.mycompany.shareexpense.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;


/**
 * DOCUMENT ME!
 *
 * @author Muthukumaran Swaminathan
 * @version $Revision$
 */
public class WebAppInitializer implements WebApplicationInitializer {
    /**
     * DOCUMENT ME!
     *
     * @param sc DOCUMENT ME!
     * @throws ServletException DOCUMENT ME!
     */
    @Override
    public void onStartup(ServletContext sc) throws ServletException {
        AnnotationConfigWebApplicationContext webApplicationContext = new AnnotationConfigWebApplicationContext();

        webApplicationContext.register(RootConfig.class);
        webApplicationContext.register(PropertiesConfig.class);
        webApplicationContext.refresh();

        AnnotationConfigWebApplicationContext mvcWebApplicationContext = new AnnotationConfigWebApplicationContext();

        mvcWebApplicationContext.register(MvcConfig.class);

        sc.addListener(new ContextLoaderListener(webApplicationContext));
        sc.setInitParameter("defaultHtmlEscape", "true");

        ServletRegistration.Dynamic dispatcher = sc.addServlet("dispatcherServlet",
                new DispatcherServlet(mvcWebApplicationContext));

        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/api/*");
    }
}
