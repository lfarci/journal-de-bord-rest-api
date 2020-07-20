package farci.logan.jdb;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;

@SpringBootApplication
public class JournalDeBordApplication {

	public static void main(String[] args) {
		SpringApplication.run(JournalDeBordApplication.class, args);
	}

}