package demoprojects.demo.service.interfaces.user;

import demoprojects.demo.dao.models.entities.Role;

import java.util.Set;

public interface RoleService {
    void seedRolesInDb();
    Set<Role> findAll();
    Role findByAuthority(String auth);
    void addNewRole();

}
