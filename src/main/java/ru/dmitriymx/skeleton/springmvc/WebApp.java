package ru.dmitriymx.skeleton.springmvc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Slf4jLog;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.net.InetSocketAddress;

@RequiredArgsConstructor
@Slf4j
public class WebApp {
    private final String host;
    private final int port;

    /**
     * Данный класс является файлом настройки контекста для Spring
     * однако, т.к. мы никакие бины не объявляем, то класс пустует
     */
    @Configuration
    @EnableWebMvc
    public static class SpringConfigMVC extends WebMvcConfigurerAdapter {
    }

    /**
     * Создаем Spring-контекст
     *
     * @return {@link WebApplicationContext}
     */
    private WebApplicationContext getWebApplicationContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(SpringConfigMVC.class);
        context.register(WebAppController.class);
        // или
        // context.setConfigLocation("ru.dmitriymx.skeleton.springmvc");
        // в этом случае будет просканирован весь пакет на @Configuration
        return context;
    }

    /**
     * Подготавливаем обработчик запросов
     *
     * @param context Spring-контекст веб-приложения {@link WebApplicationContext}
     * @return {@link ServletContextHandler}
     */
    private ServletContextHandler getServletContextHandler(WebApplicationContext context) {
        ServletContextHandler contextHandler = new ServletContextHandler();
        contextHandler.setErrorHandler(null);
        contextHandler.setContextPath("/");
        contextHandler.addServlet(new ServletHolder(new DispatcherServlet(context)), "/*");
        contextHandler.addEventListener(new ContextLoaderListener(context));
        return contextHandler;
    }

    /**
     * Запуск встроенного Jetty веб-сервера
     */
    private void start() {
        Log.setLog(new Slf4jLog("Jetty.Logger"));
        Server server = new Server(new InetSocketAddress(host, port));
        server.setHandler(getServletContextHandler(getWebApplicationContext()));
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            log.error("Error start server", e);
        }
    }

    public static void main(String[] args) {
        final String host = System.getProperty("host", "127.0.0.1");
        final int port = Integer.parseInt(System.getProperty("port", "8080"));

        log.info("Web app listen: {}:{}", host, port);
        WebApp app = new WebApp(host, port);
        app.start();
    }
}
