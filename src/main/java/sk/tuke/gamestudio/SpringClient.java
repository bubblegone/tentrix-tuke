package sk.tuke.gamestudio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.client.CommentServiceRESTClient;
import sk.tuke.gamestudio.service.client.RatingServiceRESTClient;
import sk.tuke.gamestudio.service.client.ScoreServiceRESTClient;
import sk.tuke.gamestudio.service.jpa.CommentServiceJPA;
import sk.tuke.gamestudio.service.jpa.RatingServiceJPA;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;
import sk.tuke.gamestudio.service.jpa.ScoreServiceJPA;
import sk.tuke.gamestudio.tentrix.consoleui.ConsoleUI;
import sk.tuke.gamestudio.tentrix.core.Field;

@SpringBootApplication
@Configuration
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
                                                    pattern = "sk.tuke.gamestudio.server.*"))
public class SpringClient {
    public static void main(String[] args) {
//        SpringApplication.run(SpringClient.class);
        new SpringApplicationBuilder(SpringClient.class).web(WebApplicationType.NONE).run(args);
    }

    @Bean
    public ConsoleUI consoleUI(Field field , ScoreService scoreService,
                               RatingService ratingService, CommentService commentService){
        return new ConsoleUI(field, scoreService, commentService, ratingService);
    }

    @Bean
    CommandLineRunner runner(ConsoleUI ui){
        return args -> ui.play();
    }

    @Bean
    public Field field(){
        return new Field();
    }

    @Bean
    public ScoreService scoreService() {
        return new ScoreServiceJPA();
//        return new ScoreServiceRESTClient();
    }

    @Bean
    public RatingService ratingService(){
        return new RatingServiceJPA();
//        return new RatingServiceRESTClient();
    }

    @Bean
    public CommentService commentService(){
        return new CommentServiceJPA();
//        return new CommentServiceRESTClient();
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
