package app.narvi.authz.rules.doctor;

import static app.narvi.authz.rules.sametenant.User.Role.DOCTOR;

import app.narvi.authz.CrudAction;
import app.narvi.authz.Permission;
import app.narvi.authz.PolicyRule;
import app.narvi.authz.rules.sametenant.User;

public class AllowSameBranchPolicyRule implements PolicyRule {

  private static final ScopedValue<User> AUTHENTICATED_USER = ScopedValue.newInstance();

  @Override
  public boolean hasPermisssion(Permission permission) {
    if (!(permission instanceof AllowSameBranchPermission)) {
      return false;
    }
    //the doctor is the subject
    User authenticatedUser = AUTHENTICATED_USER.get();
    if (authenticatedUser.getRole() != DOCTOR) {
      return false;
    }
    // only read is allowed
    if (permission.getAction() != CrudAction.READ) {
      return false;
    }
//    // has an appointment today
//    if (!permission.getProtectedResource().getOwner().hasAppointemnt(authenticatedUser, LocalDate.now())) {
//      return WITHHOLD;
//    }
//    //the medical record belongs to the same medicine branch
//    if (authenticatedUser.asDoctor().getSpeciality() != permission.getProtectedResource().getSpeciality()) {
//      return WITHHOLD;
//    }
//    //the doctor is at work
//    if (!authenticatedUser.getTodaysWorkingHoursInterval().includes(Instant.now())) {
//      return WITHHOLD;
//    }
//    return PERMIT;
    return true;
  }


}
