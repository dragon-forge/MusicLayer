package tk.zeitheron.sound;

import java.io.IOException;

import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class RepeatableSound implements ISound
{
	private Sound unrepeatable;
	private float currentVol;
	private final IInputStream input;
	
	public RepeatableSound(final IInputStream soundIn) throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		this.input = soundIn;
		this.unrepeatable = new Sound(this.input);
	}
	
	public RepeatableSound(final byte[] soundIn) throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		this(new IInputStream.ByteArray(soundIn.clone()));
	}
	
	@Override
	public void setVolume(final float vol)
	{
		this.currentVol = vol;
		if(this.unrepeatable != null)
		{
			this.unrepeatable.setVolume(vol);
		}
	}
	
	@Override
	public boolean isDonePlaying()
	{
		return this.unrepeatable == null || this.unrepeatable.isDonePlaying();
	}
	
	@Override
	public void play()
	{
		if(this.unrepeatable != null)
		{
			this.unrepeatable.play();
			this.unrepeatable.setVolume(this.currentVol);
		} else
		{
			try
			{
				(this.unrepeatable = new Sound(this.input)).setVolume(this.currentVol);
				this.unrepeatable.play();
			} catch(IOException ex)
			{
			} catch(LineUnavailableException ex2)
			{
			} catch(UnsupportedAudioFileException ex3)
			{
			}
		}
	}
	
	@Override
	public void stop()
	{
		if(this.unrepeatable != null)
		{
			this.unrepeatable.stop();
			this.unrepeatable.setVolume(this.currentVol);
		}
	}
	
	@Override
	public void dispose()
	{
		if(this.unrepeatable != null)
		{
			this.unrepeatable.dispose();
		}
		this.unrepeatable = null;
	}
	
	@Override
	public FloatControl getControl(final FloatControl.Type type)
	{
		if(this.unrepeatable != null)
			return this.unrepeatable.getControl(type);
		throw new IllegalArgumentException("No sound currently playing!");
	}
	
	@Override
	public double getDurationInSeconds()
	{
		if(this.unrepeatable != null)
			return unrepeatable.getDurationInSeconds();
		throw new IllegalArgumentException("No sound currently playing!");
	}
	
	@Override
	public double getPlayedSeconds()
	{
		if(this.unrepeatable != null)
			return unrepeatable.getPlayedSeconds();
		throw new IllegalArgumentException("No sound currently playing!");
	}
}