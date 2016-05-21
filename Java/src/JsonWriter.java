import java.nio.file.Files;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Map;
//import java.util.HashMap;

/**
 * Created by dobatake on 5/19/16.
 */
public class JsonWriter {
	String jsonFileName = "docToPost.json";
	// Path jsonFilePath = Paths.get(Paths.get("").toAbsolutePath().toString(), jsonFileName);
	Path jsonFilePath = Paths.get(jsonFileName).toAbsolutePath();

	public void appendToJSONFile(Map<String, String> attributes) {
		String toAppend = "{";

		for(Map.Entry<String, String> entry : attributes.entrySet()) {
			toAppend += "\"" + entry.getKey() + "\":\"" + entry.getValue() + "\",";
		}

		toAppend = toAppend.substring(0, toAppend.length() - 1); // Remove the last comma in the listing object
		toAppend += "},";

		try {
			Files.write(jsonFilePath, toAppend.getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Write the opening '[' to the file
	public void openJSONFile() {
		String s = "[";

		try {
			Files.write(jsonFilePath, s.getBytes()); // Default options are CREATE, TRUNCATE_EXISTING, WRITE
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Write the closing ']' to the file
	public void closeJSONFile() {
		String str = "";
		
		byte[] fileBytes;
		try {
			fileBytes = Files.readAllBytes(jsonFilePath);
			str = new String(fileBytes, StandardCharsets.UTF_8);
			str = str.substring(0, str.length() - 1);
			str += "]";
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			Files.write(jsonFilePath, str.getBytes(), StandardOpenOption.WRITE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
/*
	public static void main(String[] args) {
		JsonWriter jw = new JsonWriter();
		HashMap<String, String> map = new HashMap<String, String>();
		HashMap<String, String> map2 = new HashMap<String, String>();
		
		map.put("test1", "myvalue");
		map.put("test2", "myvalue2");
		map.put("test13", "myvalue3");
		
		map2.put("cmon", "yesss");
		map2.put("okay", "letsgo");
		
		jw.openJSONFile();
		jw.appendToJSONFile(map);
		jw.appendToJSONFile(map2);
		jw.closeJSONFile();
	} 
*/
}