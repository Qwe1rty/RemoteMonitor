import org.jnativehook.keyboard.NativeKeyEvent;

/**
 * This class literally just exists to translate native key strokes
 * into strings to send to the server
 * 
 * @author Caleb Choi
 */
public class KeyInterpreter {
	
	public static String interpretKey(NativeKeyEvent e, boolean isPressed) {
		
		// If it's a press and not a release
		if (isPressed) {
			if (e.getKeyCode() == NativeKeyEvent.VC_0) return "0";
			else if (e.getKeyCode() == NativeKeyEvent.VC_1) return "1";
			else if (e.getKeyCode() == NativeKeyEvent.VC_2) return "2";
			else if (e.getKeyCode() == NativeKeyEvent.VC_3) return "3";
			else if (e.getKeyCode() == NativeKeyEvent.VC_4) return "4";
			else if (e.getKeyCode() == NativeKeyEvent.VC_5) return "5";
			else if (e.getKeyCode() == NativeKeyEvent.VC_6) return "6";
			else if (e.getKeyCode() == NativeKeyEvent.VC_7) return "7";
			else if (e.getKeyCode() == NativeKeyEvent.VC_8) return "8";
			else if (e.getKeyCode() == NativeKeyEvent.VC_9) return "9";
			else if (e.getKeyCode() == NativeKeyEvent.VC_ALT_L) return "[ALT_L]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_ALT_R) return "[ALT_R]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_APP_CALCULATOR) return "[APP_CALCULATOR]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_APP_MAIL) return "[APP_MAIL]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_APP_MUSIC) return "[APP_MUSIC]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_APP_PICTURES) return "[APP_PICTURES]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_0) return "0";
			else if (e.getKeyCode() == NativeKeyEvent.VC_0) return "0";
			else if (e.getKeyCode() == NativeKeyEvent.VC_0) return "0";
			else if (e.getKeyCode() == NativeKeyEvent.VC_0) return "0";
			else if (e.getKeyCode() == NativeKeyEvent.VC_0) return "0";
			else if (e.getKeyCode() == NativeKeyEvent.VC_0) return "0";
			else if (e.getKeyCode() == NativeKeyEvent.VC_0) return "0";
			else return "[UNDEFINED]";
		} else {
			if (e.getKeyCode() == NativeKeyEvent.VC_ALT_L) return "[/ALT_L]";
			else return "[UNDEFINED]";
		}
	}

}
