package com.carnegie.springsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@SpringBootApplication
public class SpringSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityApplication.class, args);
	}

	@RestController
	class HttpController {
		@GetMapping("/public")
		public String publicRoute() {
			return "<h1>Public route, feel free to look arround!  🔓</h1>";
		}

		@GetMapping("/private")
		public String privateRoute(@AuthenticationPrincipal OidcUser user) {
			// OidcUser = Open ID Connect User
			// AuthenticationPrincipal, refers to the user info authenticated
			return String.format("""
  				<h1>Private route, only authorized person  🔐</h1>
  				""");

		}

		@GetMapping("/cookie")
		public String cookie(@AuthenticationPrincipal OidcUser user) {
			// OidcUser = Open ID Connect User
			// AuthenticationPrincipal, refers to the user info authenticated
			// curl http://localhost:8080/private --cookie "JSESSIONID=1AF658BAAC2D39E2F2EF5D5C9FB3BD6A" -v
			return String.format("""
  				<h1>Oauth2  🔐</h1>
  				<h3>Principal: %s</h3>
  				<h3>Email attribute: %s</h3>
  				<h3>Authorities: %s</h3>
  				<h3>JWT: %s</h3>
  				""", user, user.getAttribute("email"), user.getAuthorities(),user.getIdToken().getTokenValue());
		}

		@GetMapping("/jwt")
		public String jwt(@AuthenticationPrincipal Jwt jwt) {  // Deve ser adicionado o resource server
			// curl http://localhost:8080/jwt -H "Authorization: Bearer %TOKEN%" -v
			// Linux = // curl http://localhost:8080/jwt -H "Authorization: Bearer ${TOKEN}" -v

			return String.format("""
  				Principal %s <br>
  				Email attribuite: %s <br>
  				JWT: %s <br>
  				""",jwt.getClaims(), jwt.getClaim("email"),jwt.getTokenValue());

		}

	}

}
