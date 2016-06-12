import java.util.ArrayDeque;

import org.jnativehook.keyboard.NativeKeyEvent;

/**
 * This class literally just exists to translate native key strokes
 * into strings to send to the server
 * 
 * @author Caleb Choi
 */
public class KeyInterpreter {
	
	/**
	 * Returns a string representation of the native key stroke, given the 
	 * key event and a boolean representing whether or not it was the press
	 * or the release
	 * By the way this was absolute cancer to type. I've basically got arthritis
	 * right about now
	 * 
	 * @param e Native key event
	 * @param isPress Boolean on whether it was the press or release,
	 * where true is the press
	 * @return A string representation on the key event
	 */
	public static String interpretKey(NativeKeyEvent e, boolean isPress) throws ArrayIndexOutOfBoundsException {
		
		// If it's a press and not a release
		if (isPress) {
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
			else if (e.getKeyCode() == NativeKeyEvent.VC_A) return "a";
			else if (e.getKeyCode() == NativeKeyEvent.VC_B) return "b";
			else if (e.getKeyCode() == NativeKeyEvent.VC_C) return "c";
			else if (e.getKeyCode() == NativeKeyEvent.VC_D) return "d";
			else if (e.getKeyCode() == NativeKeyEvent.VC_E) return "e";
			else if (e.getKeyCode() == NativeKeyEvent.VC_F) return "f";
			else if (e.getKeyCode() == NativeKeyEvent.VC_G) return "g";
			else if (e.getKeyCode() == NativeKeyEvent.VC_H) return "h";
			else if (e.getKeyCode() == NativeKeyEvent.VC_I) return "i";
			else if (e.getKeyCode() == NativeKeyEvent.VC_J) return "j";
			else if (e.getKeyCode() == NativeKeyEvent.VC_K) return "k";
			else if (e.getKeyCode() == NativeKeyEvent.VC_L) return "l";
			else if (e.getKeyCode() == NativeKeyEvent.VC_M) return "m";
			else if (e.getKeyCode() == NativeKeyEvent.VC_N) return "n";
			else if (e.getKeyCode() == NativeKeyEvent.VC_O) return "o";
			else if (e.getKeyCode() == NativeKeyEvent.VC_P) return "p";
			else if (e.getKeyCode() == NativeKeyEvent.VC_Q) return "q";
			else if (e.getKeyCode() == NativeKeyEvent.VC_R) return "r";
			else if (e.getKeyCode() == NativeKeyEvent.VC_S) return "s";
			else if (e.getKeyCode() == NativeKeyEvent.VC_T) return "t";
			else if (e.getKeyCode() == NativeKeyEvent.VC_U) return "u";
			else if (e.getKeyCode() == NativeKeyEvent.VC_V) return "v";
			else if (e.getKeyCode() == NativeKeyEvent.VC_W) return "w";
			else if (e.getKeyCode() == NativeKeyEvent.VC_X) return "x";
			else if (e.getKeyCode() == NativeKeyEvent.VC_Y) return "y";
			else if (e.getKeyCode() == NativeKeyEvent.VC_Z) return "z";
			else if (e.getKeyCode() == NativeKeyEvent.VC_ALT_L) return "[ALT_L]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_ALT_R) return "[ALT_R]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_APP_CALCULATOR) return "[APP_CALCULATOR]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_APP_MAIL) return "[APP_MAIL]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_APP_MUSIC) return "[APP_MUSIC]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_APP_PICTURES) return "[APP_PICTURES]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_BACK_SLASH) return "\\";
			else if (e.getKeyCode() == NativeKeyEvent.VC_BACKSPACE) return "[BACKSPACE]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_CAPS_LOCK) return "[CAPS_LOCK]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_CLOSE_BRACKET) return "]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_COMMA) return ",";
			else if (e.getKeyCode() == NativeKeyEvent.VC_CONTEXT_MENU) return "[CONTEXT_MENU]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_CONTROL_L) return "[CTRL_L]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_CONTROL_R) return "[CTRL_R]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_DELETE) return "[DELETE]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_DOWN) return "[DOWN]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_END) return "[END]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_ENTER) return "[ENTER]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_EQUALS) return "=";
			else if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) return "[ESC]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_F1) return "[FUNCTION_1]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_F2) return "[FUNCTION_2]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_F3) return "[FUNCTION_3]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_F4) return "[FUNCTION_4]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_F5) return "[FUNCTION_5]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_F6) return "[FUNCTION_6]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_F7) return "[FUNCTION_7]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_F8) return "[FUNCTION_8]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_F9) return "[FUNCTION_9]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_F10) return "[FUNCTION_10]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_F11) return "[FUNCTION_11]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_F12) return "[FUNCTION_12]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_F13) return "[FUNCTION_13]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_F14) return "[FUNCTION_14]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_F15) return "[FUNCTION_15]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_F16) return "[FUNCTION_16]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_F17) return "[FUNCTION_17]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_F18) return "[FUNCTION_18]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_F19) return "[FUNCTION_19]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_F20) return "[FUNCTION_20]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_F21) return "[FUNCTION_21]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_F22) return "[FUNCTION_22]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_F23) return "[FUNCTION_23]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_F24) return "[FUNCTION_24]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_HOME) return "[HOME]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_INSERT) return "[INSERT]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_KP_0) return "0";
			else if (e.getKeyCode() == NativeKeyEvent.VC_KP_1) return "1";
			else if (e.getKeyCode() == NativeKeyEvent.VC_KP_2) return "2";
			else if (e.getKeyCode() == NativeKeyEvent.VC_KP_3) return "3";
			else if (e.getKeyCode() == NativeKeyEvent.VC_KP_4) return "4";
			else if (e.getKeyCode() == NativeKeyEvent.VC_KP_5) return "5";
			else if (e.getKeyCode() == NativeKeyEvent.VC_KP_6) return "6";
			else if (e.getKeyCode() == NativeKeyEvent.VC_KP_7) return "7";
			else if (e.getKeyCode() == NativeKeyEvent.VC_KP_8) return "8";
			else if (e.getKeyCode() == NativeKeyEvent.VC_KP_9) return "9";
			else if (e.getKeyCode() == NativeKeyEvent.VC_KP_ADD) return "[KP_ADD]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_KP_COMMA) return "[KP_COMMA]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_KP_DIVIDE) return "[KP_DIVIDE]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_KP_ENTER) return "[KP_ENTER]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_KP_EQUALS) return "[KP_EQUALS]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_KP_MULTIPLY) return "[KP_MULTIPLY]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_KP_SEPARATOR) return "[KP_SEPARATOR]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_KP_SUBTRACT) return "[KP_SUBTRACT]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_LEFT) return "[LEFT]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_MEDIA_EJECT) return "[MEDIA_EJECT]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_MEDIA_NEXT) return "[MEDIA_NEXT]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_MEDIA_PLAY) return "[MEDIA_PLAY]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_MEDIA_PREVIOUS) return "[MEDIA_PREVIOUS]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_MEDIA_SELECT) return "[MEDIA_SELECT]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_MEDIA_STOP) return "[MEDIA_STOP]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_META_L) return "[META_L]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_META_R) return "[META_R]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_MINUS) return "-";
			else if (e.getKeyCode() == NativeKeyEvent.VC_NUM_LOCK) return "[NUM_LOCK]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_OPEN_BRACKET) return "[";
			else if (e.getKeyCode() == NativeKeyEvent.VC_PAGE_DOWN) return "[PAGE_DN]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_PAGE_UP) return "[PAGE_UP]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_PAUSE) return "[PAUSE]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_PERIOD) return ".";
			else if (e.getKeyCode() == NativeKeyEvent.VC_POWER) return "[POWER]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_PRINTSCREEN) return "[PRINT_SCREEN]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_QUOTE) return "\"";
			else if (e.getKeyCode() == NativeKeyEvent.VC_RIGHT) return "[RIGHT]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_SCROLL_LOCK) return "[SCROLL_LOCK]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_SEMICOLON) return ";";
			else if (e.getKeyCode() == NativeKeyEvent.VC_SHIFT_L) return "[SHIFT_L]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_SHIFT_R) return "[SHIFT_R]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_SLASH) return "/";
			else if (e.getKeyCode() == NativeKeyEvent.VC_SLEEP) return "[SLEEP]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_SPACE) return " ";
			else if (e.getKeyCode() == NativeKeyEvent.VC_TAB) return "[TAB]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_UNDEFINED) return "[UNDEFINED]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_UNDERSCORE) return "_";
			else if (e.getKeyCode() == NativeKeyEvent.VC_VOLUME_DOWN) return "[VOL_DOWN]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_VOLUME_MUTE) return "[VOL_MUTE]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_VOLUME_UP) return "[VOL_UP]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_WAKE) return "[WAKE]";
			// else if (e.getKeyCode() == NativeKeyEvent.VC_) return "";
			else return "[fagot]";
		} else {
			if (e.getKeyCode() == NativeKeyEvent.VC_ALT_L) return "[/ALT_L]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_ALT_R) return "[/ALT_R]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_CONTROL_L) return "[/CTRL_L]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_CONTROL_R) return "[/CTRL_R]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_SHIFT_L) return "[/SHIFT_L]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_SHIFT_R) return "[/SHIFT_R]";
			else if (e.getKeyCode() == NativeKeyEvent.VC_TAB) return "[/TAB]";
			else return "";
		}
	}

}
