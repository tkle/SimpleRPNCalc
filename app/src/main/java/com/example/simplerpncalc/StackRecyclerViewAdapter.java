package com.example.simplerpncalc;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Stack;

public class StackRecyclerViewAdapter extends RecyclerView.Adapter<StackRecyclerViewAdapter.ViewHolder> {
    private ArrayListStack stack;

    public StackRecyclerViewAdapter(ArrayListStack stack) {
        this.stack = stack;
    }

    /**
     * Called when RecyclerView needs a new ViewHolder an item in the stack
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of R.layout.stack_cell
     * @see #onBindViewHolder(ViewHolder, int)
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.stack_cell,
                parent,
                false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    /**
     * Called by RecyclerView to display the stack value at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the stack.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.stackIndexTextView.setText(":" + (position + 1));

        double d = ((Double)stack.get(position)).doubleValue();
        // Display the number in localized format
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(340); //340 = DecimalFormat.DOUBLE_FRACTION_DIGITS
        holder.stackValueEditText.setText(df.format(d));
    }

    /**
     * Returns the total number of items in the stack.
     *
     * @return The total number of items in the stack.
     */
    @Override
    public int getItemCount() {
        return (stack != null) ? stack.size() : 0;
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {
        public TextView stackIndexTextView;
        private TextView stackValueEditText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            stackIndexTextView = itemView.findViewById(R.id.stackIndexTextView);
            stackValueEditText = itemView.findViewById(R.id.stackValueEditText);
        }
    }
}
