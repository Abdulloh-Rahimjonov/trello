package uz.pdp.trello.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import uz.pdp.trello.entity.Role;
import uz.pdp.trello.entity.enums.Roles;
import uz.pdp.trello.repo.RoleRepository;
import uz.pdp.trello.repo.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@Component
public class Runner implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        List<Role> all = roleRepository.findAll();
        if (all.isEmpty()) {
            all.add(new Role(null, Roles.PROGRAMMER.name()));
            all.add(new Role(null, Roles.ADMIN.name()));
            all.add(new Role(null, Roles.MAINTAINER.name()));
            roleRepository.saveAll(all);
        }
        System.out.println(userRepository.findAll().size());
    }
}
