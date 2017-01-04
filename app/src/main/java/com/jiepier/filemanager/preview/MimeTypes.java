package com.jiepier.filemanager.preview;

import android.webkit.MimeTypeMap;


import com.blankj.utilcode.utils.FileUtils;
import com.jiepier.filemanager.R;
import com.jiepier.filemanager.util.FileUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Pattern;

public final class MimeTypes {

    private MimeTypes() {}

    private static final HashMap<String, Integer> EXT_ICONS = new HashMap<>();
    private static final HashMap<String, String> MIME_TYPES = new HashMap<>();

    static {
        // BINARY
        EXT_ICONS.put("a", R.drawable.type_unknown);
        EXT_ICONS.put("bin", R.drawable.type_unknown);
        EXT_ICONS.put("class", R.drawable.type_unknown);
        EXT_ICONS.put("com", R.drawable.type_unknown);
        EXT_ICONS.put("dex", R.drawable.type_unknown);
        EXT_ICONS.put("dump", R.drawable.type_unknown);
        EXT_ICONS.put("exe", R.drawable.type_unknown);
        EXT_ICONS.put("dat", R.drawable.type_unknown);
        EXT_ICONS.put("dll", R.drawable.type_unknown);
        EXT_ICONS.put("lib", R.drawable.type_unknown);
        EXT_ICONS.put("o", R.drawable.type_unknown);
        EXT_ICONS.put("obj", R.drawable.type_unknown);
        EXT_ICONS.put("pyc", R.drawable.type_unknown);
        EXT_ICONS.put("pyo", R.drawable.type_unknown);
        EXT_ICONS.put("ser", R.drawable.type_unknown);
        EXT_ICONS.put("swf", R.drawable.type_unknown);
        EXT_ICONS.put("so", R.drawable.type_unknown);

        // Shell
        EXT_ICONS.put("bar", R.drawable.type_unknown);
        EXT_ICONS.put("csh", R.drawable.type_unknown);
        EXT_ICONS.put("ksh", R.drawable.type_unknown);
        EXT_ICONS.put("sh", R.drawable.type_unknown);

        // TEXT
        EXT_ICONS.put("csv", R.drawable.type_note);
        EXT_ICONS.put("diff", R.drawable.type_note);
        EXT_ICONS.put("in", R.drawable.type_note);
        EXT_ICONS.put("list", R.drawable.type_note);
        EXT_ICONS.put("log", R.drawable.type_note);
        EXT_ICONS.put("rc", R.drawable.type_note);
        EXT_ICONS.put("text", R.drawable.type_note);
        EXT_ICONS.put("txt", R.drawable.type_note);
        EXT_ICONS.put("tsv", R.drawable.type_note);

        // Properties
        EXT_ICONS.put("properties", R.drawable.type_config);
        EXT_ICONS.put("conf", R.drawable.type_config);
        EXT_ICONS.put("config", R.drawable.type_config);
        EXT_ICONS.put("prop", R.drawable.type_config);

        // HTML
        EXT_ICONS.put("htm", R.drawable.type_html);
        EXT_ICONS.put("html", R.drawable.type_html);
        EXT_ICONS.put("mhtml", R.drawable.type_html);
        EXT_ICONS.put("xhtml", R.drawable.type_html);

        // XML
        EXT_ICONS.put("xml", R.drawable.type_xml);
        EXT_ICONS.put("mxml", R.drawable.type_xml);

        // DOCUMENT
        EXT_ICONS.put("doc", R.drawable.type_note);
        EXT_ICONS.put("docx", R.drawable.type_note);
        EXT_ICONS.put("odp", R.drawable.type_note);
        EXT_ICONS.put("odt", R.drawable.type_note);
        EXT_ICONS.put("rtf", R.drawable.type_note);
        EXT_ICONS.put("ods", R.drawable.type_note);
        EXT_ICONS.put("xls", R.drawable.type_note);
        EXT_ICONS.put("xlsx", R.drawable.type_note);

        // Presentation
        EXT_ICONS.put("ppt", R.drawable.type_note);
        EXT_ICONS.put("pptx", R.drawable.type_note);

        // PDF
        EXT_ICONS.put("pdf", R.drawable.type_pdf);
        EXT_ICONS.put("fdf", R.drawable.type_pdf);
        EXT_ICONS.put("ldwf", R.drawable.type_pdf);

        // Package
        EXT_ICONS.put("ace", R.drawable.type_package);
        EXT_ICONS.put("bz", R.drawable.type_package);
        EXT_ICONS.put("bz2", R.drawable.type_package);
        EXT_ICONS.put("cab", R.drawable.type_package);
        EXT_ICONS.put("cpio", R.drawable.type_package);
        EXT_ICONS.put("gz", R.drawable.type_package);
        EXT_ICONS.put("lha", R.drawable.type_package);
        EXT_ICONS.put("lrf", R.drawable.type_package);
        EXT_ICONS.put("lzma", R.drawable.type_package);
        EXT_ICONS.put("rar", R.drawable.type_package);
        EXT_ICONS.put("tar", R.drawable.type_package);
        EXT_ICONS.put("tgz", R.drawable.type_package);
        EXT_ICONS.put("xz", R.drawable.type_package);
        EXT_ICONS.put("zip", R.drawable.type_package);
        EXT_ICONS.put("Z", R.drawable.type_package);
        EXT_ICONS.put("7z", R.drawable.type_package);
        EXT_ICONS.put("rar", R.drawable.type_package);
        EXT_ICONS.put("tar", R.drawable.type_package);
        EXT_ICONS.put("jar", R.drawable.type_package);

        // Image
        EXT_ICONS.put("bmp", R.drawable.type_pic);
        EXT_ICONS.put("cgm", R.drawable.type_pic);
        EXT_ICONS.put("g3", R.drawable.type_pic);
        EXT_ICONS.put("gif", R.drawable.type_pic);
        EXT_ICONS.put("ief", R.drawable.type_pic);
        EXT_ICONS.put("jpe", R.drawable.type_pic);
        EXT_ICONS.put("jpeg", R.drawable.type_pic);
        EXT_ICONS.put("jpg", R.drawable.type_pic);
        EXT_ICONS.put("png", R.drawable.type_pic);
        EXT_ICONS.put("btif", R.drawable.type_pic);
        EXT_ICONS.put("svg", R.drawable.type_pic);
        EXT_ICONS.put("svgz", R.drawable.type_pic);
        EXT_ICONS.put("tif", R.drawable.type_pic);
        EXT_ICONS.put("tiff", R.drawable.type_pic);
        EXT_ICONS.put("psd", R.drawable.type_pic);
        EXT_ICONS.put("dwg", R.drawable.type_pic);
        EXT_ICONS.put("dxf", R.drawable.type_pic);
        EXT_ICONS.put("fbs", R.drawable.type_pic);
        EXT_ICONS.put("fpx", R.drawable.type_pic);
        EXT_ICONS.put("fst", R.drawable.type_pic);
        EXT_ICONS.put("mmr", R.drawable.type_pic);
        EXT_ICONS.put("rlc", R.drawable.type_pic);
        EXT_ICONS.put("mdi", R.drawable.type_pic);
        EXT_ICONS.put("npx", R.drawable.type_pic);
        EXT_ICONS.put("wbmp", R.drawable.type_pic);
        EXT_ICONS.put("xif", R.drawable.type_pic);
        EXT_ICONS.put("ras", R.drawable.type_pic);
        EXT_ICONS.put("ico", R.drawable.type_pic);
        EXT_ICONS.put("pcx", R.drawable.type_pic);
        EXT_ICONS.put("pct", R.drawable.type_pic);
        EXT_ICONS.put("pic", R.drawable.type_pic);
        EXT_ICONS.put("xbm", R.drawable.type_pic);
        EXT_ICONS.put("xwd", R.drawable.type_pic);
        EXT_ICONS.put("bpg", R.drawable.type_pic);

        // Audio
        EXT_ICONS.put("aac", R.drawable.type_music);
        EXT_ICONS.put("adp", R.drawable.type_music);
        EXT_ICONS.put("aif", R.drawable.type_music);
        EXT_ICONS.put("aifc", R.drawable.type_music);
        EXT_ICONS.put("aiff", R.drawable.type_music);
        EXT_ICONS.put("amr", R.drawable.type_music);
        EXT_ICONS.put("ape", R.drawable.type_music);
        EXT_ICONS.put("au", R.drawable.type_music);
        EXT_ICONS.put("dts", R.drawable.type_music);
        EXT_ICONS.put("eol", R.drawable.type_music);
        EXT_ICONS.put("flac", R.drawable.type_music);
        EXT_ICONS.put("kar", R.drawable.type_music);
        EXT_ICONS.put("lvp", R.drawable.type_music);
        EXT_ICONS.put("m2a", R.drawable.type_music);
        EXT_ICONS.put("m3a", R.drawable.type_music);
        EXT_ICONS.put("m3u", R.drawable.type_music);
        EXT_ICONS.put("m4a", R.drawable.type_music);
        EXT_ICONS.put("mid", R.drawable.type_music);
        EXT_ICONS.put("mid", R.drawable.type_music);
        EXT_ICONS.put("mka", R.drawable.type_music);
        EXT_ICONS.put("mp2", R.drawable.type_music);
        EXT_ICONS.put("mp3", R.drawable.type_music);
        EXT_ICONS.put("mpga", R.drawable.type_music);
        EXT_ICONS.put("oga", R.drawable.type_music);
        EXT_ICONS.put("ogg", R.drawable.type_music);
        EXT_ICONS.put("pya", R.drawable.type_music);
        EXT_ICONS.put("ram", R.drawable.type_music);
        EXT_ICONS.put("rmi", R.drawable.type_music);
        EXT_ICONS.put("snd", R.drawable.type_music);
        EXT_ICONS.put("spx", R.drawable.type_music);
        EXT_ICONS.put("wav", R.drawable.type_music);
        EXT_ICONS.put("wax", R.drawable.type_music);
        EXT_ICONS.put("wma", R.drawable.type_music);
        EXT_ICONS.put("xmf", R.drawable.type_music);

        // Video
        EXT_ICONS.put("3gp", R.drawable.type_video);
        EXT_ICONS.put("3gpp", R.drawable.type_video);
        EXT_ICONS.put("3g2", R.drawable.type_video);
        EXT_ICONS.put("3gpp2", R.drawable.type_video);
        EXT_ICONS.put("h261", R.drawable.type_video);
        EXT_ICONS.put("h263", R.drawable.type_video);
        EXT_ICONS.put("h264", R.drawable.type_video);
        EXT_ICONS.put("jpgv", R.drawable.type_video);
        EXT_ICONS.put("jpgm", R.drawable.type_video);
        EXT_ICONS.put("jpm", R.drawable.type_video);
        EXT_ICONS.put("mj2", R.drawable.type_video);
        EXT_ICONS.put("mp4", R.drawable.type_video);
        EXT_ICONS.put("mp4v", R.drawable.type_video);
        EXT_ICONS.put("mpg4", R.drawable.type_video);
        EXT_ICONS.put("m1v", R.drawable.type_video);
        EXT_ICONS.put("m2v", R.drawable.type_video);
        EXT_ICONS.put("mpa", R.drawable.type_video);
        EXT_ICONS.put("mpe", R.drawable.type_video);
        EXT_ICONS.put("mpg", R.drawable.type_video);
        EXT_ICONS.put("mpeg", R.drawable.type_video);
        EXT_ICONS.put("ogv", R.drawable.type_video);
        EXT_ICONS.put("mov", R.drawable.type_video);
        EXT_ICONS.put("qt", R.drawable.type_video);
        EXT_ICONS.put("fvt", R.drawable.type_video);
        EXT_ICONS.put("m4u", R.drawable.type_video);
        EXT_ICONS.put("pyv", R.drawable.type_video);
        EXT_ICONS.put("viv", R.drawable.type_video);
        EXT_ICONS.put("f4v", R.drawable.type_video);
        EXT_ICONS.put("fli", R.drawable.type_video);
        EXT_ICONS.put("flv", R.drawable.type_video);
        EXT_ICONS.put("m4v", R.drawable.type_video);
        EXT_ICONS.put("asf", R.drawable.type_video);
        EXT_ICONS.put("asx", R.drawable.type_video);
        EXT_ICONS.put("avi", R.drawable.type_video);
        EXT_ICONS.put("wmv", R.drawable.type_video);
        EXT_ICONS.put("wmx", R.drawable.type_video);
        EXT_ICONS.put("mkv", R.drawable.type_video);
        EXT_ICONS.put("divx", R.drawable.type_video);

        // Application
        EXT_ICONS.put("apk", R.drawable.type_apk);

		/*
         * ================= MIME TYPES ====================
		 */
        MIME_TYPES.put("asm", "text/x-asm");
        MIME_TYPES.put("def", "text/plain");
        MIME_TYPES.put("in", "text/plain");
        MIME_TYPES.put("rc", "text/plain");
        MIME_TYPES.put("list", "text/plain");
        MIME_TYPES.put("log", "text/plain");
        MIME_TYPES.put("pl", "text/plain");
        MIME_TYPES.put("prop", "text/plain");
        MIME_TYPES.put("properties", "text/plain");
        MIME_TYPES.put("rc", "text/plain");

        MIME_TYPES.put("epub", "application/epub+zip");
        MIME_TYPES.put("ibooks", "application/x-ibooks+zip");

        MIME_TYPES.put("ifb", "text/calendar");
        MIME_TYPES.put("eml", "message/rfc822");
        MIME_TYPES.put("msg", "application/vnd.ms-outlook");

        MIME_TYPES.put("ace", "application/x-ace-compressed");
        MIME_TYPES.put("bz", "application/x-bzip");
        MIME_TYPES.put("bz2", "application/x-bzip2");
        MIME_TYPES.put("cab", "application/vnd.ms-cab-compressed");
        MIME_TYPES.put("gz", "application/x-gzip");
        MIME_TYPES.put("lrf", "application/octet-stream");
        MIME_TYPES.put("jar", "application/java-archive");
        MIME_TYPES.put("xz", "application/x-xz");
        MIME_TYPES.put("Z", "application/x-compress");

        MIME_TYPES.put("bat", "application/x-msdownload");
        MIME_TYPES.put("ksh", "text/plain");
        MIME_TYPES.put("sh", "application/x-sh");

        MIME_TYPES.put("db", "application/octet-stream");
        MIME_TYPES.put("db3", "application/octet-stream");

        MIME_TYPES.put("otf", "x-font-otf");
        MIME_TYPES.put("ttf", "x-font-ttf");
        MIME_TYPES.put("psf", "x-font-linux-psf");

        MIME_TYPES.put("cgm", "image/cgm");
        MIME_TYPES.put("btif", "image/prs.btif");
        MIME_TYPES.put("dwg", "image/vnd.dwg");
        MIME_TYPES.put("dxf", "image/vnd.dxf");
        MIME_TYPES.put("fbs", "image/vnd.fastbidsheet");
        MIME_TYPES.put("fpx", "image/vnd.fpx");
        MIME_TYPES.put("fst", "image/vnd.fst");
        MIME_TYPES.put("mdi", "image/vnd.ms-mdi");
        MIME_TYPES.put("npx", "image/vnd.net-fpx");
        MIME_TYPES.put("xif", "image/vnd.xiff");
        MIME_TYPES.put("pct", "image/x-pict");
        MIME_TYPES.put("pic", "image/x-pict");

        MIME_TYPES.put("adp", "audio/adpcm");
        MIME_TYPES.put("au", "audio/basic");
        MIME_TYPES.put("snd", "audio/basic");
        MIME_TYPES.put("m2a", "audio/mpeg");
        MIME_TYPES.put("m3a", "audio/mpeg");
        MIME_TYPES.put("oga", "audio/ogg");
        MIME_TYPES.put("spx", "audio/ogg");
        MIME_TYPES.put("aac", "audio/x-aac");
        MIME_TYPES.put("mka", "audio/x-matroska");

        MIME_TYPES.put("jpgv", "video/jpeg");
        MIME_TYPES.put("jpgm", "video/jpm");
        MIME_TYPES.put("jpm", "video/jpm");
        MIME_TYPES.put("mj2", "video/mj2");
        MIME_TYPES.put("mjp2", "video/mj2");
        MIME_TYPES.put("mpa", "video/mpeg");
        MIME_TYPES.put("ogv", "video/ogg");
        MIME_TYPES.put("flv", "video/x-flv");
        MIME_TYPES.put("mkv", "video/x-matroska");
    }

    public static int getIconForExt(String ext) {
        final Integer res = EXT_ICONS.get(ext);
        return res == null ? 0 : res;
    }

    public static String getMimeType(File file) {
        if (file.isDirectory()) {
            return null;
        }

        String type = null;
        final String extension = FileUtil.getExtension(file.getName());

        if (extension != null && !extension.isEmpty()) {
            final String extensionLowerCase = extension.toLowerCase(Locale.getDefault());
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extensionLowerCase);
            if (type == null) {
                type = MIME_TYPES.get(extensionLowerCase);
            }
        }

        if(type == null)
            type="*/*";
        return type;
    }

    private static boolean mimeTypeMatch(String mime, String input) {
        return Pattern.matches(mime.replace("*", ".*"), input);
    }

    public static boolean isPicture(File f) {
        final String mime = getMimeType(f);
        return mime != null && mimeTypeMatch("image/*", mime);
    }

    public static boolean isVideo(File f) {
        final String mime = getMimeType(f);
        return mime != null && mimeTypeMatch("video/*", mime);
    }

    public static boolean isDoc(File f){
        final String mime = getMimeType(f);
        return mime != null && (mime.equals("text/plain")
                ||mime.equals("application/pdf")
                ||mime.equals("application/msword")
                ||mime.equals("application/vnd.ms-excel"));
    }

    public static boolean isApk(File f){
        String path = FileUtils.getFileName(f);
        return path.endsWith(".apk");
    }

    public static boolean isZip(File f){
        String path = FileUtils.getFileName(f);
        return path.endsWith(".zip");
    }
}
