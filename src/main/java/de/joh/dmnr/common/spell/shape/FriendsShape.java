package de.joh.dmnr.common.spell.shape;

import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import de.joh.dmnr.common.init.ItemInit;
import de.joh.dmnr.common.item.BraceletOfFriendshipItem;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * This Shape targets the Player, and all its friends (determent by the {@link BraceletOfFriendshipItem BraceletOfFriendship}) in the radius
 * @see BraceletOfFriendshipItem
 * @author Joh0210
 */
public class FriendsShape extends Shape {
    public FriendsShape(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.RANGE, 2.0F, 1.0F, 10.0F, 1.0F, 2.0F));
    }

    @Override
    public List<SpellTarget> Target(SpellSource source, Level level, IModifiedSpellPart<Shape> modifiedSpellPart, ISpellDefinition iSpellDefinition) {
        LivingEntity caster = source.getCaster();

        if(caster != null ){
            if(CuriosApi.getCuriosHelper().findFirstCurio(caster, ItemInit.BRACELET_OF_FRIENDSHIP.get()).isPresent()){
                Stream<Player> friendsSteam = CuriosApi.getCuriosHelper().findCurios(caster, ItemInit.BRACELET_OF_FRIENDSHIP.get()).stream().map(SlotResult::stack).filter(item -> item.getItem() instanceof BraceletOfFriendshipItem).flatMap(stack -> Arrays.stream(((BraceletOfFriendshipItem) stack.getItem()).getPlayerTargets(stack, level)));
                if(caster instanceof Player){
                    friendsSteam = Stream.concat(friendsSteam, Stream.of((Player) caster));
                }
                List<Player> friends = friendsSteam.toList();

                List<SpellTarget> inArea = level.getEntities(source.getCaster(), caster.getBoundingBox().inflate(modifiedSpellPart.getValue(Attribute.RANGE)), (entity) -> entity.isPickable() && entity.isAlive() && entity != source.getCaster()).stream().map(SpellTarget::new).toList();
                ArrayList<SpellTarget> targets = new ArrayList<>();
                for(SpellTarget target : inArea){
                    for(Player friend: friends){
                        if(friend == BraceletOfFriendshipItem.playerOrOwner(target.getEntity())){
                            targets.add(target);
                            break;
                        }
                    }
                }

                targets.add(new SpellTarget(caster));

                return targets;
            } else if(caster instanceof Player) {
                ((Player) caster).displayClientMessage(Component.translatable("dmnr.shapes.friends.no_bracelet_of_friendship.error"), true);
            }
        }

        return List.of(SpellTarget.NONE);
    }

    @Override
    public float initialComplexity() {
        return 20;
    }

    @Override
    public int requiredXPForRote() {
        return 200;
    }
}
