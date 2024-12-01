package com.example.shopping_list;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainPage extends AppCompatActivity {
    Button logoutBtn;
    FirebaseAuth auth;
    FirebaseUser user;
    RecyclerView myList;
    DatabaseReference myDB;
    MyAdapter adapter;
    ArrayList<ItemModal> list;
    FloatingActionButton addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

        // Applying system bar insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainpage), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the required components
        init();

        // Check if user is authenticated
        if (user == null) {
            Intent navigator = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(navigator);
            finish();
        } else {
            // Set up RecyclerView
            myList.setLayoutManager(new LinearLayoutManager(this));
            adapter = new MyAdapter(this, list);
            myList.setAdapter(adapter);

            // Fetch data from Firebase
            myDB.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear(); // Clear the list before adding new data
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ItemModal item = dataSnapshot.getValue(ItemModal.class);
                        if (item != null) {
                            item.setKey(dataSnapshot.getKey()); // Set the key for the item
                            list.add(item);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainPage.this, "Failed to fetch data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


            // Add item functionality
            addBtn.setOnClickListener(v -> {
                AddItemDialogFragment dialog = new AddItemDialogFragment(new AddItemDialogFragment.AddItemListener() {
                    @Override
                    public void onItemAdded(String name, float price, int quantity) {
                        String id = myDB.push().getKey();
                        if (id != null) {
                            ItemModal item = new ItemModal(id, name, price, quantity);
                            myDB.child(id).setValue(item)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(MainPage.this, "Item added successfully!", Toast.LENGTH_SHORT).show();
                                            list.add(item);
                                            adapter.notifyDataSetChanged();
                                        } else {
                                            Toast.makeText(MainPage.this, "Failed to add item!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });

                // Show the AddItemDialogFragment
                dialog.show(getSupportFragmentManager(), "AddItemDialog");
            });

            // Logout functionality
            logoutBtn.setOnClickListener(view -> {
                auth.signOut();
                Toast.makeText(MainPage.this, "Signed out successfully!", Toast.LENGTH_SHORT).show();
                Intent navigator = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(navigator);
                finish();
            });
        }
    }

    private void init() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        logoutBtn = findViewById(R.id.logoutBtn);
        myList = findViewById(R.id.item_list);
        myDB = FirebaseDatabase.getInstance().getReference("items");
        list = new ArrayList<>();
        addBtn = findViewById(R.id.fab_add_btn);
    }
}
