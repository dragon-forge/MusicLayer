package org.zeith.musiclayer.api;

import java.io.IOException;
import java.io.InputStream;

import org.zeith.musiclayer.url.MusicCacher;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public interface IInput
{
	InputStream createInput() throws IOException;
	
	/**
	 * Use this to set your sound. Here's how it works: <br>
	 * Parsing new ResourceLocation("musiclayer", "music/example_music") <br>
	 * Will grab following sound:
	 * /assets/musiclayer/sounds/music/example_music.ogg <br>
	 * Note: the music file can be overrided by resource packs.
	 */
	public static IInput ofSound(ResourceLocation path)
	{
		return () -> Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(path.getNamespace(), "sounds/" + path.getPath() + ".ogg")).getInputStream();
	}
	
	/**
	 * Use this to set your sound. Here's how it works: <br>
	 * Parsing new ResourceLocation("musiclayer",
	 * "sounds/music/example_music.ogg") <br>
	 * Will grab following sound:
	 * /assets/musiclayer/sounds/music/example_music.ogg <br>
	 * Note: the music file can be overrided by resource packs.
	 */
	public static IInput ofSoundFullPath(ResourceLocation path)
	{
		return () -> Minecraft.getMinecraft().getResourceManager().getResource(path).getInputStream();
	}
	
	/**
	 * Creates sound input from url. Note: the music file will get cached on
	 * user's hard drive in order to play it in offline mode.
	 */
	public static IInput ofSound(String url)
	{
		return () -> MusicCacher.getMusicStream(url);
	}
}