package com.jiepier.filemanager.util;

import com.stericson.RootShell.RootShell;
import com.stericson.RootShell.execution.Command;
import com.stericson.RootShell.execution.Shell;
import com.stericson.RootTools.RootTools;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class RootCommands {

    private RootCommands() {}

    private static final String UNIX_ESCAPE_EXPRESSION = "(\\(|\\)|\\[|\\]|\\s|\'|\"|`|\\{|\\}|&|\\\\|\\?)";

    private static String getCommandLineString(String input) {
        return input.replaceAll(UNIX_ESCAPE_EXPRESSION, "\\\\$1");
    }

    public static ArrayList<String> listFiles(String path, boolean showhidden) {
        ArrayList<String> content = new ArrayList<>();
        ArrayList<String> items;
        String hidden = "";

        if (showhidden)
            hidden = "-a ";

        items = executeForResult("ls " + hidden + getCommandLineString(path));

        // add path to files/folders
        for (String i : items) {
            content.add(path + "/" + i);
        }
        return content;
    }

    public static ArrayList<String> findFiles(String path, String query) {
        String cmd = "find " + getCommandLineString(path) + " -type f -iname " + '*' + getCommandLineString(query) + '*' + " -exec ls -a {} \\;";
        return executeForResult(cmd);
    }

    // Create Directory with root
    public static boolean createRootdir(File dir) {
        if (dir.exists())
            return false;

        try {
            if (!readReadWriteFile())
                RootTools.remount(getCommandLineString(dir.getParent()), "rw");

            runAndWait("mkdir " + getCommandLineString(dir.getAbsolutePath()));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Move or Copy with Root Access using RootTools library
    public static void moveCopyRoot(String old, String newDir) {
        try {
            if (!readReadWriteFile())
                RootTools.remount(getCommandLineString(newDir), "rw");

            runAndWait("cp -fr " + getCommandLineString(old) + " " + getCommandLineString(newDir));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // path = currentDir
    // oldName = currentDir + "/" + selected Item
    // name = new name
    public static void renameRootTarget(String path, String oldname, String name) {
        File file = new File(path + "/" + oldname);
        File newf = new File(path + "/" + name);

        if (name.length() < 1)
            return;

        try {
            if (!readReadWriteFile())
                RootTools.remount(getCommandLineString(path), "rw");

            runAndWait("mv " + getCommandLineString(file.getAbsolutePath()) + " "
                    + getCommandLineString(newf.getAbsolutePath()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Delete file with root using RootTools library
    public static void deleteRootFileOrDir(File f) {
        RootTools.deleteFileOrDirectory(getCommandLineString(f.getPath()), true);
    }

    // Create file with root
    public static boolean createRootFile(String cdir, String name) {
        File dir = new File(cdir + "/" + name);

        if (dir.exists())
            return false;

        try {
            if (!readReadWriteFile())
                RootTools.remount(getCommandLineString(cdir), "rw");

            runAndWait("touch " + getCommandLineString(dir.getAbsolutePath()));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Check if system is mounted
    private static boolean readReadWriteFile() {
        File mountFile = new File("/proc/mounts");
        StringBuilder procData = new StringBuilder();
        if (mountFile.exists()) {
            BufferedReader br = null;
            try {
                FileInputStream fis = new FileInputStream(mountFile.toString());
                DataInputStream dis = new DataInputStream(fis);
                br = new BufferedReader(new InputStreamReader(dis));
                String data;
                while ((data = br.readLine()) != null) {
                    procData.append(data).append("\n");
                }
                
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        // ignore
                    }
                }
            }

            String[] tmp = procData.toString().split("\n");
            for (String aTmp : tmp) {
                // Kept simple here on purpose different devices have
                // different blocks
                if (aTmp.contains("/dev/block")
                        && aTmp.contains("/system")) {
                    if (aTmp.contains("rw")) {
                        // system is rw
                        return true;
                    } else if (aTmp.contains("ro")) {
                        // system is ro
                        return false;
                    } else {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    public static boolean changeGroupOwner(File file, String owner, String group) {
        try {
            if (!readReadWriteFile())
                RootTools.remount(getCommandLineString(file.getAbsolutePath()), "rw");

            runAndWait("chown " + owner + "." + group + " "
                    + getCommandLineString(file.getAbsolutePath()));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean applyPermissions(File file, Permissions permissions) {
        try {
            if (!readReadWriteFile())
                RootTools.remount(getCommandLineString(file.getAbsolutePath()), "rw");

            runAndWait("chmod " + Permissions.toOctalPermission(permissions) + " "
                    + getCommandLineString(file.getAbsolutePath()));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static String[] getFileProperties(File file) {
        String[] info;
        String dir = "";

        if (file.isDirectory()) {
            dir = "d";
        }

        String cmd = "ls -l" + dir + " " + getCommandLineString(file.getAbsolutePath());

        info = getAttrs(executeForResult(cmd).get(0));
        return info;
    }

    private static String[] getAttrs(String string) {
        if (string.length() < 44) {
            throw new IllegalArgumentException("Bad ls -l output: " + string);
        }
        final char[] chars = string.toCharArray();

        final String[] results = new String[11];
        int ind = 0;
        final StringBuilder current = new StringBuilder();

        Loop:
        for (int i = 0; i < chars.length; i++) {
            switch (chars[i]) {
                case ' ':
                case '\t':
                    if (current.length() != 0) {
                        results[ind] = current.toString();
                        ind++;
                        current.setLength(0);
                        if (ind == 10) {
                            results[ind] = string.substring(i).trim();
                            break Loop;
                        }
                    }
                    break;

                default:
                    current.append(chars[i]);
                    break;
            }
        }

        return results;
    }

    private static void runAndWait(String cmd) {
        Command c = new Command(0, cmd);

        try {
            RootShell.getShell(false).add(c);
            commandWait(RootShell.getShell(false), c);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<String> executeForResult(String cmd) {
        final ArrayList<String> results = new ArrayList<>();

        Command command = new Command(3, false, cmd) {
            @Override
            public void commandOutput(int id, String line) {
                results.add(line);
                super.commandOutput(id, line);
            }
        };

        try {
            RootShell.getShell(true).add(command);
            commandWait(RootShell.getShell(true), command);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return results;
    }

    private static void commandWait(Shell shell, Command cmd) {
        while (!cmd.isFinished()) {
            synchronized (cmd) {
                try {
                    if (!cmd.isFinished()) {
                        cmd.wait(2000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (!cmd.isExecuting() && !cmd.isFinished()) {
                if (!shell.isExecuting && !shell.isReading) {
                    Exception e = new Exception();
                    e.setStackTrace(Thread.currentThread().getStackTrace());
                    e.printStackTrace();
                } else if (shell.isExecuting && !shell.isReading) {
                    Exception e = new Exception();
                    e.setStackTrace(Thread.currentThread().getStackTrace());
                    e.printStackTrace();
                } else {
                    Exception e = new Exception();
                    e.setStackTrace(Thread.currentThread().getStackTrace());
                    e.printStackTrace();
                }
            }
        }
    }
}
