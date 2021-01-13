package com.sda.rideshare;

import com.sda.rideshare.configs.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

public class App 
{
    public static void main( String[] args )
    {
        SpringApplication.run(AppConfig.class);
    }


}
