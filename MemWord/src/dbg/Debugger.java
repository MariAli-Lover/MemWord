package dbg;

public class Debugger {
	public static boolean isEnabled() {
		return true;
	}
	
	public static void log(Object o) {
		if(isEnabled())
			System.out.println(o.toString());
	}

}
