package com.alexviana.alexvianaprojeto.config;



import jakarta.faces.webapp.FacesServlet;
import jakarta.servlet.ServletContext;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer; // <-- ESTE IMPORT É CRUCIAL
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// ... (o restante da classe FacesConfig)
// Classe de configuração para integrar JSF com Spring Boot
@Configuration
public class FacesConfig {

    // Bean para registrar o FacesServlet
    @Bean
    public ServletRegistrationBean<FacesServlet> facesServletRegistration() {
        // Cria uma nova instância de ServletRegistrationBean para o FacesServlet
        ServletRegistrationBean<FacesServlet> registration = new ServletRegistrationBean<>(
                new FacesServlet(), "*.xhtml"); // Mapeia o FacesServlet para todas as requisições .xhtml
        
        // Configura a ordem de inicialização do servlet
        registration.setLoadOnStartup(1); // Garante que o FacesServlet seja inicializado na inicialização da aplicação

        return registration;
    }

    // Bean para configurar o FacesServlet em um contexto de servlet não-tradicional (como o embutido do Spring Boot)
    // Isso é importante para que o JSF saiba onde encontrar seus recursos e configure o ciclo de vida corretamente.
    @Bean
    public ServletContextInitializer contextInitializer() {
        return new ServletContextInitializer() {
            @Override
            public void onStartup(ServletContext servletContext) {
                // Configura o projeto como "PROJECT_STAGE" de desenvolvimento para logs detalhados
                servletContext.setInitParameter("jakarta.faces.PROJECT_STAGE", "Development");
                // Opcional: Desabilita a verificação de estado para evitar alguns problemas em certos setups
                // servletContext.setInitParameter("jakarta.faces.STATE_SAVING_METHOD", "client");
            }
        };
    }
}