package code.cli;

import code.enums.UserRole;
import code.model.User;

/**
 * Factory abstraction that supplies menus per role (SOLID - OCP/DIP).
 */
public interface RoleMenuFactory {

    MenuBase createMenu(User user);

    String getMenuName(UserRole role);
}
