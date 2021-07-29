package com.example.simplerpncalc;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;

public class MainFragment extends Fragment {
    public static final String DEBUG_TAG = "MainFragment";
    public static final int OPERATION_DIVIDE = 0;
    public static final int OPERATION_MULTIPLY = 1;
    public static final int OPERATION_SUBTRACT= 2;
    public static final int OPERATION_ADD = 3;
    public static final int OPERATION_NEGATE = 4;
    public static final int OPERATION_PERCENT = 5;
    public static final int OPERATION_PERCENT_ADD = 6;
    public static final int OPERATION_PERCENT_SUBTRACT = 7;

    ArrayListStack stack = new ArrayListStack();

    Button enterButton, clearStackButton, deleteButton, eraseButton;
    Button negateButton, percentButton, percentAddButton, percentSubtractButton;
    Button zeroButton, oneButton, twoButton, threeButton, fourButton, fiveButton, sixButton, sevenButton, eightButton, nineButton;
    Button divideButton, multiplyButton, subtractButton, addButton;
    Button decimalPointButton;

    EditText inputEditText;

    RecyclerView stackRecyclerView;
    private StackRecyclerViewAdapter stackRecyclerViewAdapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        stackRecyclerView = view.findViewById(R.id.stackRecyclerView);

        stackRecyclerViewAdapter = new StackRecyclerViewAdapter(stack);
        stackRecyclerView.setAdapter(stackRecyclerViewAdapter);
        stackRecyclerView.setHasFixedSize(true);
        stackRecyclerView.setNestedScrollingEnabled(false);


        // Allow users to drag/drop and swipe items in RecyclerView
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.DOWN | ItemTouchHelper.UP,
                ItemTouchHelper.START | ItemTouchHelper.END) {
            int fromPosition = 0;
            int toPosition = 0;

            // Remove item from stack when swipe left or right
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // remove item from adapter
                int position = viewHolder.getAdapterPosition();
                stack.remove(position);
                stackRecyclerViewAdapter.notifyDataSetChanged();
            }

            // Drag & drop - Allow user to re-position an item in stack
            @Override
            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                fromPosition = viewHolder.getAdapterPosition();
                toPosition = target.getAdapterPosition();
                // move item in `fromPos` to `toPos` in adapter.
                Collections.swap(stack, fromPosition, toPosition);
                stackRecyclerViewAdapter.notifyItemMoved(fromPosition, toPosition);
                return true;// true if moved, false otherwise
            }

            // Change background color when select item to drag&drop or swipe
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY,
                        actionState, isCurrentlyActive);
                if (isCurrentlyActive)
                    viewHolder.itemView.setBackgroundResource(R.color.light_gray);
            }

            // Reset background color when drag&drop or swipe finish
            @Override
            public void clearView(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                viewHolder.itemView.setBackgroundResource(android.R.color.transparent);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(stackRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        // Display stack in reverse order
        linearLayoutManager.setReverseLayout(true);
        //linearLayoutManager.setStackFromEnd(true);
        stackRecyclerView.setLayoutManager(linearLayoutManager);

        // draw dividers between list items
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                stackRecyclerView.getContext(),
                linearLayoutManager.getOrientation());
        stackRecyclerView.addItemDecoration(dividerItemDecoration);

        enterButton = view.findViewById(R.id.enterButton);
        clearStackButton = view.findViewById(R.id.clearAllButton);
        deleteButton = view.findViewById(R.id.deleteButton);
        eraseButton = view.findViewById(R.id.eraseButton);

        negateButton = view.findViewById(R.id.negateButton);
        percentButton = view.findViewById(R.id.percentButton);
        percentAddButton = view.findViewById(R.id.percentAddButton);
        percentSubtractButton = view.findViewById(R.id.percentSubtractButton);

        zeroButton = view.findViewById(R.id.zeroButton);
        oneButton = view.findViewById(R.id.oneButton);
        twoButton = view.findViewById(R.id.twoButton);
        threeButton = view.findViewById(R.id.threeButton);
        fourButton = view.findViewById(R.id.fourButton);
        fiveButton = view.findViewById(R.id.fiveButton);
        sixButton = view.findViewById(R.id.sixButton);
        sevenButton = view.findViewById(R.id.sevenButton);
        eightButton = view.findViewById(R.id.eightButton);
        nineButton = view.findViewById(R.id.nineButton);

        divideButton = view.findViewById(R.id.divideButton);
        multiplyButton = view.findViewById(R.id.multiplyButton);
        subtractButton = view.findViewById(R.id.subtractButton);
        addButton = view.findViewById(R.id.addButton);

        decimalPointButton = view.findViewById(R.id.decimalPointButton);
        // Set localized decimal point character
        String decimalSeparator = String.valueOf(DecimalFormatSymbols.getInstance().getDecimalSeparator());
        decimalPointButton.setText(decimalSeparator);

        // Set click listeners to all the buttons
        Button[] buttonList = {enterButton, clearStackButton, deleteButton, eraseButton,
                negateButton, percentButton, percentAddButton, percentSubtractButton,
                zeroButton, oneButton, twoButton, threeButton, fourButton, fiveButton, sixButton, sevenButton, eightButton, nineButton,
                divideButton, multiplyButton, subtractButton, addButton,
                decimalPointButton};
        for (Button b: buttonList) {
            b.setOnClickListener(buttonListener);
        }

        inputEditText = view.findViewById(R.id.inputEditText);

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * Called when any button clicked.
     */
    private final View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();

            /*
            * Use if/else.  switch statement might not work.
            * See http://tools.android.com/tips/non-constant-fields
            */
            /*
            Push value to stack
             */
            if (id == R.id.enterButton) {
                String str = inputEditText.getText().toString();
                // Error if nothing to push to stack
                if (str.length() == 0) {
                    if (! stack.isEmpty()) {
                        // Add a duplicate value to stack
                        stack.push(stack.peeks());
                        stackRecyclerViewAdapter.notifyDataSetChanged();
                        return;
                    } else {
                        String errorMsg = getString(R.string.error_too_few_arg, getString(R.string.enter));
                        showError(errorMsg);
                        return;
                    }
                }

                try {
                    // Note: Double.valueOf() cannot parse localized number format
                    Double value = DecimalFormat.getNumberInstance().parse(str).doubleValue();
                    stack.push(value);
                    stackRecyclerViewAdapter.notifyDataSetChanged();
                    inputEditText.setText("");
                } catch (ParseException exception) { // This shouldn't happen as we restrict input characters.
                    showError(exception.getLocalizedMessage());
                    Log.e(DEBUG_TAG, exception.toString());
                    return;
                } catch (NullPointerException exception) {
                    Log.e(DEBUG_TAG, exception.toString());
                    return;
                }
                return;

            /*
            Clear all
             */
            } else if (id == R.id.clearAllButton) {
                inputEditText.setText("");
                stack.clear();
                stackRecyclerViewAdapter.notifyDataSetChanged();
                return;

            /*
            Delete value in input field
             */
            } else if (id == R.id.deleteButton) {
                inputEditText.setText("");
                return;

            /*
            Back
             */
            } else if (id == R.id.eraseButton) {
                int length = inputEditText.length();
                if (length > 0)
                    inputEditText.setText(inputEditText.getText().subSequence(0, length-1));
                return;

            /*
            Negate
             */
            } else if (id == R.id.negateButton) {
                doOperation(OPERATION_NEGATE, 1);
                return;

            // Percent
            } else if (id == R.id.percentButton) {
                doOperation(OPERATION_PERCENT, 2);
                return;

            // Add percentage
            } else if (id == R.id.percentAddButton) {
                doOperation(OPERATION_PERCENT_ADD, 2);
                return;

            // Subtract percentage
            } else if (id == R.id.percentSubtractButton) {
                doOperation(OPERATION_PERCENT_SUBTRACT, 2);
                return;

            /*
            Digits
             */
            } else if (id == R.id.zeroButton) {
                inputEditText.append("0");
                return;
            } else if (id == R.id.oneButton) {
                inputEditText.append("1");
                return;
            } else if (id == R.id.twoButton) {
                inputEditText.append("2");
                return;
            } else if (id == R.id.threeButton) {
                inputEditText.append("3");
                return;
            } else if (id == R.id.fourButton) {
                inputEditText.append("4");
                return;
            } else if (id == R.id.fiveButton) {
                inputEditText.append("5");
                return;
            } else if (id == R.id.sixButton) {
                inputEditText.append("6");
                return;
            } else if (id == R.id.sevenButton) {
                inputEditText.append("7");
                return;
            } else if (id == R.id.eightButton) {
                inputEditText.append("8");
                return;
            } else if (id == R.id.nineButton) {
                inputEditText.append("9");
                return;

            /*
            Divide
             */
            } else if (id == R.id.divideButton) {
                doOperation(OPERATION_DIVIDE, 2);
                return;

            /*
            Multiply
             */
            } else if (id == R.id.multiplyButton) {
                doOperation(OPERATION_MULTIPLY, 2);
                return;

            /*
            Subtract
             */
            } else if (id == R.id.subtractButton) {
                doOperation(OPERATION_SUBTRACT, 2);
                return;

            /*
            Add
             */
            } else if (id == R.id.addButton) {
                doOperation(OPERATION_ADD, 2);
                return;

            /*
             Decimal point
             */
            } else if (id == R.id.decimalPointButton) {
                // Note: Depending on locale, decimal point is either "." or ","
                String decimalSeparator = String.valueOf(DecimalFormatSymbols.getInstance().getDecimalSeparator());
                if (! inputEditText.getText().toString().contains(decimalSeparator)) {
                    inputEditText.append(decimalSeparator);
                } else {
                    // Error - already has a decimal point in input field
                    String errorMsg = getString(R.string.error_syntax);
                    showError(errorMsg);
                }
                return;
            }
        }
    };

    /*
    Return true if stack + input field (if exist) have enough of operands
     */
    private boolean hasEnoughOperand(int numberOfOperand) {
        int count = 0;
        if (inputEditText.getText().length() > 0)
            count++;

        if (count + stack.size() >= numberOfOperand)
            return true;

        return false;
    }

    /*
    Do the calculation
     */
    private boolean doOperation(int typeOperation, int requireNumberOfOperand) {
        Double operand1 = null;
        Double operand2 = null;

        if (requireNumberOfOperand != 1 && requireNumberOfOperand != 2) {
            Log.e(DEBUG_TAG, "Invalid requireNumberOfOperand value in doOperation()");
            return false;
        }

        // Get operand1 from input field
        String inputString = inputEditText.getText().toString();
        if (inputString.length() != 0) {
            try {
                operand1 = Double.valueOf(inputString);
            } catch (NumberFormatException exception) { // This shouldn't happen as we restrict input characters.
                Log.e(DEBUG_TAG, exception.toString());
                return false;
            }
        }

        if (operand1 != null) {
            if (requireNumberOfOperand > 1) {
                if (stack.size() >= 1) {
                    // Get 2nd operand from stack
                    operand2 = stack.pop();
                } else {
                    // Error - no 2nd operand on stack
                    String errorMsg = getString(R.string.error_too_few_arg, operationTypeToString(typeOperation));
                    showError(errorMsg);
                    return false;
                }
            }
        } else { // operand1 == null
            if (requireNumberOfOperand == 1) {
                if (stack.size() >= 1) {
                    // Get 1st operand from stack
                    operand1 = stack.pop();
                } else {
                    // Error - not enough operands on stack
                    String errorMsg = getString(R.string.error_too_few_arg, operationTypeToString(typeOperation));
                    showError(errorMsg);
                    return false;
                }
            } else { // requireNumberOfOperand > 1
                if (stack.size() >= 2) {
                    // Get 2 operands from stack
                    operand1 = stack.pop();
                    operand2 = stack.pop();
                } else {
                    // Error - not enough operands on stack
                    String errorMsg = getString(R.string.error_too_few_arg, operationTypeToString(typeOperation));
                    showError(errorMsg);
                    return false;
                }
            }
        }

        if (requireNumberOfOperand == 1 && operand1 != null) {
            switch (typeOperation) {
                case OPERATION_NEGATE:
                    // If there is value in input field
                    String str = inputEditText.getText().toString();
                    if (str.length() > 0) {
                        if (str.startsWith("-")) {
                            // Change - to + by remove first character
                            inputEditText.setText(str.substring(1));
                        } else {
                            // Change + to -
                            StringBuilder stringBuilder = new StringBuilder(str);
                            stringBuilder.insert(0, '-');
                            inputEditText.setText(stringBuilder);
                        }
                    } else {
                        operand1 = operand1 * -1;
                        stack.push(operand1);
                        stackRecyclerViewAdapter.notifyDataSetChanged();
                    }
                    break;
            }
            return true;
        } else if (requireNumberOfOperand >= 2 && operand1 != null && operand2 != null) {
            switch (typeOperation) {
                case OPERATION_DIVIDE:
                    // Error - Cannot divide by 0
                    if (operand1 == 0.0) {
                        String errorMsg = getString(R.string.error_divide_by_zero, getString(R.string.divide));
                        showError(errorMsg);

                        // Push original values back to stack
                        stack.push(operand2);
                        if (inputString.length() == 0)
                            stack.push(operand1);
                        return false;
                    }
                    stack.push(operand2 / operand1);
                    inputEditText.setText("");
                    break;
                case OPERATION_MULTIPLY:
                    // TODO - need to catch arithmetic overflow
                    stack.push(operand1 * operand2);
                    inputEditText.setText("");
                    break;
                case OPERATION_ADD:
                    // TODO - need to catch arithmetic overflow
                    stack.push(operand1 + operand2);
                    inputEditText.setText("");
                    break;
                case OPERATION_SUBTRACT:
                    stack.push(operand2 - operand1);
                    inputEditText.setText("");
                    break;
                case OPERATION_PERCENT:
                    stack.push(operand2 * operand1/100.0);
                    inputEditText.setText("");
                    break;
                case OPERATION_PERCENT_ADD:
                    // TODO - need to catch arithmetic overflow
                    stack.push(operand2 + (operand2 * operand1/100.0));
                    inputEditText.setText("");
                    break;
                case OPERATION_PERCENT_SUBTRACT:
                    stack.push(operand2 - (operand2 * operand1/100.0));
                    inputEditText.setText("");
                    break;
            }
            stackRecyclerViewAdapter.notifyDataSetChanged();
            return true;
        } else {
            Log.e(DEBUG_TAG, "Unexpected error.  operand1 or/and operand2 are null.");
        }
        return false;
    }

    /*
    Display error message to user
     */
    private void showError(String msg) {
        Snackbar snackbar = Snackbar.make(getView(), msg, Snackbar.LENGTH_LONG);
        snackbar.setAction("Action", null);
        snackbar.setTextColor(Color.BLACK);
        snackbar.setBackgroundTint(Color.RED);
        snackbar.show();
    }

    /*
    Return the String of the operation type
     */
    private String operationTypeToString(int typeOperation) {
        switch (typeOperation) {
            case OPERATION_NEGATE:
                return getString(R.string.negate);
            case OPERATION_DIVIDE:
                return getString(R.string.divide);
            case OPERATION_MULTIPLY:
                return getString(R.string.multiple);
            case OPERATION_ADD:
                return getString(R.string.add);
            case OPERATION_SUBTRACT:
                return getString(R.string.subtract);
            case OPERATION_PERCENT:
                return getString(R.string.percent);
            case OPERATION_PERCENT_ADD:
                return getString(R.string.percent_add);
            case OPERATION_PERCENT_SUBTRACT:
                return getString(R.string.percent_subtract);
        }

        return "Unknown type";
    }
}