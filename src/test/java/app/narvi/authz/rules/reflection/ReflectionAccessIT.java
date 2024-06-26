package app.narvi.authz.rules.reflection;

import static app.narvi.authz.CrudAction.UPDATE;
import static app.narvi.authz.rules.TestExecutionSteps.TestSteps.THEN_;
import static app.narvi.authz.rules.TestExecutionSteps.TestSteps.WHEN_;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;

import app.narvi.authz.Permission;
import app.narvi.authz.PolicyEvaluator;
import app.narvi.authz.PolicyRule;
import app.narvi.authz.rules.Test;
import app.narvi.authz.rules.TestExecutionSteps.Scenario;
import app.narvi.authz.rules.sametenant.TenantAccessPermission;

@Order(2)
public class ReflectionAccessIT extends Test {

  @Scenario("Reflection to protego-core is forbidden by java modules system.")
  public void denyReflectionForProtegoCore() {
    Throwable exception = Assertions.assertThrows(InaccessibleObjectException.class,
        () -> {
//          GIVEN_("Using cutom policy rules configuration file.");
//          System.setProperty(FILE_NAME_PROPERTY,
//              "app/narvi/authz/rules/sametenant/allow-same-tenant-policy-rules.yaml");
//
//          AND_GIVEN_("The framework is initialized");
//          PolicyRulesProvider policyRulesProvider = new BasicPolicyRuleProvider();
//          PolicyEvaluator.registerProviders(policyRulesProvider);

          WHEN_("The framework is molested with reflection.");
          Field soleInstance = PolicyEvaluator.class.getDeclaredField("soleInstance");
          soleInstance.setAccessible(true);
          PolicyEvaluator policyEvaluator = (PolicyEvaluator) soleInstance.get(null);
          Field rulesCollection = PolicyEvaluator.class.getDeclaredField("rulesCollection");
          rulesCollection.setAccessible(true);
          List newPolyList = new ArrayList();
          newPolyList.add(new PolicyRule() {
            @Override
            public boolean hasPermisssion(Permission permission) {
              return true;
            }
          });
          rulesCollection.set(policyEvaluator, newPolyList);

          THEN_("The user has unwanted access.");
          Assertions.assertTrue(PolicyEvaluator.hasPermission(new TenantAccessPermission(UPDATE, null)));
        });
  }

  @Scenario("Reflection to java-util is forbidden by java modules system.")
  public void denyReflectionForJavaUtl() {
    Throwable exception = Assertions.assertThrows(InaccessibleObjectException.class,
        () -> {
//          GIVEN_("Using cutom policy rules configuration file.");
//          System.setProperty(FILE_NAME_PROPERTY, "app/narvi/authz/rules/sametenant/allow-same-tenant-policy-rules.yaml");
//
//          AND_GIVEN_("The framework is initialized");
//          PolicyRulesProvider policyRulesProvider = new BasicPolicyRuleProvider();
//          PolicyEvaluator.registerProviders(policyRulesProvider);

          WHEN_("The framework is molested with reflection.");
          Field soleInstance = PolicyEvaluator.class.getDeclaredField("soleInstance");
          soleInstance.setAccessible(true);
          PolicyEvaluator policyEvaluator = (PolicyEvaluator) soleInstance.get(null);
          Field rulesCollection = PolicyEvaluator.class.getDeclaredField("rulesCollection");
          rulesCollection.setAccessible(true);

          Class unmodifiableListClass = rulesCollection.get(policyEvaluator).getClass();
          Field ucl = unmodifiableListClass.getSuperclass().getDeclaredField("list");
          ucl.setAccessible(true);
          ucl.set(policyEvaluator, new ArrayList());
          List aList = (List) ucl.get(policyEvaluator);
          aList.clear();

          THEN_("The user has unwanted access.");
          Assertions.assertTrue(PolicyEvaluator.hasPermission(new TenantAccessPermission(UPDATE, null)));
        });
  }

}