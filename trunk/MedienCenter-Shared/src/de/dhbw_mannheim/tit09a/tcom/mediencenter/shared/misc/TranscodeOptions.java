package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.misc;

import java.io.Serializable;

/**
 * <p>
 * Transcoding a stream via VLC command line:<br>
 * <a href="http://wiki.videolan.org/Documentation:Streaming_HowTo/Advanced_Streaming_Using_the_Command_Line#transcode">http://wiki.videolan.org/
 * Documentation:Streaming_HowTo/Advanced_Streaming_Using_the_Command_Line#transcode</a>
 * </p>
 * 
 * <p>
 * A complete list of all supported codecs: <a href="http://wiki.videolan.org/Codec">http://wiki.videolan.org/Codec</a><br>
 * For example:<br>
 * Video codecs:
 * <ul>
 * <li>mp4v: MPEG-4 Video</li>
 * <li>h264: H264</li>
 * </ul>
 * Audio codecs:
 * <ul>
 * <li>mpga: MPEG audio (recommended for portability)</li>
 * <li>mp3: MPEG Layer 3 audio</li>
 * <li>mp4a: MP4 audio</li>
 * <li>a52: Dolby Digital (A52 or AC3)</li>
 * </ul>
 * </p>
 * 
 * @author Max
 * 
 */
public class TranscodeOptions implements Serializable
{
	private static final long	serialVersionUID	= 1L;

	/**
	 * FourCC of the video codec. <code>vcodec</code> in VLC command line. This options allows to specify the codec the video tracks of the input
	 * stream should be transcoded to.
	 */
	public String				videoCodec;

	/**
	 * FourCC of the audio codec. <code>acodec</code> in VLC command line. This options allows to specify the codec the audio tracks of the input
	 * stream should be transcoded to.
	 */
	public String				audioCodec;

	/**
	 * <code>vb</code> in VLC command line. This option allows to set the bitrate of the transcoded video stream, in kbit/s.
	 */
	public int					videoKBitRate;

	/**
	 * <code>ab</code> in VLC command line. This option allows to set the bitrate of the transcoded audio stream, in kbit/s.
	 */
	public int					audioKBitRate;

	/**
	 * <code>audio-sync<code> in VLC command line.
	 * When this option is enabled, VLC will drop/duplicate video frames to synchronize the video track on the audio track.
	 */
	public boolean				audioSync;

	/**
	 * <code>deinterlace</code> in VLC command line. This option allows to enable deinterlacing of interlaced video streams before encoding.
	 */
	public boolean				deinterlace;


	public TranscodeOptions()
	{

	}
	
	public TranscodeOptions(String videoCodec, String audioCodec, int videoKBitRate, int audioKBitRate)
	{
		this.videoCodec = videoCodec;
		this.audioCodec = audioCodec;
		this.videoKBitRate = videoKBitRate;
		this.audioKBitRate = audioKBitRate;
	}
	
	public TranscodeOptions(String videoCodec, String audioCodec, int videoKBitRate, int audioKBitRate, boolean audioSync, boolean deinterlace)
	{
		this.videoCodec = videoCodec;
		this.audioCodec = audioCodec;
		this.videoKBitRate = videoKBitRate;
		this.audioKBitRate = audioKBitRate;
		this.audioSync = audioSync;
		this.deinterlace = deinterlace;
	}

}
