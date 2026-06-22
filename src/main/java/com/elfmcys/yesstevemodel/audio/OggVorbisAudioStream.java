package com.elfmcys.yesstevemodel.audio;
import javax.sound.sampled.AudioFormat;
public class OggVorbisAudioStream implements IAudioStreamSupport {
    public AudioFormat getFormat() { return null; }
    public int read(int i) { return 0; }
    public void close() {}
    public boolean isClosed() { return true; }
    public void reset() {}
}
