package demoprojects.demo.service.models.bind;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleChangeServiceModel {
    private Set<String> roles;
}
