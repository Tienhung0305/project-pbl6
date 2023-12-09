package com.example.sportstore06.service.MomoService;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Map<String, String> jsonToMap(String json) {
        Map<String, String> map = new HashMap<>();
        try {
            JsonNode rootNode = objectMapper.readTree(json);
            populateMap(map, rootNode, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    private static void populateMap(Map<String, String> map, JsonNode node, String currentPath) {
        Iterator<Map.Entry<String, JsonNode>> fieldsIterator = node.fields();
        while (fieldsIterator.hasNext()) {
            Map.Entry<String, JsonNode> entry = fieldsIterator.next();
            String key = currentPath.isEmpty() ? entry.getKey() : currentPath + "." + entry.getKey();
            if (entry.getValue().isObject()) {
                populateMap(map, entry.getValue(), key);
            } else {
                map.put(key, entry.getValue().asText());
            }
        }
    }
}
