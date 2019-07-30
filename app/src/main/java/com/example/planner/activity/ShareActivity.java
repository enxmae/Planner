package com.example.planner.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.planner.NetworkService;
import com.example.planner.R;
import com.example.planner.dao.PermissionRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShareActivity extends AppCompatActivity {

    private TextView shareTokenTextView;

    private CheckBox readCheckBox;
    private CheckBox updateChekBox;
    private CheckBox deleteCheckBox;

    private Button shareButton;

    private Long eventId;
    private Long patternId;

    private final String entityTypeEvent = "EVENT";
    private final String entityTypePattern = "PATTERN";

    private final String actionRead = "READ";
    private final String actionUpdate = "UPDATE";
    private final String actionDelete = "DELETE";

    private String userToken = "serega_mem";

    private NetworkService networkService = NetworkService.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        Intent intent = getIntent();
        eventId = Long.parseLong(intent.getStringExtra("eventId"));
        patternId = Long.parseLong(intent.getStringExtra("patternId"));

        shareTokenTextView = findViewById(R.id.shareToken);
        shareButton = findViewById(R.id.shareButton);
        readCheckBox = findViewById(R.id.readCheckBox);
        updateChekBox = findViewById(R.id.updateCheckBox);
        deleteCheckBox = findViewById(R.id.deleteCheckBox);

    }

    private PermissionRequest[] createPermissions() {
        List<PermissionRequest> permissionRequestList = new ArrayList<>();

        if(readCheckBox.isChecked()) {
            PermissionRequest permissionRequestEvent =
                    new PermissionRequest(actionRead, eventId, entityTypeEvent);

            PermissionRequest permissionRequestPattern =
                    new PermissionRequest(actionRead, patternId, entityTypePattern);

            permissionRequestList.add(permissionRequestEvent);
            permissionRequestList.add(permissionRequestPattern);

        }

        if(updateChekBox.isChecked()) {
            PermissionRequest permissionRequestEvent =
                    new PermissionRequest(actionUpdate, eventId, entityTypeEvent);

            PermissionRequest permissionRequestPattern =
                    new PermissionRequest(actionUpdate, patternId, entityTypePattern);

            permissionRequestList.add(permissionRequestEvent);
            permissionRequestList.add(permissionRequestPattern);
        }

        if(deleteCheckBox.isChecked()) {
            PermissionRequest permissionRequestEvent =
                    new PermissionRequest(actionDelete, eventId, entityTypeEvent);

            PermissionRequest permissionRequestPattern =
                    new PermissionRequest(actionDelete, patternId, entityTypePattern);

            permissionRequestList.add(permissionRequestEvent);
            permissionRequestList.add(permissionRequestPattern);
        }

        PermissionRequest[] permissionRequests = new PermissionRequest[permissionRequestList.size()];
        for(int i = 0; i < permissionRequestList.size(); i++) {
            permissionRequests[i] = permissionRequestList.get(i);
        }
        return permissionRequests;
    }

    public void share(View view) {
        PermissionRequest[] permissionRequests = createPermissions();

        networkService
                .getPermissionRepository()
                .generateLinkForSharing(permissionRequests, userToken)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                             shareTokenTextView.setText(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });

    }



}
