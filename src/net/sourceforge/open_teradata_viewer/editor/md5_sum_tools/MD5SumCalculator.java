/*
 * Open Teradata Viewer ( editor md5 sum tools )
 * Copyright (C), D. Campione
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

package net.sourceforge.open_teradata_viewer.editor.md5_sum_tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class MD5SumCalculator {

    public MD5SumCalculator() {
    }

    public String calculateMD5Checksum(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        byte[] md5 = DigestUtils.md5(fis);
        String hexString = new String(Hex.encodeHex(md5));
        return hexString;
    }
}