package xyz.company.bank;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import xyz.company.bank.service.ExecutionService;

/**
 * Starts the small spring app. Only using Spring-Context for dependency injection
 * and easy testing of the IOC pattern.
 */
public class Start {

    public static void main(String[] args) {
        // Initialize the spring context, grab our main service bean.
        final var applicationContext = new AnnotationConfigApplicationContext();
        // Scan for services
        applicationContext.scan(ExecutionService.class.getPackage().getName());
        applicationContext.refresh();

        final ExecutionService executionService = applicationContext.getBean(ExecutionService.class);
        // Start our main input loop.
        executionService.initAndRead(true);

        applicationContext.close();
    }
}
