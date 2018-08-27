/*
 * Open Teradata Viewer ( util )
 * Copyright (C) 2015, D. Campione
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

package net.sourceforge.open_teradata_viewer.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class StreamUtil {

    public static byte[] stream2Array(InputStream strm) throws IOException {
        return stream2Array(strm, Long.MAX_VALUE);
    }

    public static byte[] stream2Array(InputStream strm, long maxSize)
            throws IOException {
        if (strm == null) {
            return null;
        }

        ArrayList<byte[]> buflist = new ArrayList<byte[]>();
        byte[] buffer = new byte[8096];
        byte[] result = null;
        int readed, pos;

        try {
            while ((readed = strm.read(buffer, 0, 8096)) >= 0) {
                byte[] tbf = new byte[readed];
                System.arraycopy(buffer, 0, tbf, 0, readed);
                buflist.add(tbf);
                if ((maxSize -= readed) < 0) {
                    break;
                }
            }
        } finally {
            strm.close();
        }

        readed = 0;
        for (int i = 0; i < buflist.size(); i++) {
            readed += buflist.get(i).length;
        }

        if (readed > 0) {
            pos = 0;
            result = new byte[readed];
            for (int i = 0; i < buflist.size(); i++) {
                byte[] b = buflist.get(i);
                System.arraycopy(b, 0, result, pos, b.length);
                pos += b.length;
            }
            buflist.clear();
        }

        return result;
    }

    public static String stream2String(InputStream inputStream, String charset)
            throws IOException {
        int read;
        StringBuilder sb = new StringBuilder();

        InputStreamReader isr = new InputStreamReader(inputStream, charset);
        try {
            while ((read = isr.read()) != -1) {
                sb.append((char) read);
            }
        } finally {
            isr.close();
        }
        return sb.toString();
    }

    public static String stream2String(InputStream inputStream)
            throws IOException {
        return stream2String(inputStream, Charset.defaultCharset().name());
    }

}
