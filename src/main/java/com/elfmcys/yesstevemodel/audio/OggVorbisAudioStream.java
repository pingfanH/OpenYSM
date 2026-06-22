package com.elfmcys.yesstevemodel.audio;
import net.minecraft.client.sounds.AudioStream;
import javax.sound.sampled.AudioFormat;
import java.nio.ByteBuffer;
import java.io.IOException;
public class OggVorbisAudioStream implements IAudioStreamSupport, AudioStream {
    public AudioFormat getFormat() { return new AudioFormat(44100f, 16, 2, true, false); }
    public ByteBuffer read(int size) { return ByteBuffer.allocate(0); }
    public void close() {}
    public boolean isClosed() { return true; }
    public void reset() {}
}
