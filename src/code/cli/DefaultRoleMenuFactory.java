package code.cli;

import code.enums.UserRole;
import code.filter.FilterStateManager;
import code.filter.OpportunityFilterService;
import code.model.User;
import code.repository.ApplicationRepository;
import code.repository.InternshipRepository;
import code.repository.RegistrationRequestRepository;
import code.repository.UserRepository;
import code.repository.WithdrawalRequestRepository;
import code.service.InternshipService;
import code.service.StudentApplicationService;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Provides menu instances per role without hard-coding creation logic in MainMenu (SOLID - OCP).
 */
public class DefaultRoleMenuFactory implements RoleMenuFactory {

    private final Map<UserRole, Function<User, MenuBase>> menuCreators = new EnumMap<>(UserRole.class);
    private final Map<UserRole, String> menuNames = new EnumMap<>(UserRole.class);

    public DefaultRoleMenuFactory(
            UserRepository userRepository,
            InternshipRepository internshipRepository,
            ApplicationRepository applicationRepository,
            RegistrationRequestRepository registrationRequestRepository,
            WithdrawalRequestRepository withdrawalRequestRepository,
            InternshipService internshipService,
            StudentApplicationService studentApplicationService,
            OpportunityFilterService opportunityFilterService,
            FilterStateManager filterStateManager) {

        menuCreators.put(UserRole.STUDENT, user -> new StudentMenu(
                userRepository,
                internshipRepository,
                applicationRepository,
                registrationRequestRepository,
                withdrawalRequestRepository,
                internshipService,
                opportunityFilterService,
                filterStateManager,
                studentApplicationService,
                user));

        menuCreators.put(UserRole.COMPANY_REPRESENTATIVE, user -> new CompanyRepresentativeMenu(
                userRepository,
                internshipRepository,
                applicationRepository,
                registrationRequestRepository,
                withdrawalRequestRepository,
                internshipService,
                opportunityFilterService,
                filterStateManager,
                user));

        menuCreators.put(UserRole.CAREER_CENTER_STAFF, user -> new CareerCenterStaffMenu(
                userRepository,
                internshipRepository,
                applicationRepository,
                registrationRequestRepository,
                withdrawalRequestRepository,
                internshipService,
                opportunityFilterService,
                filterStateManager,
                user));

        menuNames.put(UserRole.STUDENT, "Student Menu");
        menuNames.put(UserRole.COMPANY_REPRESENTATIVE, "Company Representative Menu");
        menuNames.put(UserRole.CAREER_CENTER_STAFF, "Career Center Staff Menu");
    }

    @Override
    public MenuBase createMenu(User user) {
        Function<User, MenuBase> creator = menuCreators.get(user.getRole());
        if (creator == null) {
            return null;
        }
        return creator.apply(user);
    }

    @Override
    public String getMenuName(UserRole role) {
        return menuNames.getOrDefault(role, "Role Menu");
    }
}
