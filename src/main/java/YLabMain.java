import infrastructure.configuration.LiquibaseMigration;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class YLabMain implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        new LiquibaseMigration().migrateDatabase();
    }
}