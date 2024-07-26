package com.example.meritmatch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.google.android.material.snackbar.Snackbar;

public class homepage extends AppCompatActivity {

    private LinearLayout tasksContainer;
    private ApiService apiService;
    private int userId;
    private String userName;
    private String userPassword;
    private int userKarmaPoints;
    private float userRep;
    private ImageView imageView;
    private TextView tvKarmaPoints,tvReputation,tvUsername;
    private DrawerLayout drawerLayout;
    private Button transactionButton,createTask,undergoingtask,transfer,tasks,share;
    private TextView textView,textView2;
    private Spinner spinnerSort;
    private Boolean selected = false;
    private int lastSelectedPosition = 0;
    private View rootView;
    float textSize = 60;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        tasksContainer = findViewById(R.id.tasksContainer);
        imageView=findViewById(R.id.profile_image);
        drawerLayout = findViewById(R.id.drawer_layout);
        tvKarmaPoints = findViewById(R.id.tv_karma_points);
        tvReputation = findViewById(R.id.tv_reputation);
        tvUsername = findViewById(R.id.tv_username);
        transactionButton = findViewById(R.id.transaction);
        createTask = findViewById(R.id.createTask);
        undergoingtask = findViewById(R.id.undergoingTask);
        transfer = findViewById(R.id.transfer);
        tasks = findViewById(R.id.tasks);
        textView = findViewById(R.id.coins);
        spinnerSort = findViewById(R.id.spinner_sort);
        share = findViewById(R.id.share);
        rootView = findViewById(R.id.layout);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.karma_ranges, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(adapter);
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != lastSelectedPosition) {
                    selected = true;
                    lastSelectedPosition = position;
                    fetchTasks();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });
        transactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchTransactions();
                spinnerSort.setVisibility(View.GONE);

                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }
            }
        });
        tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchTasks();
                spinnerSort.setVisibility(View.VISIBLE);

                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }
            }
        });
        createTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateTaskDialog();
                spinnerSort.setVisibility(View.GONE);

                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }
            }
        });
        undergoingtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInAppNotification("Click on Tasks to Chat with the required person");
                fetchMyTasks();
                spinnerSort.setVisibility(View.GONE);

                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }
            }
        });
        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchMyTaskstoTransfer();
                spinnerSort.setVisibility(View.GONE);

                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }
            }
        });
        Intent intent = getIntent();
        userId = intent.getIntExtra("userID", -1);
        userKarmaPoints = intent.getIntExtra("userKarmaPoints", 500);
        userRep = intent.getFloatExtra("userRep", 0.0f);
        userName = intent.getStringExtra("userName");
        userPassword = intent.getStringExtra("userPassword");
        apiService = ApiClient.getClient().create(ApiService.class);

        tvKarmaPoints.setText("KarmaPoints: " + String.valueOf(userKarmaPoints));
        if(userRep== 0.0f) {
            tvReputation.setText("Reputation: Null");
        }else{
            tvReputation.setText("Reputation: " + String.valueOf(userRep));
        }
        tvUsername.setText(userName);
        tvUsername.post(new Runnable() {
            @Override
            public void run() {
                float textSize = tvUsername.getTextSize();
                while (tvUsername.getPaint().measureText(userName) > tvUsername.getWidth() && textSize > 0) {
                    textSize--;
                    tvUsername.setTextSize( textSize);
                }
            }
        });
        tvUsername.setTextSize(textSize);
        tvReputation.setText("Reputation: " + String.valueOf(userRep));
        tvReputation.post(new Runnable() {
            @Override
            public void run() {
                float textSize = Math.min(30, tvReputation.getTextSize());
                while (tvReputation.getPaint().measureText(tvReputation.getText().toString()) > tvReputation.getWidth() && textSize > 0) {
                    textSize--;
                    tvReputation.setTextSize(textSize);
                }
            }
        });

        textView.setText(String.valueOf(userKarmaPoints));
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUserDetails(userKarmaPoints, userRep);
            }
        });

        fetchTasks();



    }
    private void fetchTasks() {

        apiService.getAllTasks().enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Task> tasks = response.body();
                    if(selected) {
                        List<Task> sortedTasks = sortTasksByKarmaRange(tasks);
                        displayTasks(sortedTasks);
                    }
                    else{
                        displayTasks(tasks);
                    }
                } else {
                    Toast.makeText(homepage.this, "Failed to fetch tasks", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Toast.makeText(homepage.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private List<Task> sortTasksByKarmaRange(List<Task> tasks) {
        int[] range = getSelectedKarmaRange();
        List<Task> sortedTasks = new ArrayList<>();

        for (Task task : tasks) {
            if (task.getKarmaCost() >= range[0] && task.getKarmaCost() <= range[1]) {
                sortedTasks.add(task);
            }
        }
        return sortedTasks;
    }

    private int[] getSelectedKarmaRange() {
        Spinner spinnerSort = findViewById(R.id.spinner_sort);
        String selectedRange = spinnerSort.getSelectedItem().toString();
        switch (selectedRange) {
            case "Sort by KarmaPoints":
                return new int[]{0, Integer.MAX_VALUE};
            case "1-50":
                return new int[]{1, 50};
            case "50-100":
                return new int[]{50, 100};
            case "100-200":
                return new int[]{100, 200};
            case "200-500":
                return new int[]{200, 500};
            case "500+":
                return new int[]{500, Integer.MAX_VALUE};
            default:
                return new int[]{0, Integer.MAX_VALUE};
        }
    }

    private void displayTasks(List<Task> tasks) {
        LayoutInflater inflater = LayoutInflater.from(this);
        tasksContainer.removeAllViews();

        for (Task task : tasks) {
            if(task.getChosenById()==null && task.getOwnerId() != userId) {
                View taskView = inflater.inflate(R.layout.item_task, tasksContainer, false);

                TextView taskTitle = taskView.findViewById(R.id.taskTitle);
                TextView taskDescription = taskView.findViewById(R.id.taskDescription);

                taskTitle.setText( "Karma Points: " + task.getKarmaCost());
                taskDescription.setText(task.getDescription());
                taskView.setOnClickListener(v -> chooseTask(task));


                tasksContainer.addView(taskView);
            }
        }
    }
    private void fetchMyTasks() {

        apiService.getAllTasks().enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.isSuccessful() && response.body() != null ) {
                    displayMyTasks(response.body());
                } else {
                    Toast.makeText(homepage.this, "Failed to fetch tasks", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Toast.makeText(homepage.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void displayMyTasks(List<Task> tasks) {
        LayoutInflater inflater = LayoutInflater.from(this);
        tasksContainer.removeAllViews();


        List<Task> chosenIdTasks = new ArrayList<>();
        List<Task> ownerIdTasks = new ArrayList<>();

        for (Task task : tasks) {
            if (task.getChosenById() != null && task.getChosenById() == userId) {
                chosenIdTasks.add(task);
            } else if (task.getOwnerId() == userId && task.getChosenById() != null) {
                ownerIdTasks.add(task);
            }
        }


        if (!chosenIdTasks.isEmpty()) {
            addHeader("CHOSENID TASKS");

            for (Task task : chosenIdTasks) {
                addTaskView(task, inflater);
            }
        }


        if (!ownerIdTasks.isEmpty()) {
            addHeader("OWNERID TASKS");

            for (Task task : ownerIdTasks) {
                addTaskView(task, inflater);
            }
        }
    }

    private void addHeader(String title) {
        TextView header = new TextView(this);
        header.setText(title);
        header.setTypeface(null, Typeface.BOLD);
        header.setPadding(0, 16, 0, 8);
        tasksContainer.addView(header);
    }

    private void addTaskView(Task task, LayoutInflater inflater) {
        View taskView = inflater.inflate(R.layout.item_task, tasksContainer, false);

        TextView taskTitle = taskView.findViewById(R.id.taskTitle);
        TextView taskDescription = taskView.findViewById(R.id.taskDescription);

        taskTitle.setText("Karma Points: " + task.getKarmaCost());
        taskDescription.setText(task.getDescription());
        taskView.setOnClickListener(v -> chat(task));

        tasksContainer.addView(taskView);
    }

    private void chat(Task task){
        Intent intent = new Intent(homepage.this, MessageActivity.class);
        intent.putExtra("userID", userId);
        if(task.getOwnerId() == userId) {
            intent.putExtra("otherUserID", task.getChosenById());
        } else if (task.getChosenById() == userId) {
            intent.putExtra("otherUserID", task.getOwnerId());
        }

        startActivity(intent);
    }
    private void fetchMyTaskstoTransfer() {

        apiService.getAllTasks().enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.isSuccessful() && response.body() != null ) {
                    displayMyTaskstoTransfer(response.body());
                } else {
                    Toast.makeText(homepage.this, "Failed to fetch tasks", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Toast.makeText(homepage.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void displayMyTaskstoTransfer(List<Task> tasks) {
        LayoutInflater inflater = LayoutInflater.from(this);
        tasksContainer.removeAllViews();

        for (Task task : tasks) {
            if(task.getOwnerId()==userId && task.getChosenById() != null) {
                View taskView = inflater.inflate(R.layout.item_task, tasksContainer, false);

                TextView taskTitle = taskView.findViewById(R.id.taskTitle);
                TextView taskDescription = taskView.findViewById(R.id.taskDescription);

                taskTitle.setText( "Karma Points: " + task.getKarmaCost());
                taskDescription.setText(task.getDescription());
                taskView.setOnClickListener(v -> completedTask(task));


                tasksContainer.addView(taskView);
            }
        }
    }
    private void completedTask(Task task) {

        final Dialog confirmationDialog = new Dialog(homepage.this);
        confirmationDialog.setContentView(R.layout.dialog1);
        confirmationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        confirmationDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        confirmationDialog.setCancelable(true);

        Button btnYes = confirmationDialog.findViewById(R.id.btnYes);
        Button btnNo = confirmationDialog.findViewById(R.id.btnNo);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                confirmationDialog.dismiss();

                showRatingDialog(task);

            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                confirmationDialog.dismiss();
            }
        });

        confirmationDialog.show();
    }

    private void showRatingDialog(Task task) {
        final Dialog ratingDialog = new Dialog(homepage.this);
        ratingDialog.setContentView(R.layout.dialog3);
        ratingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ratingDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ratingDialog.setCancelable(true);

        ImageView[] stars = new ImageView[5];
        stars[0] = ratingDialog.findViewById(R.id.star1);
        stars[1] = ratingDialog.findViewById(R.id.star2);
        stars[2] = ratingDialog.findViewById(R.id.star3);
        stars[3] = ratingDialog.findViewById(R.id.star4);
        stars[4] = ratingDialog.findViewById(R.id.star5);
        final int[] lastClickedRating = {0};


        for (int i = 0; i < stars.length; i++) {
            final int rating = i + 1;
            stars[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastClickedRating[0] = rating;
                    for (int j = 0; j < rating; j++) {
                        stars[j].setImageResource(R.drawable.heart);
                    }
                    for (int j = rating; j < stars.length; j++) {
                        stars[j].setImageResource(R.drawable.heartless);
                    }

                }
            });

        }

        Button btnSubmit = ratingDialog.findViewById(R.id.btnSubmit);
        Button btnNoReview = ratingDialog.findViewById(R.id.btnNoReview);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ratingDialog.dismiss();
                completeTaskWithRating(task, lastClickedRating[0]);
            }
        });

        btnNoReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ratingDialog.dismiss();
                completeTaskWithRating(task, null);
            }
        });

        ratingDialog.show();
    }

    private void completeTaskWithRating(Task task, Integer rating) {
        TaskCompleteRequest request = new TaskCompleteRequest();
        fetchMyTaskstoTransfer();
        request.setTaskId(task.getId());
        request.setUsername(userName);
        request.setPassword(userPassword);

        if (rating != null) {
            request.setReputationRating(rating);
        } else {
            request.setReputationRating(null);
        }

        apiService.completeTask(request).enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fetchMyTaskstoTransfer();
                    Toast.makeText(homepage.this, "Task completed successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(homepage.this, "Failed to complete task", Toast.LENGTH_SHORT).show();
                    Log.d("Homepage", "Response failed with code " + response.code() + " and message " + response.message());
                    try {
                        Log.d("Homepage", "Response body " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                Toast.makeText(homepage.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void chooseTask(Task task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog2, null);

        TextView taskTitle = dialogView.findViewById(R.id.taskTitle);
        TextView taskDescription = dialogView.findViewById(R.id.taskDescription);
        Button btnOkay = dialogView.findViewById(R.id.btn_okay);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        TextView karmacost = dialogView.findViewById(R.id.karmacost);
        TextView owner = dialogView.findViewById(R.id.owner);

        taskTitle.setText("Task ID: " + String.valueOf(task.getId()));
        taskDescription.setText(task.getDescription());
        karmacost.setText("Karma Points: " + String.valueOf(task.getKarmaCost()));
        owner.setText("Owner ID: " + String.valueOf(task.getOwnerId()));

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        btnOkay.setOnClickListener(v -> {
            dialog.dismiss();
            TaskChooseRequest request = new TaskChooseRequest();
            request.setTaskId(task.getId());
            request.setUsername(userName);
            request.setPassword(userPassword);

            apiService.chooseTask(request).enqueue(new Callback<Task>() {
                @Override
                public void onResponse(Call<Task> call, Response<Task> response) {
                    if (response.isSuccessful()) {
                        fetchTasks();
                        Toast.makeText(homepage.this, "Task chosen successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.d("Homepage", "Failed to choose task: " + errorBody);
                        } catch (IOException e) {
                            Toast.makeText(homepage.this, "Failed to choose task: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Task> call, Throwable t) {
                    Toast.makeText(homepage.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            fetchTasks();
        });

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }
    private void fetchTransactions() {
        apiService.getTransactionHistory(userName).enqueue(new Callback<List<Transaction>>() {
            @Override
            public void onResponse(Call<List<Transaction>> call, Response<List<Transaction>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayTransactions(response.body());
                } else {
                    Log.d("fetchTransactions", "Response failed with code " + response.code());
                    Toast.makeText(homepage.this, "Failed to fetch transactions", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Transaction>> call, Throwable t) {
                Toast.makeText(homepage.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayTransactions(List<Transaction> transactions) {
        LayoutInflater inflater = LayoutInflater.from(this);
        tasksContainer.removeAllViews();

        for (Transaction transaction : transactions) {
            View transactionView = inflater.inflate(R.layout.item_transaction, tasksContainer, false);

            TextView transactionId = transactionView.findViewById(R.id.transactionId);
            TextView transactionAmount = transactionView.findViewById(R.id.transactionAmount);
            TextView transactionDate = transactionView.findViewById(R.id.transactionDate);
            TextView transactionDescription = transactionView.findViewById(R.id.transactionDescription);

            transactionId.setText("Transaction ID: " + transaction.getId());
            transactionDate.setText("Date: " + transaction.getTimestamp().substring(0, 19));
            transactionDescription.setText("Description: " + transaction.getDescription());

            String karmaPointsText;
            if (transaction.getChosenById() == userId) {
                karmaPointsText = "+" + transaction.getKarmaPoints();
            } else if (transaction.getUserId() == userId) {
                karmaPointsText = "-" + transaction.getKarmaPoints();
            } else {
                karmaPointsText = String.valueOf(transaction.getKarmaPoints());
            }
            transactionAmount.setText(karmaPointsText);

            tasksContainer.addView(transactionView, 0);
        }
    }
    private void showCreateTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog, null);
        builder.setView(dialogView);

        EditText etDescription = dialogView.findViewById(R.id.et_description);
        EditText etKarmaCost = dialogView.findViewById(R.id.et_karma_cost);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String description = etDescription.getText().toString();
                int karmaCost = Integer.parseInt(etKarmaCost.getText().toString());
                createTask(description, karmaCost);
                fetchTasks();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createTask(String description, int karmaCost) {
        TaskCreateRequest request = new TaskCreateRequest();
        fetchTasks();
        request.setDescription(description);
        request.setKarmaCost(karmaCost);
        request.setUsername(userName);
        request.setPassword(userPassword);
        userKarmaPoints-= karmaCost;
        textView.setText( String.valueOf(userKarmaPoints));
        tvKarmaPoints.setText("Karma Points: " + userKarmaPoints);
        Log.d("CreateTaskRequest", new Gson().toJson(request));

        apiService.createTask(request).enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fetchTasks();
                    Toast.makeText(homepage.this, "Task created successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(homepage.this, "Failed to create task", Toast.LENGTH_SHORT).show();
                    Log.d("Homepage", "Response failed with code " + response.code() + " and message " + response.message());
                    try {
                        Log.d("Homepage", "Response body " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                Toast.makeText(homepage.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Homepage", "Error: " + t.getMessage());
            }
        });

    }
    private String getUserDetails(int karmaPoints, float reputation) {
        return "Check out my profile on Merit Match!\n" +
                "Karma Points: " + userKarmaPoints + "\n" +
                "Reputation: " + userRep;
    }
    private void shareUserDetails(int karmaPoints, float reputation) {
        String userDetails = getUserDetails(karmaPoints, reputation);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, userDetails);
        shareIntent.setType("text/plain");

        Intent chooser = Intent.createChooser(shareIntent, "Share your profile via");
        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        } else {
            Toast.makeText(this, "No app available to share your profile", Toast.LENGTH_SHORT).show();
        }
    }




    private void showInAppNotification(String message) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show();

    }
}