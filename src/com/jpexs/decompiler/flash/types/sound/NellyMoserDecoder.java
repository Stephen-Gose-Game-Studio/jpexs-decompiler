/*
 * Copyright (C) 2014 JPEXS
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jpexs.decompiler.flash.types.sound;

import java.io.IOException;
import java.io.OutputStream;
import lt.dkd.nellymoser.CodecImpl;

/**
 *
 * @author JPEXS
 */
public class NellyMoserDecoder extends SoundDecoder {

    public static final int NELLY_BLOCK_LEN = 64;
    public static final int NELLY_BUF_LEN = 128;
    public static final int NELLY_SAMPLES = 2 * NELLY_BUF_LEN;

    public NellyMoserDecoder(SoundFormat soundFormat) {
        super(soundFormat);
    }

    @Override
    public void decode(byte[] data, OutputStream os) throws IOException {
        soundFormat.stereo = false;
        float audioD[] = new float[NELLY_SAMPLES];

        final float[] state = new float[64];

        byte[] block = new byte[NELLY_BLOCK_LEN];
        int blockCount = data.length / NELLY_BLOCK_LEN;
        for (int j = 0; j < blockCount; j++) {
            System.arraycopy(data, j * NELLY_BLOCK_LEN, block, 0, NELLY_BLOCK_LEN);
            CodecImpl.decode(state, block, audioD);
            short audio[] = new short[NELLY_SAMPLES];
            for (int i = 0; i < audioD.length; i++) {
                audio[i] = (short) (audioD[i]);
            }
            byte d[] = new byte[audio.length * 2];
            for (int i = 0; i < audio.length; i++) {
                int s = audio[i];
                d[i * 2] = (byte) (s & 0xff);
                d[i * 2 + 1] = (byte) ((s >> 8) & 0xff);
            }
            os.write(d);
        }
    }
}