package app;

import config.ClientConfiguration;
import config.ConfigurationReader;
import dto.*;
import dto.commdata.NodeCommandDataTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import service.NodeService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@ComponentScan(basePackages = {"config", "service", "web"})
public class HvssSlaveEntryPoint {

    @Autowired
    NodeService nodeService;

    public static void main(String[] args) throws Exception{
        SpringApplication.run(HvssSlaveEntryPoint.class, args);
    }
}
