package com.example.meritmatch;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {

    private ListView messageListView;
    private EditText composeMessage;
    private Button sendMessage;
    private ArrayAdapter<String> adapter;
    private List<String> messageList = new ArrayList<>();
    private int userId;
    private int otherUserId;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        messageListView = findViewById(R.id.messageListView);
        composeMessage = findViewById(R.id.composeMessage);
        sendMessage = findViewById(R.id.sendMessage);

        Intent intent = getIntent();
        userId = intent.getIntExtra("userID", -1);
        otherUserId = intent.getIntExtra("otherUserID", -1);

        apiService = ApiClient.getClient().create(ApiService.class);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messageList);
        messageListView.setAdapter(adapter);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageContent = composeMessage.getText().toString();
                if (!messageContent.isEmpty()) {
                    sendMessage(userId, otherUserId, messageContent);
                }
            }
        });

        fetchMessages();
    }

    private void sendMessage(int senderId, int receiverId, String content) {
        Message message = new Message();
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setContent(content);

        apiService.sendMessage(message).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful() && response.body() != null) {
                    composeMessage.setText("");
                    fetchMessages();
                } else {

                    Toast.makeText(MessageActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Toast.makeText(MessageActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchMessages() {
        apiService.getMessages(userId, otherUserId).enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    messageList.clear();
                    List<Message> messages = response.body();
                    for (int i = Math.max(0, messages.size() - 15); i < messages.size(); i++) {
                        Message message = messages.get(i);
                        String messageText = (message.getSenderId() == userId? "You: " : "Other: ") + message.getContent();
                        messageList.add(messageText);

                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MessageActivity.this, "Failed to fetch messages", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Toast.makeText(MessageActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
