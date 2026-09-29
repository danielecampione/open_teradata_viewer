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

package net.sourceforge.open_teradata_viewer.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Structured logging utility for Open Teradata Viewer.
 * Provides thread-safe logging with different levels and automatic log rotation.
 * 
 * @author D. Campione
 */
public final class Logger {
    
    public enum Level {
        DEBUG(0, "DEBUG"),
        INFO(1, "INFO"),
        WARN(2, "WARN"),
        ERROR(3, "ERROR");
        
        private final int priority;
        private final String name;
        
        Level(int priority, String name) {
            this.priority = priority;
            this.name = name;
        }
        
        public int getPriority() {
            return priority;
        }
        
        public String getName() {
            return name;
        }
    }
    
    private static final Logger INSTANCE = new Logger();
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final int MAX_LOG_SIZE = 10 * 1024 * 1024; // 10MB
    private static final int MAX_LOG_FILES = 5;
    
    private final ConcurrentLinkedQueue<LogEntry> logQueue;
    private final ScheduledExecutorService logWriter;
    private final Path logDirectory;
    private final Path currentLogFile;
    private Level currentLevel;
    private boolean consoleOutput;
    
    private Logger() {
        this.logQueue = new ConcurrentLinkedQueue<>();
        this.logWriter = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "OTV-Logger");
            t.setDaemon(true);
            return t;
        });
        this.currentLevel = Level.INFO;
        this.consoleOutput = true;
        
        // Initialize log directory
        String userHome = System.getProperty("user.home");
        this.logDirectory = Paths.get(userHome, ".open_teradata_viewer", "logs");
        this.currentLogFile = logDirectory.resolve("otv.log");
        
        try {
            Files.createDirectories(logDirectory);
        } catch (IOException e) {
            System.err.println("Failed to create log directory: " + e.getMessage());
        }
        
        // Start log writer thread
        logWriter.scheduleWithFixedDelay(this::processLogQueue, 1, 1, TimeUnit.SECONDS);
        
        // Shutdown hook to flush remaining logs
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logWriter.shutdown();
            try {
                if (!logWriter.awaitTermination(5, TimeUnit.SECONDS)) {
                    logWriter.shutdownNow();
                }
            } catch (InterruptedException e) {
                logWriter.shutdownNow();
                Thread.currentThread().interrupt();
            }
            processLogQueue(); // Final flush
        }));
    }
    
    public static Logger getInstance() {
        return INSTANCE;
    }
    
    public void setLevel(Level level) {
        this.currentLevel = level;
    }
    
    public void setConsoleOutput(boolean enabled) {
        this.consoleOutput = enabled;
    }
    
    public void debug(String message) {
        log(Level.DEBUG, message, null);
    }
    
    public void debug(String message, Throwable throwable) {
        log(Level.DEBUG, message, throwable);
    }
    
    public void info(String message) {
        log(Level.INFO, message, null);
    }
    
    public void info(String message, Throwable throwable) {
        log(Level.INFO, message, throwable);
    }
    
    public void warn(String message) {
        log(Level.WARN, message, null);
    }
    
    public void warn(String message, Throwable throwable) {
        log(Level.WARN, message, throwable);
    }
    
    public void error(String message) {
        log(Level.ERROR, message, null);
    }
    
    public void error(String message, Throwable throwable) {
        log(Level.ERROR, message, throwable);
    }
    
    private void log(Level level, String message, Throwable throwable) {
        if (level.getPriority() < currentLevel.getPriority()) {
            return;
        }
        
        LogEntry entry = new LogEntry(
            LocalDateTime.now(),
            level,
            Thread.currentThread().getName(),
            message,
            throwable
        );
        
        logQueue.offer(entry);
        
        // For ERROR level, also output to console immediately
        if (level == Level.ERROR && consoleOutput) {
            System.err.println(formatLogEntry(entry));
        }
    }
    
    private void processLogQueue() {
        LogEntry entry;
        while ((entry = logQueue.poll()) != null) {
            String formattedEntry = formatLogEntry(entry);

            // Write to console if enabled (ERROR already printed immediately in log())
            if (consoleOutput && entry.level != Level.ERROR) {
                if (entry.level == Level.WARN) {
                    System.err.println(formattedEntry);
                } else {
                    System.out.println(formattedEntry);
                }
            }

            // Write to file
            writeToFile(formattedEntry);
        }
    }
    
    private String formatLogEntry(LogEntry entry) {
        StringBuilder sb = new StringBuilder();
        sb.append(entry.timestamp.format(TIMESTAMP_FORMAT))
          .append(" [")
          .append(entry.level.getName())
          .append("] ")
          .append(entry.threadName)
          .append(" - ")
          .append(entry.message);
        
        if (entry.throwable != null) {
            sb.append("\n");
            StringWriter sw = new StringWriter();
            entry.throwable.printStackTrace(new PrintWriter(sw));
            sb.append(sw.toString());
        }
        
        return sb.toString();
    }
    
    private void writeToFile(String logEntry) {
        try {
            // Check if log rotation is needed
            if (Files.exists(currentLogFile) && Files.size(currentLogFile) > MAX_LOG_SIZE) {
                rotateLogFiles();
            }
            
            Files.write(currentLogFile, (logEntry + "\n").getBytes(), 
                       StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }
    
    private void rotateLogFiles() throws IOException {
        // Move existing log files
        for (int i = MAX_LOG_FILES - 1; i > 0; i--) {
            Path oldFile = logDirectory.resolve("otv.log." + i);
            Path newFile = logDirectory.resolve("otv.log." + (i + 1));
            
            if (Files.exists(oldFile)) {
                if (i == MAX_LOG_FILES - 1) {
                    Files.deleteIfExists(newFile);
                }
                Files.move(oldFile, newFile);
            }
        }
        
        // Move current log to .1
        Path firstBackup = logDirectory.resolve("otv.log.1");
        if (Files.exists(currentLogFile)) {
            Files.move(currentLogFile, firstBackup);
        }
    }
    
    private static class LogEntry {
        final LocalDateTime timestamp;
        final Level level;
        final String threadName;
        final String message;
        final Throwable throwable;
        
        LogEntry(LocalDateTime timestamp, Level level, String threadName, 
                String message, Throwable throwable) {
            this.timestamp = timestamp;
            this.level = level;
            this.threadName = threadName;
            this.message = message;
            this.throwable = throwable;
        }
    }
}