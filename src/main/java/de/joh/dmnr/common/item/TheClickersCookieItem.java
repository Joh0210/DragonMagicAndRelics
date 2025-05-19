package de.joh.dmnr.common.item;

import de.joh.dmnr.networking.ModMessages;
import de.joh.dmnr.networking.packet.SpawnRngParticleS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * Spawns a Cookie in 2 Blocks Radius when Clicked
 * <br>Is a Relict
 * @author Joh0210
 */
public class TheClickersCookieItem extends Item {
    public TheClickersCookieItem() {
        super(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.EPIC));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, @NotNull Player user, @NotNull InteractionHand hand) {
        InteractionResultHolder<ItemStack> ar = super.use(world, user, hand);
        world.playSeededSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 0.2F, 0.4F + (float)Math.random() * 0.7F, 2);

        if(!world.isClientSide() && world.getServer() != null) {
            if (user.isShiftKeyDown()) {
                user.getCooldowns().addCooldown(this, 5);

                ItemEntity item = new ItemEntity(world, user.getX(), user.getY(), user.getZ(), new ItemStack(Items.COOKIE));
                world.addFreshEntity(item);
            }
            else {
                user.getCooldowns().addCooldown(this, 2);
                Random random = new Random();


                //Random Pos:
                for(int i = 0; i < 6; i++){
                    int x = random.nextInt(5) - 2;
                    int y = random.nextInt(4) - 1;
                    int z = random.nextInt(5) - 2;

                    BlockPos pos = new BlockPos((int) user.getX()+x, (int) user.getY()+y,  (int)user.getZ()+z);

                    if (world.isEmptyBlock(pos)) {
                        ItemEntity item = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.COOKIE));
                        world.addFreshEntity(item);

                        for (ServerPlayer player : world.getServer().getPlayerList().getPlayers()) {
                            if (player.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 16 * 16) {
                                ModMessages.sendToPlayer(new SpawnRngParticleS2CPacket(pos), player);
                            }
                        }

                        return ar;  // Only Spawn 1
                    }
                }

                // tries to Force:
                for (int xOffset = -2; xOffset <= 2; xOffset++) {
                    for (int yOffset = -2; yOffset <= 2; yOffset++) {
                        for (int zOffset = -1; zOffset <= 2; zOffset++) {
                            if (world.isEmptyBlock(new BlockPos((int) user.getX()+xOffset,  (int)user.getY()+yOffset, (int) user.getZ()+zOffset))) {
                                ItemEntity item = new ItemEntity(world, user.getX()+xOffset, user.getY()+yOffset, user.getZ()+zOffset, new ItemStack(Items.COOKIE));
                                world.addFreshEntity(item);
                                return ar;  // Only Spawn 1
                            }
                        }
                    }
                }
            }

        }

        return ar;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(@NotNull ItemStack itemStack){
        return true;
    }
}
