package demoprojects.demo.service.impl.user;

import demoprojects.demo.dao.models.entities.Role;
import demoprojects.demo.dao.models.entities.User;
import demoprojects.demo.dao.repositories.user.RoleRepository;
import demoprojects.demo.service.interfaces.user.RoleService;
import demoprojects.demo.service.interfaces.user.UserService;
import demoprojects.demo.service.models.view.RoleViewServiceModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final ModelMapper mapper;


    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper mapper) {
        this.roleRepository = roleRepository;
        this.mapper = mapper;

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

    @Override
    public List<RoleViewServiceModel> listAllRoles() {
        return this.roleRepository
                .findAll()
                .stream()
                .filter(role -> !role.getAuthority().equals("ROLE_ROOT"))
                .map(role -> this.mapper.map(role, RoleViewServiceModel.class))
                .collect(Collectors.toList());
    }


}
