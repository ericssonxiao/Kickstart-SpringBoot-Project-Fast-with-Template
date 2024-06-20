package com.eric.demo.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;


@EnableWs
@Configuration
public class SoapConfig extends WsConfigurerAdapter{
    
    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext){
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(servlet, "/service/*");
    }

    @Bean(name = "userInfoWsdl")
    public DefaultWsdl11Definition userWsdl11Definition(XsdSchema userSchema){
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("UserInfoPort");
        wsdl11Definition.setLocationUri("/service/user-info");
        wsdl11Definition.setTargetNamespace("http://www.eric.com/xml/todoapp");
        wsdl11Definition.setSchema(userSchema);
        return wsdl11Definition;
    }

    @Bean(name = "todoInfoWsdl")
    public DefaultWsdl11Definition todoWsdl11Definition(XsdSchema todoSchema){
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("TodoInfoPort");
        wsdl11Definition.setLocationUri("/service/todo-info");
        wsdl11Definition.setTargetNamespace("http://www.eric.com/xml/todoapp");
        wsdl11Definition.setSchema(todoSchema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema userSchema(){
        return new SimpleXsdSchema(new ClassPathResource("user.xsd"));
    }

    @Bean
    public XsdSchema todoSchema(){
        return new SimpleXsdSchema(new ClassPathResource("todo.xsd"));
    }





}
