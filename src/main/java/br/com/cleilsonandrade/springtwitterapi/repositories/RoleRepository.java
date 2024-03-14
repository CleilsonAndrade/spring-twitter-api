package br.com.cleilsonandrade.springtwitterapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.cleilsonandrade.springtwitterapi.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
