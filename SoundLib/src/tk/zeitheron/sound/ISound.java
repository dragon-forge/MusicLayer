package tk.zeitheron.sound;

import javax.sound.sampled.FloatControl;

public interface ISound
{
	double getDurationInSeconds();
	
	double getPlayedSeconds();
	
	default double getTimeLeftInSeconds()
	{
		return getDurationInSeconds() - getPlayedSeconds();
	}
	
	void setVolume(float var1);
	
	boolean isDonePlaying();
	
	void play();
	
	void stop();
	
	void dispose();
	
	FloatControl getControl(FloatControl.Type var1);
}