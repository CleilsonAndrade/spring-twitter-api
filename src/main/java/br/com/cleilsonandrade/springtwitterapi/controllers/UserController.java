package br.com.cleilsonandrade.springtwitterapi.controllers;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.cleilsonandrade.springtwitterapi.controllers.dtos.CreateUserDto;
import br.com.cleilsonandrade.springtwitterapi.entities.Role;
import br.com.cleilsonandrade.springtwitterapi.entities.User;
import br.com.cleilsonandrade.springtwitterapi.repositories.RoleRepository;
import br.com.cleilsonandrade.springtwitterapi.repositories.UserRepository;

@RestController
@RequestMapping
public class UserController {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public UserController(UserRepository userRepository, RoleRepository roleRepository,
      BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @PostMapping("/users")
  @Transactional
  public ResponseEntity<Void> newUser(@RequestBody CreateUserDto dto) {
    var basicRole = roleRepository.findByName(Role.Values.BASIC.name());

    var userFromDb = userRepository.findByUsername(dto.username());

    if (userFromDb.isPresent()) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    var user = new User();
    user.setUsername(dto.username());
    user.setPassword(bCryptPasswordEncoder.encode(dto.password()));
    user.setRoles(Set.of(basicRole));

    userRepository.save(user);

    return ResponseEntity.ok().build();
  }
}
