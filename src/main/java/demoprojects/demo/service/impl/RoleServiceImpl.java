package demoprojects.demo.service.impl;

import demoprojects.demo.dao.models.entities.Role;
import demoprojects.demo.dao.repositories.RoleRepository;
import demoprojects.demo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void seedRolesInDb() {
        if (this.roleRepository.count() == 0) {
            this.roleRepository.saveAndFlush(new Role("ROLE_USER"));
            this.roleRepository.saveAndFlush(new Role("ROLE_ADMIN"));
            this.roleRepository.saveAndFlush(new Role("ROLE_ROOT"));
        }
    }

    @Override
    public Set<Role> findAll() {
        return new HashSet<>(this.roleRepository.findAll());
    }

    @Override
    public Role findByAuthority(String authority) {
        return this.roleRepository.findByAndAuthority(authority);
    }

    @Override
    public void addNewRole() {
        this.roleRepository.saveAndFlush(new Role("ROLE_MODERATOR"));
        this.roleRepository.saveAndFlush(new Role("ROLE_BLOG-KING"));
        this.roleRepository.saveAndFlush(new Role("ROLE_MICHAEL-SCOTT"));
        this.roleRepository.saveAndFlush(new Role("ROLE_SALESMAN"));
    }
}
