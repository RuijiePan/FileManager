package com.jiepier.filemanager.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by JiePier on 16/11/22.
 */

public class AppProcessInfo {

    private String appName;
    private String processName;
    private String process;
    private int pid;
    private int uid;
    private Drawable icon;
    private long memory;
    private String cpu;
    private String threadsCount;

    private boolean isSystem;
    public AppProcessInfo(){
        super();
    }

    public AppProcessInfo(String processName, int pid, int uid) {
        super();
        this.processName = processName;
        this.pid = pid;
        this.uid = uid;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public long getMemory() {
        return memory;
    }

    public void setMemory(long memory) {
        this.memory = memory;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getThreadsCount() {
        return threadsCount;
    }

    public void setThreadsCount(String threadsCount) {
        this.threadsCount = threadsCount;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }

    @Override
    public String toString() {
        return "AppProcessInfo{" +
                "appName='" + appName + '\'' +
                ", processName='" + processName + '\'' +
                ", process='" + process + '\'' +
                ", pid=" + pid +
                ", uid=" + uid +
                ", icon=" + icon +
                ", memory=" + memory +
                ", cpu='" + cpu + '\'' +
                ", threadsCount='" + threadsCount + '\'' +
                ", isSystem=" + isSystem +
                '}';
    }
}
