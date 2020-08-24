package tk.zeitheron.sound;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.function.Supplier;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundLib
{
	private static final SoundLib sl = new SoundLib();
	
	private SoundLib()
	{
	}
	
	public static Sound playOgg(Supplier<InputStream> stream) throws FileNotFoundException, UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		return sl.play(stream::get);
	}
	
	public static Sound playOgg(File file) throws FileNotFoundException, UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		return sl.play(file);
	}
	
	public static Sound playOgg(String path) throws FileNotFoundException, UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		if(path.startsWith("http://") || path.startsWith("https://"))
		{
			return sl.play(() -> new URL(path).openStream());
		}
		return sl.play(new File(path));
	}
	
	public Sound play(IInputStream stream) throws FileNotFoundException, UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		return new Sound(() -> new BufferedInputStream(stream.getInput()));
	}
	
	public Sound play(File file) throws FileNotFoundException, UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		return play(() -> new FileInputStream(file));
	}
	
	static AudioFormat getOutFormat(AudioFormat inFormat)
	{
		int ch = inFormat.getChannels();
		float rate = inFormat.getSampleRate();
		return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);
	}
}