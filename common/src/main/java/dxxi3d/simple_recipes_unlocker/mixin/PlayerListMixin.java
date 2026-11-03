package dxxi3d.simple_recipes_unlocker.mixin;

import dxxi3d.simple_recipes_unlocker.ModLogger;
import net.minecraft.network.Connection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.List;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {

    @Shadow
    @Final
    private MinecraftServer server;

    @Shadow
    @Final
    private List<ServerPlayer> players;

    @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    private void unlockRecipesOnPlayerJoin(Connection connection, ServerPlayer player, CommonListenerCookie cookie, CallbackInfo ci) {
        unlockAllRecipesFor(player);
    }

    @Inject(method = "reloadResources", at = @At("TAIL"))
    private void unlockRecipesOnDatapackReload(CallbackInfo ci) {
        unlockAllRecipesForAllPlayers();
    }

    private void unlockAllRecipesFor(ServerPlayer player) {
        if (player == null || player.connection == null || this.server == null) {
            return;
        }

        RecipeManager recipeManager = this.server.getRecipeManager();
        if (recipeManager == null) {
            return;
        }

        Collection<RecipeHolder<?>> recipes = recipeManager.getRecipes();
        if (recipes.isEmpty()) {
            return;
        }

        int awarded = player.awardRecipes(recipes);
        if (awarded > 0) {
            ModLogger.debug("Awarded {} recipes to {}", awarded, player.getPlainTextName());
        }
    }

    private void unlockAllRecipesForAllPlayers() {
        if (this.server == null || this.players == null || this.players.isEmpty()) {
            return;
        }

        RecipeManager recipeManager = this.server.getRecipeManager();
        if (recipeManager == null) {
            return;
        }

        Collection<RecipeHolder<?>> recipes = recipeManager.getRecipes();
        if (recipes.isEmpty()) {
            return;
        }

        int totalAwarded = 0;
        for (ServerPlayer player : this.players) {
            if (player != null && player.connection != null) {
                totalAwarded += player.awardRecipes(recipes);
            }
        }

        if (totalAwarded > 0) {
            ModLogger.info("Data pack reload complete: awarded {} recipes across players.", totalAwarded);
        }
    }
}
