package me.theonlysd12.soundboard;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class PluginMessageByteBuffer {

    private final ByteBuffer inputBuffer;

    public PluginMessageByteBuffer(byte[] data) {
        this.inputBuffer = ByteBuffer.wrap(data);
    }

    public int readVarInt() {
        this.ensureReading();

        int value = 0;
        int length = 0;
        byte currentByte;

        do {
            currentByte = readByte();
            value |= (currentByte & 0x7F) << (length * 7);

            length += 1;
            if (length > 5) {
                throw new RuntimeException("VarInt is too big");
            }

        } while ((currentByte & 0x80) == 0x80);

        return value;
    }

    @NotNull
    public String readString() {
        this.ensureReading();
        return new String(readByteArray(), StandardCharsets.UTF_8);
    }

    public byte[] readByteArray() {
        this.ensureReading();

        int size = readVarInt();
        byte[] bytes = new byte[size];
        this.inputBuffer.get(bytes);

        return bytes;
    }

    public byte readByte() {
        this.ensureReading();
        return inputBuffer.get();
    }

    private void ensureReading() {
        if (inputBuffer == null) {
            throw new IllegalStateException("Cannot read from a write-only buffer");
        }
    }
}
