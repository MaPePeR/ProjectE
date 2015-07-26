package moze_intel.projecte.emc.mappers.customConversions;

import moze_intel.projecte.emc.IMappingCollector;
import moze_intel.projecte.emc.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.IEMCMapper;
import moze_intel.projecte.emc.mappers.customConversions.json.ConversionGroup;
import moze_intel.projecte.emc.mappers.customConversions.json.CustomConversion;
import moze_intel.projecte.emc.mappers.customConversions.json.CustomConversionDeserializer;
import moze_intel.projecte.emc.mappers.customConversions.json.CustomConversionFile;
import moze_intel.projecte.emc.mappers.customConversions.json.FixedValues;
import moze_intel.projecte.emc.mappers.customConversions.json.FixedValuesDeserializer;
import moze_intel.projecte.utils.PELogger;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraftforge.common.config.Configuration;
import scala.Int;

import java.io.Reader;
import java.util.Map;

public class CustomConversionMapper implements IEMCMapper<NormalizedSimpleStack, Integer>
{
	@Override
	public String getName()
	{
		return "CustomConversionMapper";
	}

	@Override
	public String getDescription()
	{
		return "";
	}

	@Override
	public boolean isAvailable()
	{
		return true;
	}

	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Integer> mapper, Configuration config)
	{

	}

	public static void addMappingsFromFile(Reader json, IMappingCollector<NormalizedSimpleStack, Integer> mapper) {
		addMappingsFromFile(parseJson(json), mapper);
	}

	public static void addMappingsFromFile(CustomConversionFile file, IMappingCollector<NormalizedSimpleStack, Integer> mapper) {
		Map<String, NormalizedSimpleStack> fakes = Maps.newHashMap();
		//TODO implement buffered IMappingCollector
		for (Map.Entry<String, ConversionGroup> entry : file.groups.entrySet())
		{
			PELogger.logDebug(String.format("Adding conversions from group '%s' with comment '%s'", entry.getKey(), entry.getValue().comment));
			try
			{
				for (CustomConversion conversion : entry.getValue().conversions)
				{
					NormalizedSimpleStack output = getNSSfromJsonString(conversion.output, fakes);
					mapper.addConversionMultiple(conversion.count, output, convertToNSSMap(conversion.ingredients, fakes));
				}
			} catch (Exception e) {
				PELogger.logFatal(String.format("ERROR reading custom conversion from group %s!", entry.getKey()));
				e.printStackTrace();
			}
		}
	}


	private static NormalizedSimpleStack getNSSfromJsonString(String s, Map<String, NormalizedSimpleStack> fakes) throws Exception
	{
		if (s.startsWith("OD|")) {
			return NormalizedSimpleStack.forOreDictionary(s.substring(3));
		} else if (s.startsWith("FAKE|")) {
			String fakeIdentifier = s.substring(5);
			if (fakes.containsKey(fakeIdentifier)) {
				return fakes.get(fakeIdentifier);
			} else {
				NormalizedSimpleStack nssFake = NormalizedSimpleStack.createFake();
				fakes.put(fakeIdentifier, nssFake);
				return nssFake;
			}
		} else {
			return NormalizedSimpleStack.fromSerializedItem(s);
		}
	}

	private static<V> Map<NormalizedSimpleStack, V> convertToNSSMap(Map<String, V> m, Map<String, NormalizedSimpleStack> fakes) throws Exception{
		Map<NormalizedSimpleStack, V> out = Maps.newHashMap();
		for (Map.Entry<String, V> e: m.entrySet()) {
			NormalizedSimpleStack nssItem = getNSSfromJsonString(e.getKey(), fakes);
			out.put(nssItem, e.getValue());
		}
		return out;
	}

	public static CustomConversionFile parseJson(Reader json) {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(CustomConversion.class, new CustomConversionDeserializer());
		builder.registerTypeAdapter(FixedValues.class, new FixedValuesDeserializer());
		Gson gson = builder.create();
		return gson.fromJson(json, CustomConversionFile.class);
	}
}