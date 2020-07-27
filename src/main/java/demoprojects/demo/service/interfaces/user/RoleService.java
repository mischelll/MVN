package demoprojects.demo.service.interfaces.user;

import demoprojects.demo.dao.models.entities.Role;
import demoprojects.demo.service.models.view.RoleViewServiceModel;

import java.util.List;
import java.util.Set;

public interface RoleService {
    void seedRolesInDb();
    Set<Role> findAll();
    Role findByAuthority(String auth);
    void addNewRole();
    List<RoleViewServiceModel> listAllRoles();
}
