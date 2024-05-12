package app.narvi.authz.rules.sametenant;

import app.narvi.authz.Permission;
import app.narvi.authz.PolicyRule;

public class PermitNothingPolicyRule implements PolicyRule {

  @Override
  public boolean hasPermisssion(Permission permission) {
    return false;
  }

}
