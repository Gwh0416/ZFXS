package org.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Connector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.SpringVersion;
import org.springframework.scheduling.annotation.EnableAsync;

@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableAsync
public class ZFXSApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZFXSApplication.class,args);
        System.out.println("启动成功！");
        String versionSpring = SpringVersion.getVersion();

        String versionSpringBoot = SpringBootVersion.getVersion();

        System.out.println("Spring Version：" + versionSpring);

        System.out.println("SpringBoot Version：" + versionSpringBoot);



    }
    static {
        System.setProperty("druid.mysql.usePingMethod","false");
    }

}