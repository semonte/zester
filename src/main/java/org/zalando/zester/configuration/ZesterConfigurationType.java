package org.zalando.zester.configuration;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.ConfigurationTypeUtil;
import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ZesterConfigurationType implements ConfigurationType {

    private static final String DISPLAY_NAME = "Zester";
    private static final String DESCRIPTION = "Zester executes mutation tests using PIT";
    private static final Icon ICON = IconLoader.getIcon("/icons/zester.png");
    private static final String ID = "Zester";

    private final ZesterConfigurationFactory zesterConfigurationFactory = new ZesterConfigurationFactory(this);

    public static ZesterConfigurationType getInstance() {
        return ConfigurationTypeUtil.findConfigurationType(ZesterConfigurationType.class);
    }

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @Override
    public String getConfigurationTypeDescription() {
        return DESCRIPTION;
    }

    @Override
    public Icon getIcon() {
        return ICON;
    }

    @NotNull
    @Override
    public String getId() {
        return ID;
    }

    @Override
    public ConfigurationFactory[] getConfigurationFactories() {
        return new ConfigurationFactory[]{zesterConfigurationFactory};
    }

    public ZesterConfigurationFactory getFactory() {
        return zesterConfigurationFactory;
    }
}
