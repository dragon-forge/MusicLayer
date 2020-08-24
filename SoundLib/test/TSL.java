import java.io.File;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import tk.zeitheron.sound.Sound;
import tk.zeitheron.sound.SoundLib;

public class TSL
{
	public static void main(String[] args)
	{
		try
		{
			Sound snd = SoundLib.playOgg(new File("C:\\Users\\Zeitheron\\Downloads\\eerie.ogg"));
			snd.play();
			
			while(!snd.isDonePlaying())
			{
				snd.setVolume(0.5F);
				System.out.println(Math.round(snd.getPlayedSeconds() / snd.getDurationInSeconds() * 1000F) / 10F + "% LEFT: " + Math.round(snd.getTimeLeftInSeconds()));
			}
		} catch(UnsupportedAudioFileException | IOException | LineUnavailableException e)
		{
			e.printStackTrace();
		}
	}
}