package com.example.shopping_list;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddItemDialogFragment extends DialogFragment {

    private EditText itemNameInput, itemPriceInput, itemQuantityInput;
    private Button addButton, cancelButton;

    public interface AddItemListener {
        void onItemAdded(String name, float price, int quantity);
    }

    private AddItemListener listener;

    public AddItemDialogFragment(AddItemListener listener) {
        this.listener = listener;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_dialog, container, false);

        // Initialize the EditText fields and buttons
        itemNameInput = view.findViewById(R.id.item_name_input);
        itemPriceInput = view.findViewById(R.id.item_price_input);
        itemQuantityInput = view.findViewById(R.id.item_quantity_input);
        addButton = view.findViewById(R.id.add_item_button);
        cancelButton = view.findViewById(R.id.cancel_button);

        // Handle Add Item button click
        addButton.setOnClickListener(v -> {
            String name = itemNameInput.getText().toString().trim();
            String priceInput = itemPriceInput.getText().toString().trim();
            String quantityInput = itemQuantityInput.getText().toString().trim();

            // Validate inputs
            if (name.isEmpty() || priceInput.isEmpty() || quantityInput.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                float price = Float.parseFloat(priceInput);
                int quantity = Integer.parseInt(quantityInput);

                // Pass the data back to the listener
                listener.onItemAdded(name, price, quantity);
                dismiss(); // Close the dialog
            } catch (NumberFormatException e) {
                Toast.makeText(getActivity(), "Please enter valid numeric values for price and quantity", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle Cancel button click
        cancelButton.setOnClickListener(v -> dismiss()); // Close the dialog

        return view;
    }
}
