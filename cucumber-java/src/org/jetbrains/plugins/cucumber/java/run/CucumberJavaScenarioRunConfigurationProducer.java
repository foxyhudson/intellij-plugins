package org.jetbrains.plugins.cucumber.java.run;

import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.cucumber.psi.GherkinScenario;
import org.jetbrains.plugins.cucumber.psi.GherkinScenarioOutline;
import org.jetbrains.plugins.cucumber.psi.GherkinStepsHolder;

/**
 * User: Andrey.Vokin
 * Date: 10/22/12
 */
public class CucumberJavaScenarioRunConfigurationProducer extends CucumberJavaFeatureRunConfigurationProducer {
  @Override
  protected String getNameFilter(@NotNull ConfigurationContext context) {
    final PsiElement sourceElement = context.getPsiLocation();

    final GherkinStepsHolder scenario = PsiTreeUtil.getParentOfType(sourceElement, GherkinScenario.class, GherkinScenarioOutline.class);
    if (scenario != null) {
      final String scenarioName = scenario.getScenarioName().replaceAll("\\\"", "\\\\\"").replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)");
      return scenarioName;
    }

    return super.getNameFilter(context);
  }

  @Nullable
  @Override
  protected VirtualFile getFileToRun(ConfigurationContext context) {
    final PsiElement element = context.getPsiLocation();
    final GherkinStepsHolder scenario = PsiTreeUtil.getParentOfType(element, GherkinScenario.class, GherkinScenarioOutline.class);
    final PsiFile psiFile = scenario != null ? scenario.getContainingFile() : null;
    return psiFile != null ? psiFile.getVirtualFile() : null;
  }

  @Override
  protected String getConfigurationName(@NotNull final ConfigurationContext context) {
    final PsiElement sourceElement = context.getPsiLocation();
    final GherkinStepsHolder scenario = PsiTreeUtil.getParentOfType(sourceElement, GherkinScenario.class, GherkinScenarioOutline.class);

    return "Scenario: " + (scenario != null ? scenario.getScenarioName() : "");
  }
}
