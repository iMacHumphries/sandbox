package toolbox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Debug {
	
	private static final boolean DEBUG = true;
	private static final boolean SUPER_DEBUG = false;
	private static StackTraceElement currentCaller;
	
	public Debug() {}
	
	public static void log(String _string) {
		print(_string);
	}
	
	public static void log(boolean bool) {
		String result = "false";
		if (bool)
			 result = "true";
		print(result);
	}
	
	public static void log(Object obj) {
		if (obj != null)
			print(obj.toString());
		else 
			print("null object");
	}
	
	public static void log(int _string) {
		print(_string + "");
	}
	
	public static void log(float[] _array) {
		if (_array.length <= 0) {
			print("emptyArray");
			return;
		}
		String result = "[";
		for (float s : _array)
			result += s + ", ";
		result = result.substring(0, result.length() -2);
		result += "]";
		print(result);	
	}
	
	public static void log(String[] _array) {
		if (_array.length <= 0) {
			print("emptyArray");
			return;
		}
		String result = "[";
		for (String s : _array)
			result += s + ", ";
		result = result.substring(0, result.length() -2);
		result += "]";
		print(result);	
	}
	
	public static void log(int[] _array) {
		if (_array.length <= 0) {
			print("emptyArray");
			return;
		}
		String result = "[";
		for (int s : _array)
			result += s + ", ";
		result = result.substring(0, result.length() -2);
		result += "]";
		print(result);	
	}
	
	public static void log(Vector3f vector) {
		if (vector != null) {
			String result = "V:[" + vector.x + ", " + vector.y + ", " + vector.z + "]";
			print(result);
		}
		else {
			print("null vector3f");
		}
	}
	
	public static void log(Matrix4f matrix) {
		String result = "\nstart-------------------\n";
		result += "| " + matrix.m00 + " "  + matrix.m01 + " " + matrix.m02 + " " + matrix.m03 + " |\n";
		result += "| " + matrix.m10 + " "  + matrix.m11 + " " + matrix.m12 + " " + matrix.m13 + " |\n";
		result += "| " + matrix.m20 + " "  + matrix.m21 + " " + matrix.m22 + " " + matrix.m23 + " |\n";
		result += "| " + matrix.m30 + " "  + matrix.m31 + " " + matrix.m32 + " " + matrix.m33 + " |\n";
		result += "end---------------------";
		print(result);
	}
	
	private static void print(String _string) {
		updateCaller();
		if (DEBUG)
			System.out.println("ln:" + currentCaller.getLineNumber() + " "
					+ currentCaller.getClassName() + 
					(SUPER_DEBUG ? " " + currentCaller.getMethodName() : 
					"") + ": " + _string);
	}
	
	private static void updateCaller()
	{
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		currentCaller = stackTrace[4];
	}

	public static void log(float value) {
		print(""+value);
	}
}
