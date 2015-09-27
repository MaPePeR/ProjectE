package moze_intel.projecte.api.proxy;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.Map;

public interface IRecipeProxy
{
	/**
	 * Add a Recipe to the EMC Calculation.
	 * Has to be called after {@code FMLInitializationEvent} and before {@code FMLServerStartingEvent}.
	 *
	 * You can use the following things for the {@code output}-Parameter and the keys in the {@code ingredients} Map:
	 * <ul>
	 *     <li>{@link ItemStack} - The ItemId and Metadata will be used to identify this ItemStack (May contain a {@code Block} or {@code Item})</li>
	 *     <li>{@link FluidStack} - {@link FluidStack#getFluid()} and {@link Fluid#getName()} will be used to identify this Fluid.</li>
	 *     <li>{@link String} - will be interpreted as an OreDictionary name.</li>
	 *     <li>{@link Object} - (No subclasses of {@code Object} - only {@code Object}!) can be used as a intermediate fake object for complex recipes.</li>
	 * </ul>
	 * All {@code Object}s will be assumed to be a single instance. No stacksize will be used.
	 *
	 * Use the {@code amount} parameter to specify how many {@code output}s are created.
	 * Use the key in the {@code ingredients}-Mao to specify how much of an ingredient is required.
	 * (Use Millibuckets for Fluids)
	 *
	 * @param amount
	 * @param output
	 * @param ingredients
	 */
	void addRecipe(int amount, Object output, Map<Object, Integer> ingredients);
}
