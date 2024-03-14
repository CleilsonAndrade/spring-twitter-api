package br.com.cleilsonandrade.springtwitterapi.config;

import java.util.Set;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import br.com.cleilsonandrade.springtwitterapi.entities.Role;
import br.com.cleilsonandrade.springtwitterapi.entities.User;
import br.com.cleilsonandrade.springtwitterapi.repositories.RoleRepository;
import br.com.cleilsonandrade.springtwitterapi.repositories.UserRepository;

@Configuration
public class AdminUserConfig {
  private RoleRepository roleRepository;
  private UserRepository userRepository;
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  public AdminUserConfig(RoleRepository roleRepository, UserRepository userRepository,
      BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.roleRepository = roleRepository;
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Override
  @Transactional
  public void run(String... args) {
    var roleAdmin = roleRepository.findByName(Role.Values.ADMIN.name());
    var userAdmin = userRepository.findByUsername("ADMIN");

    userAdmin.ifPresentOrElse(
        user -> {
          System.out.println("admin already exists");
        }, () -> {
          var user = new User();
          user.setUsername("ADMIN");
          user.setPassword(bCryptPasswordEncoder.encode("123"));
          user.setRoles(Set.of(roleAdmin));
          userRepository.save(user);
        });
  }
}
