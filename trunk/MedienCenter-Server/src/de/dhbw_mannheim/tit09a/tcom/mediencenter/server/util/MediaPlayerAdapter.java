package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.util.List;

import uk.co.caprica.vlcj.binding.internal.libvlc_logo_position_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_marquee_position_e;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_player_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_stats_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.binding.internal.libvlc_state_t;
import uk.co.caprica.vlcj.player.AudioOutputDeviceType;
import uk.co.caprica.vlcj.player.DeinterlaceMode;
import uk.co.caprica.vlcj.player.MediaDetails;
import uk.co.caprica.vlcj.player.MediaMeta;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.player.TrackDescription;
import uk.co.caprica.vlcj.player.TrackInfo;

public class MediaPlayerAdapter implements MediaPlayer
{

	@Override
	public void addMediaPlayerEventListener(MediaPlayerEventListener listener)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void removeMediaPlayerEventListener(MediaPlayerEventListener listener)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void enableEvents(int eventMask)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setStandardMediaOptions(String... mediaOptions)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void playMedia(String mrl, String... mediaOptions)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void prepareMedia(String mrl, String... mediaOptions)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean startMedia(String mrl, String... mediaOptions)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void parseMedia()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void requestParseMedia()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isMediaParsed()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public MediaMeta getMediaMeta()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MediaMeta getMediaMeta(libvlc_media_t mediaInstance)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MediaMeta> getSubItemMediaMeta()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addMediaOptions(String... mediaOptions)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setRepeat(boolean repeat)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean getRepeat()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setPlaySubItems(boolean playSubItems)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public int subItemCount()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int subItemIndex()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<String> subItems()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<libvlc_media_t> subItemsMedia()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean playNextSubItem(String... mediaOptions)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean playSubItem(int index, String... mediaOptions)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPlayable()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPlaying()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSeekable()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canPause()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long getLength()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getTime()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getPosition()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getFps()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getRate()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getVideoOutputs()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Dimension getVideoDimension()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MediaDetails getMediaDetails()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAspectRatio()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getScale()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getCropGeometry()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public libvlc_media_stats_t getMediaStatistics()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public libvlc_media_stats_t getMediaStatistics(libvlc_media_t media)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public libvlc_state_t getMediaState()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public libvlc_state_t getMediaPlayerState()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTitleCount()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTitle()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setTitle(int title)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public int getVideoTrackCount()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getVideoTrack()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setVideoTrack(int track)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public int getAudioTrackCount()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getAudioTrack()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setAudioTrack(int track)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void play()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean start()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void stop()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setPause(boolean pause)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void pause()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void nextFrame()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void skip(long delta)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void skipPosition(float delta)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setTime(long time)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setPosition(float position)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public int setRate(float rate)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setAspectRatio(String aspectRatio)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setScale(float factor)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setCropGeometry(String cropGeometry)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean setAudioOutput(String output)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setAudioOutputDevice(String output, String outputDeviceId)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setAudioOutputDeviceType(AudioOutputDeviceType deviceType)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public AudioOutputDeviceType getAudioOutputDeviceType()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void mute()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mute(boolean mute)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isMute()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getVolume()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setVolume(int volume)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public int getAudioChannel()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setAudioChannel(int channel)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public long getAudioDelay()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setAudioDelay(long delay)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public int getChapterCount()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getChapter()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setChapter(int chapterNumber)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void nextChapter()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void previousChapter()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void menuActivate()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void menuUp()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void menuDown()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void menuLeft()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void menuRight()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public int getSpuCount()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSpu()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setSpu(int spu)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void cycleSpu()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public long getSpuDelay()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setSpuDelay(long delay)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setSubTitleFile(String subTitleFileName)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setSubTitleFile(File subTitleFile)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public int getTeletextPage()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setTeletextPage(int pageNumber)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void toggleTeletext()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public List<TrackDescription> getTitleDescriptions()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TrackDescription> getVideoDescriptions()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TrackDescription> getAudioDescriptions()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TrackDescription> getSpuDescriptions()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getChapterDescriptions(int title)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getChapterDescriptions()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<List<String>> getAllChapterDescriptions()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TrackInfo> getTrackInfo()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TrackInfo> getTrackInfo(libvlc_media_t media)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<List<TrackInfo>> getSubItemTrackInfo()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSnapshotDirectory(String snapshotDirectoryName)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean saveSnapshot()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveSnapshot(int width, int height)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveSnapshot(File file)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveSnapshot(File file, int width, int height)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public BufferedImage getSnapshot()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BufferedImage getSnapshot(int width, int height)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void enableLogo(boolean enable)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setLogoOpacity(int opacity)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setLogoOpacity(float opacity)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setLogoLocation(int x, int y)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setLogoPosition(libvlc_logo_position_e position)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setLogoFile(String logoFile)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setLogoImage(RenderedImage logoImage)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void enableMarquee(boolean enable)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setMarqueeText(String text)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setMarqueeColour(Color colour)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setMarqueeColour(int colour)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setMarqueeOpacity(int opacity)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setMarqueeOpacity(float opacity)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setMarqueeSize(int size)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setMarqueeTimeout(int timeout)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setMarqueeLocation(int x, int y)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setMarqueePosition(libvlc_marquee_position_e position)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setDeinterlace(DeinterlaceMode deinterlaceMode)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setAdjustVideo(boolean adjustVideo)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isAdjustVideo()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public float getContrast()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setContrast(float contrast)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public float getBrightness()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setBrightness(float brightness)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public int getHue()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setHue(int hue)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public float getSaturation()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setSaturation(float saturation)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public float getGamma()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setGamma(float gamma)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public String mrl()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String mrl(libvlc_media_t mediaInstance)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object userData()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void userData(Object userData)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void release()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public libvlc_media_player_t mediaPlayerInstance()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
