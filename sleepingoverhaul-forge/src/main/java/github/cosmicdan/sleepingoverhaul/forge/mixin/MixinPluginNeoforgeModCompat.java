package github.cosmicdan.sleepingoverhaul.forge.mixin;

import github.cosmicdan.sleepingoverhaul.SleepingOverhaul;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.LoadingModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * This plugin is to facilitate only applying mod-specific mixins if the modid exists (prevents annoying errors in log if a mod isn't present)
 */
public class MixinPluginNeoforgeModCompat implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {}

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (targetClassName.startsWith("com.illusivesoulworks.comforts.")) {
            if (LoadingModList.get().getModFileById("comforts") == null) {
                return false;
            } else
                SleepingOverhaul.LOGGER.info("Comforts detected; applying compatibility patches. Please report if you see a mixin error after this line, it means Comforts has updated and compat. may no longer work properly!");
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {
        return new ArrayList<>(0);
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
