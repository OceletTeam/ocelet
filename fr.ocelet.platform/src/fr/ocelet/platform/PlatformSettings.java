package fr.ocelet.platform;

public class PlatformSettings {

	public final static String version = "1.0";

	public final static int NORMAL = 0;
	public final static int VERBOSE = 1;
	public final static int DEBUG = 2;

	public static int msgLevel=DEBUG;

	public static int getMsgLevel() {
		return msgLevel;
	}

	public static void setMsgLevel(int msgLevel) {
		PlatformSettings.msgLevel = msgLevel;
	}

	public PlatformSettings() {
		msgLevel = NORMAL;
	}
}
