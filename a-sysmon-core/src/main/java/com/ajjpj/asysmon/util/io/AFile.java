package com.ajjpj.asysmon.util.io;

import com.ajjpj.asysmon.util.AFunction0;
import com.ajjpj.asysmon.util.AFunction1;
import com.ajjpj.asysmon.util.AStatement1;
import com.ajjpj.asysmon.util.AUnchecker;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author arno
 */
public class AFile {
    private final File file;

    public AFile(String file) {
        this(new File(file));
    }

    public AFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public List<String> lines(Charset encoding) throws IOException {
        final List<String> result = new ArrayList<String>();

        final BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
        try {
            String line;
            while((line = r.readLine()) != null) {
                result.add(line);
            }
        }
        finally {
            r.close();
        }
        return result;
    }

    public <E extends Exception> void iterate(Charset encoding, AStatement1<Iterator<String>, E> callback) throws E, IOException {
        final BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
        try {
            final Iterator<String> iter = new Iterator<String>() {
                private String line = r.readLine();

                @Override public boolean hasNext() {
                    return line != null;
                }

                @Override public String next() {
                    return AUnchecker.executeUnchecked(new AFunction0<String, IOException>() {
                        @Override public String apply() throws IOException {
                            final String result = line;
                            line = r.readLine();
                            return result;
                        }
                    });
                }

                @Override public void remove() {
                    throw new UnsupportedOperationException();
                }
            };

            callback.apply(iter);
        }
        finally {
            r.close();
        }
    }
    public <R, E extends Exception> R iterate(Charset encoding, AFunction1<Iterator<String>, R, E> callback) throws E, IOException {
        final BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
        try {
            final Iterator<String> iter = new Iterator<String>() {
                private String line = r.readLine();

                @Override public boolean hasNext() {
                    return line != null;
                }

                @Override public String next() {
                    return AUnchecker.executeUnchecked(new AFunction0<String, IOException>() {
                        @Override public String apply() throws IOException {
                            final String result = line;
                            line = r.readLine();
                            return result;
                        }
                    });
                }

                @Override public void remove() {
                    throw new UnsupportedOperationException();
                }
            };

            return callback.apply(iter);
        }
        finally {
            r.close();
        }
    }
}
