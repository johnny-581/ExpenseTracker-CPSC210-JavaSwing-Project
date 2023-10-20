package persistence;

import org.json.JSONObject;

// Adapted from JsonSerializationDemo
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
