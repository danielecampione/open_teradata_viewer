/*
 * Open Teradata Viewer ( editor syntax )
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

package test.net.sourceforge.open_teradata_viewer.editor.syntax;

import java.io.File;
import java.net.URL;

import net.sourceforge.open_teradata_viewer.editor.syntax.FileFileLocation;
import net.sourceforge.open_teradata_viewer.editor.syntax.FileLocation;
import net.sourceforge.open_teradata_viewer.editor.syntax.URLFileLocation;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for the {@link FileLocation} class.
 *
 * @author D. Campione
 *
 */
public class FileLocationTest {

    @Test
    public void testCreate_StringArg_FileName() throws Exception {
        String url = "test.txt";
        FileLocation loc = FileLocation.create(url);
        Assert.assertTrue(loc instanceof FileFileLocation);
        Assert.assertTrue(loc.isLocal());
        Assert.assertFalse(loc.isRemote());
    }

    @Test
    public void testCreate_StringArg_FileUrl() throws Exception {
        String url = File.separatorChar == '/' ? "file:///test.txt"
                : "file:///C:/test.txt";
        FileLocation loc = FileLocation.create(url);
        Assert.assertTrue(loc instanceof FileFileLocation);
        Assert.assertTrue(loc.isLocal());
        Assert.assertFalse(loc.isRemote());
    }

    @Test
    public void testCreate_StringArg_FtpUrl() throws Exception {
        String url = "ftp://ftp.microsoft.com/deskapps/readme.txt";
        FileLocation loc = FileLocation.create(url);
        Assert.assertTrue(loc instanceof URLFileLocation);
        Assert.assertFalse(loc.isLocal());
        Assert.assertTrue(loc.isRemote());
    }

    @Test
    public void testCreate_StringArg_HttpUrl() throws Exception {
        String url = "http://google.com";
        FileLocation loc = FileLocation.create(url);
        Assert.assertTrue(loc instanceof URLFileLocation);
        Assert.assertFalse(loc.isLocal());
        Assert.assertTrue(loc.isRemote());
    }

    @Test
    public void testCreate_StringArg_HttpsUrl() throws Exception {
        String url = "https://google.com";
        FileLocation loc = FileLocation.create(url);
        Assert.assertTrue(loc instanceof URLFileLocation);
        Assert.assertFalse(loc.isLocal());
        Assert.assertTrue(loc.isRemote());
    }

    @Test
    public void testCreate_FileArg() throws Exception {
        File file = new File(File.separatorChar == '/' ? "test.txt"
                : "C:/test.txt");
        FileLocation loc = FileLocation.create(file);
        Assert.assertTrue(loc instanceof FileFileLocation);
        Assert.assertTrue(loc.isLocal());
        Assert.assertFalse(loc.isRemote());
    }

    @Test
    public void testCreate_UrlArg_HttpsUrl() throws Exception {
        URL url = new URL("https://google.com");
        FileLocation loc = FileLocation.create(url);
        Assert.assertTrue(loc instanceof URLFileLocation);
        Assert.assertFalse(loc.isLocal());
        Assert.assertTrue(loc.isRemote());
    }

    @Test
    public void testCreate_UrlArg_FileUrl() throws Exception {
        URL url = new URL("file:///test.txt");
        FileLocation loc = FileLocation.create(url);
        Assert.assertTrue(loc instanceof FileFileLocation);
        Assert.assertTrue(loc.isLocal());
        Assert.assertFalse(loc.isRemote());
    }
}