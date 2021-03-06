package br.com.pandox.lastcloud.boot;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.IOException;

public class ApplicationServer {
    private final String CONFIG_LOCATION = "br.com.pandox.lastcloud.boot";

    public static final int DEFAULT_PORT = 15081;
    public static final String CONTEXT_PATH = "/";

    public static final String MAPPING_URL = "/*";
    public static final String DEFAULT_PROFILE = "staging";

    private int jettyPort;

    private Server server;
    private String customProfile;

    public static void main(String[] args) throws Exception {
        ApplicationServer server = new ApplicationServer();
        server.addProfile("self-monitored");
        server.start(ApplicationServer.DEFAULT_PORT);
    }

    private void addProfile(String preload) {
        customProfile = preload;
    }

    public void start() throws Exception {
        start(0);
    }

    public void start(Integer port) throws Exception {
        QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setMaxThreads(100);
        server = new Server(threadPool);
        server.setHandler(getServletContextHandler(getContext()));
        server.setConnectors(new Connector[]{ getHttpConnector(port) });
        server.start();

        ServerConnector connector = (ServerConnector) server.getConnectors()[0];
        jettyPort = connector.getLocalPort();
    }

    public void stop() throws Exception {
        server.stop();
        server.destroy();
    }

    private Connector getHttpConnector(Integer port){
        HttpConnectionFactory httpConnectionFactory = new HttpConnectionFactory();
        ServerConnector http = new ServerConnector(server, httpConnectionFactory);
        http.setPort(port);
        http.setIdleTimeout(30000);

        return http;
    }

    private WebAppContext getServletContextHandler(WebApplicationContext context) throws
        IOException {
        WebAppContext webApp = new WebAppContext();
        webApp.setErrorHandler(null);
        webApp.setContextPath(CONTEXT_PATH);
        webApp.addServlet(new ServletHolder(new DispatcherServlet(context)), MAPPING_URL);
        webApp.addEventListener(new ContextLoaderListener(context));
        webApp.setResourceBase("src/main/webapp/");
        return webApp;
    }

    private WebApplicationContext getContext() throws IOException {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation(CONFIG_LOCATION);
        if(org.springframework.util.StringUtils.hasText(customProfile)) {
            context.getEnvironment().setDefaultProfiles(DEFAULT_PROFILE, customProfile);
        }else {
            context.getEnvironment().setDefaultProfiles(DEFAULT_PROFILE);
        }

        return context;
    }

    public int getJettyPort() {
        return jettyPort;
    }
}
