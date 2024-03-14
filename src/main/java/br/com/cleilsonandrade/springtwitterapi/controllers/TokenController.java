package br.com.cleilsonandrade.springtwitterapi.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cleilsonandrade.springtwitterapi.controllers.dtos.LoginRequestDto;
import br.com.cleilsonandrade.springtwitterapi.controllers.dtos.LoginResponseDto;
import br.com.cleilsonandrade.springtwitterapi.repositories.UserRepository;

@RestController
@RequestMapping()
public class TokenController {
  private final JwtEncoder jwtEncoder;
  private final UserRepository userRepository;
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  public TokenController(JwtEncoder jwtEncoder, UserRepository userRepository,
      BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.jwtEncoder = jwtEncoder;
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
    var user = userRepository.findByUsername(loginRequest.username());

    if (user.isEmpty() || !user.get().isLoginCorrect(loginRequest, bCryptPasswordEncoder)) {
      throw new BadCredentialsException("User or password is invalid");
    }
  }

}
