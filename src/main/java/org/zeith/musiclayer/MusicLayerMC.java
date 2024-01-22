package org.zeith.musiclayer;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.event.GetAllRequiredApisEvent;
import lombok.var;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.zeith.musiclayer.api.MusicLayer;
import org.zeith.musiclayer.proxy.CommonProxy;

import java.io.*;
import java.net.MalformedURLException;

@Mod(
		modid = "musiclayer",
		name = "MusicLayer",
		version = "@VERSION@",
		dependencies = "required-after:hammercore",
		certificateFingerprint = "9f5e2a811a8332a842b34f6967b7db0ac4f24856",
		updateJSON = "https://api.modrinth.com/updates/4Y5irTB9/forge_updates.json"
)
public class MusicLayerMC
{
	@SidedProxy(clientSide = "org.zeith.musiclayer.proxy.ClientProxy", serverSide = "org.zeith.musiclayer.proxy.CommonProxy")
	public static CommonProxy proxy;
	public static File mcPath;
	
	@EventHandler
	public void construct(FMLConstructionEvent e)
	{
		ModClassLoader loader = e.getModClassLoader();
		
		File dir = Loader.instance().getConfigDir().getAbsoluteFile();
		dir = dir.getParentFile();
		mcPath = dir;
		
		var d = mcPath.toPath().resolve("asm").resolve("MusicLayer");
		d.toFile().mkdirs();
		File SoundLib = d.resolve("SoundLib.jar").toFile();
		try(FileOutputStream fos = new FileOutputStream(SoundLib); InputStream in = MusicLayerMC.class.getResourceAsStream("/SoundLib.jar"))
		{
			byte[] buf = new byte[1024];
			int read;
			while((read = in.read(buf)) > 0)
				fos.write(buf, 0, read);
		} catch(IOException err)
		{
			throw new RuntimeException(err);
		}
		
		try
		{
			loader.addFile(SoundLib);
		} catch(MalformedURLException err)
		{
			throw new RuntimeException(err);
		}
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{
		MinecraftForge.EVENT_BUS.register(this);
		MusicLayer.LOG.info("PreInit");
		proxy.preInit();
		meta(e.getModMetadata());
	}
	
	@EventHandler
	public void certificateViolation(FMLFingerprintViolationEvent e)
	{
		MusicLayer.LOG.warn("*****************************");
		MusicLayer.LOG.warn("WARNING: Somebody has been tampering with MusicLayer jar!");
		MusicLayer.LOG.warn("It is highly recommended that you redownload mod from https://www.curseforge.com/projects/289079 !");
		MusicLayer.LOG.warn("*****************************");
		HammerCore.invalidCertificates.put("musiclayer", "https://www.curseforge.com/projects/289079");
	}
	
	@SubscribeEvent
	public void handle(GetAllRequiredApisEvent e)
	{
		e.addRequiredApi("SoundLib", "1.1.4");
	}
	
	private static ModMetadata meta(ModMetadata md)
	{
		md.authorList = HammerCore.getHCAuthorsArray();
		md.name = "MusicLayer";
		md.modId = "musiclayer";
		md.autogenerated = false;
		md.description = "A mod that adds an easy layer(s) of music that can be manipulated/chosen by multiple mods.";
		md.version = "@VERSION@";
		md.logoFile = "logo.png";
		return md;
	}
}