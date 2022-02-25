# DrugStoreSpringBootApp
Java SpringBoot Hibernate Drug Store Web App with generating PDF Invoice From HTML Template
Project for my IT studies - subject: Programming Platforms (Java, MVC, Spring, Hibernate).

 ### App deployed on Heroku:
 https://springdrugstore.herokuapp.com/ (site might take more than 30 seconds to laod for the first time)
 1. Log in as user:\
  Login: `user`\
  Password: `user`\
  Or as superuser:\
  Login: `superuser`\
  Password: `super`\
  Or create new account (email verification needed).
  2. Add item to cart -> Confirm -> (if personal data not present) Enter random personal info, and go back to cart -> Pay button generates PDF Invoice from template and sends it via email

### Built With
* [Spring Boot](https://spring.io/projects/spring-boot)
* [Thymeleaf](https://www.thymeleaf.org/)

<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE` for more information.

<!-- ACKNOWLEDGMENTS -->
## Acknowledgments

* [imgscalr-lib](https://mvnrepository.com/artifact/org.imgscalr/imgscalr-lib/4.2)
* [flying-saucer-pdf](https://github.com/flyingsaucerproject/flyingsaucer)
