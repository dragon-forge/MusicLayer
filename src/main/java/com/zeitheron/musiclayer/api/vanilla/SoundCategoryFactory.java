package com.zeitheron.musiclayer.api.vanilla;

import java.util.HashMap;
import java.util.Map;

import com.zeitheron.hammercore.utils.ReflectionUtil;

import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.util.EnumHelper;

public class SoundCategoryFactory
{
	private static final Map<String, SoundCategory> SOUND_CATEGORIES;
	
	static
	{
		Map<String, SoundCategory> scs = new HashMap<>();
		try
		{
			scs = Map.class.cast(ReflectionUtil.getField(SoundCategory.class, Map.class).get(null));
		} catch(ReflectiveOperationException e)
		{
			e.printStackTrace();
		}
		SOUND_CATEGORIES = scs;
	}
	
	public static SoundCategory create(String enumName, String categoryName)
	{
		SoundCategory whateverthefuckthisissupposedtobe = EnumHelper.addEnum(SoundCategory.class, enumName, new Class<?>[] { String.class }, categoryName.replaceAll("[:]", "."));
		SOUND_CATEGORIES.put(whateverthefuckthisissupposedtobe.getName(), whateverthefuckthisissupposedtobe);
		return whateverthefuckthisissupposedtobe;
	}
}