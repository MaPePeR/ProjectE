package moze_intel.projecte.gameObjs.items.armor;

import com.google.common.collect.Multimap;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import moze_intel.projecte.handlers.PlayerChecks;
import moze_intel.projecte.utils.ChatHelper;
import moze_intel.projecte.utils.ClientKeyHelper;
import moze_intel.projecte.utils.EnumArmorType;
import moze_intel.projecte.utils.PEKeybind;
import moze_intel.projecte.utils.PlayerHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class GemFeet extends GemArmorBase
{
    public GemFeet()
    {
        super(EnumArmorType.FEET);
    }

    public static boolean isStepAssistEnabled(ItemStack boots)
    {
        return !boots.hasTagCompound() || !boots.stackTagCompound.hasKey("StepAssist") || boots.stackTagCompound.getBoolean("StepAssist");

    }

    public static void toggleStepAssist(ItemStack boots, EntityPlayer player)
    {
        if (!boots.hasTagCompound())
        {
            boots.setTagCompound(new NBTTagCompound());
        }

        boolean value;

        if (boots.stackTagCompound.hasKey("StepAssist"))
        {
            boots.stackTagCompound.setBoolean("StepAssist", !boots.stackTagCompound.getBoolean("StepAssist"));
            value = boots.stackTagCompound.getBoolean("StepAssist");
        }
        else
        {
            boots.stackTagCompound.setBoolean("StepAssist", false);
            value = false;
        }

        EnumChatFormatting e = value ? EnumChatFormatting.GREEN : EnumChatFormatting.RED;
        String s = value ? "pe.gem.enabled" : "pe.gem.disabled";
        player.addChatMessage(new ChatComponentTranslation("pe.gem.stepassist_tooltip").appendText(" ")
                .appendSibling(ChatHelper.modifyColor(new ChatComponentTranslation(s), e)));
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack stack)
    {
        if (!world.isRemote)
        {
            EntityPlayerMP playerMP = ((EntityPlayerMP) player);
            playerMP.fallDistance = 0;

            if (isStepAssistEnabled(stack))
            {
                if (playerMP.stepHeight != 1.0f)
                {
                    playerMP.stepHeight = 1.0f;
                    PlayerHelper.updateClientStepHeight(playerMP, 1.0F);
                    PlayerChecks.addPlayerStepChecks(playerMP);
                }
            }

            if (!player.capabilities.allowFlying)
            {
                PlayerHelper.enableFlight(playerMP);
            }
        }
        else
        {
            if (!player.onGround)
            {
                if (player.motionY <= 0)
                {
                    player.motionY *= 0.90;
                }
                if (!player.capabilities.isFlying)
                {
                    player.motionX *= 1.1;
                    player.motionZ *= 1.1;
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List tooltips, boolean unused)
    {
        tooltips.add(StatCollector.translateToLocal("pe.gem.feet.lorename"));
        tooltips.add(String.format(
                StatCollector.translateToLocal("pe.gem.stepassist.prompt"), ClientKeyHelper.getKeyName(PEKeybind.ARMOR_TOGGLE)));

        EnumChatFormatting e = isStepAssistEnabled(stack) ? EnumChatFormatting.GREEN : EnumChatFormatting.RED;
        String s = isStepAssistEnabled(stack) ? "pe.gem.enabled" : "pe.gem.disabled";
        tooltips.add(StatCollector.translateToLocal("pe.gem.stepassist_tooltip") + " "
                + e + StatCollector.translateToLocal(s));
    }

    @Override
    public Multimap getAttributeModifiers(ItemStack stack)
    {
        Multimap multimap = super.getAttributeModifiers(stack);
        multimap.put(SharedMonsterAttributes.movementSpeed.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Armor modifier", 1.0, 2));
        return multimap;
    }
}
