/*
 * Open Teradata Viewer ( kernel )
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

package net.sourceforge.open_teradata_viewer.update;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

/**
 * Builds the {@link Proxy} instance to use for the update-check HTTP(S)
 * connection.
 * <p>
 * Earlier versions of the update checker relied on the JVM-wide system
 * properties <code>proxyHost</code>/<code>proxyPort</code> to configure a
 * manually specified proxy. Those two property names, however, are not
 * honored by {@link java.net.HttpURLConnection} for <code>https://</code>
 * connections: since J2SE 1.4 the JDK uses the protocol-specific properties
 * <code>http.proxyHost</code>/<code>http.proxyPort</code> and
 * <code>https.proxyHost</code>/<code>https.proxyPort</code> instead. As a
 * consequence, a manually configured proxy was silently ignored and a direct
 * connection was always attempted - very likely the root cause of the
 * errors observed both with and without an explicit proxy configuration.
 * <p>
 * This resolver sidesteps the issue entirely: instead of mutating global JVM
 * system properties, it builds an explicit {@link Proxy} object that the
 * caller passes directly to {@link java.net.URL#openConnection(Proxy)}.
 *
 * @author D. Campione
 * 
 */
public final class ProxyResolver {

    private ProxyResolver() {
    }

    /**
     * Builds a {@link Proxy} from an explicit host/port pair, typically
     * entered by the user in the proxy configuration dialog.
     *
     * @param host the proxy host; if <code>null</code> or blank, a direct
     *        connection ({@link Proxy#NO_PROXY}) is returned.
     * @param port the proxy port, as text; must be a valid, positive
     *        integer if <code>host</code> is not blank.
     * @return the resulting {@link Proxy}.
     * @throws IOException if <code>host</code> is not blank but
     *         <code>port</code> is not a valid port number.
     */
    public static Proxy resolveManualProxy(String host, String port)
            throws IOException {
        if (host == null || host.trim().isEmpty()) {
            return Proxy.NO_PROXY;
        }
        int portNumber;
        try {
            portNumber = Integer.parseInt(port == null ? "" : port.trim());
        } catch (NumberFormatException nfe) {
            throw new IOException("Invalid proxy port: '" + port + "'", nfe);
        }
        return new Proxy(Proxy.Type.HTTP,
                new InetSocketAddress(host.trim(), portNumber));
    }

    /**
     * Resolves the operating system's configured proxy (if any) for the
     * given URL, through the platform's default {@link ProxySelector}. This
     * requires the <code>java.net.useSystemProxies</code> system property to
     * have been set to <code>true</code> beforehand, or the JVM will not
     * pick up the OS-level proxy configuration at all.
     *
     * @param url the URL that is about to be contacted.
     * @return the resolved {@link Proxy}, or {@link Proxy#NO_PROXY} if none
     *         could be determined.
     */
    public static Proxy resolveSystemProxy(URL url) {
        try {
            URI uri = url.toURI();
            List<Proxy> proxies = ProxySelector.getDefault().select(uri);
            if (proxies != null) {
                for (Proxy candidate : proxies) {
                    if (candidate.type() != Proxy.Type.DIRECT) {
                        return candidate;
                    }
                }
            }
        } catch (URISyntaxException use) {
            // Fall through to a direct connection
        }
        return Proxy.NO_PROXY;
    }
}