/**
 * Created by dobatake on 5/19/16.
 */
public class JsonWriter {
	String jsonFileName = "docToPost.json";
	Path absPath = ;
	// Path jsonFilePath = Paths.get(Paths.get("").toAbsolutePath().toString(), jsonFileName);
	Path jsonFilePath = Paths.get(jsonFileName).toAbsolutePath();

	public void appendToJSONFile(Map<String, String> attributes) {
		String toAppend = "{";

		for(Map.Entry<String, String> entry : attributes.entrySet()) {
			toAppend.append("\"" + entry.getKey() + "\":\"" + entry.getValue() + "\",");
		}

		toAppend.substring(0, toAppend.length() - 1); // Remove the last comma in the listing object
		toAppend.append("},");

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
		String s = "]";

		// TODO: remove the extra comma at the end before the ']'

		try {
			Files.write(jsonFilePath, s.getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}