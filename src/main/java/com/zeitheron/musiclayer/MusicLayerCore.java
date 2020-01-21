package com.zeitheron.musiclayer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public class MusicLayerCore implements IFMLLoadingPlugin
{
	public static File mcPath;
	
	@Override
	public String[] getASMTransformerClass()
	{
		return new String[0];
	}
	
	@Override
	public String getModContainerClass()
	{
		return null;
	}
	
	@Override
	public String getSetupClass()
	{
		return null;
	}
	
	@Override
	public void injectData(Map<String, Object> data)
	{
		File coremodLocation = new File("" + data.get("coremodLocation"));
		File mcLocation = mcPath = new File("" + data.get("mcLocation"));
		File mods = null;
		
		if(mods == null && !(mods = new File(mcLocation, "mods")).isDirectory())
			mods = null;
		
		try
		{
			if(mods == null && coremodLocation.isFile() && !(mods = new File(coremodLocation.getParentFile(), "mods")).isDirectory())
				mods = null;
		} catch(Throwable err)
		{
		}
		
		if(mods == null)
			throw new RuntimeException("Unable to locate mods folder!");
		
		File SoundLib = new File(mods, "com.zeitheron.SoundLib.jar");
		if(SoundLib.isFile())
			SoundLib.delete();
		SoundLib = new File(mods, "com.zeitheron.SoundLib.jar");
		
		try(FileOutputStream fos = new FileOutputStream(SoundLib); InputStream in = MusicLayerCore.class.getResourceAsStream("/SoundLib.jar"))
		{
			byte[] buf = new byte[1024];
			int read = 0;
			while((read = in.read(buf)) > 0)
				fos.write(buf, 0, read);
		} catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}
}