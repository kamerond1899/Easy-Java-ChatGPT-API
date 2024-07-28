package easy_chatgptapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class Easy_ChatGPTAPI {

    private final String apiKey;

    public Easy_ChatGPTAPI(String apiKey) {
        this.apiKey = apiKey;
    }


      public String getChatGPTResponse(String systemMessage, String userMessage) {
        String url = "https://api.openai.com/v1/chat/completions"; //API endpoint

        String model = "gpt-4o-mini-2024-07-18"; //4o NEW model. Other models available on OpenAI website
    
    try {
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + apiKey);
        connection.setRequestProperty("Content-Type", "application/json");

        JSONArray messages = new JSONArray();
        messages.put(new JSONObject().put("role", "system").put("content", systemMessage));
        messages.put(new JSONObject().put("role", "user").put("content", userMessage));

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", model);
        requestBody.put("messages", messages);

        String body = requestBody.toString();

        connection.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
        writer.write(body);
        writer.flush();
        writer.close();

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();

        while ((line = br.readLine()) != null) {
            response.append(line);
        }
        br.close();

        return extractMessageFromJSONResponse(response.toString());

    } catch (IOException e) {
        e.printStackTrace();
        return "Error: " + e.getMessage();
    }
}

    private String extractMessageFromJSONResponse(String response) {
        int start = response.indexOf("content") + 11;
        int end = response.indexOf("\"", start);
        return response.substring(start, end);
    }
}
