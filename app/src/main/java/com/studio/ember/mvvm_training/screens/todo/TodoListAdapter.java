package com.studio.ember.mvvm_training.screens.todo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.studio.ember.mvvm_training.R;
import com.studio.ember.mvvm_training.database.entities.ToDo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ToDoViewHolder> {

    private final LayoutInflater mInflater;
    private List<ToDo> toDos; // Cached copy of words


    TodoListAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.todo_item, parent, false);
        return new ToDoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        if (toDos != null) {
            ToDo current = toDos.get(position);
            holder.cb_todo.setText(current.getTask());
        } else {
            // Covers the case of data not being ready yet.
            holder.cb_todo.setText("No todo");
        }
    }

    @Override
    public int getItemCount() {
        if(toDos != null){
            return toDos.size();
        }
        return 0;
    }

    void setToDos(List<ToDo> toDoList){
        toDos = toDoList;
        notifyDataSetChanged();
    }

    /**
     * To-do ViewHolder
     */
    class ToDoViewHolder extends RecyclerView.ViewHolder {

        private View view;

        @BindView(R.id.cb_todo)
        TextView cb_todo;


        private Unbinder unbinder;
        private ToDoViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            unbinder = ButterKnife.bind(this,view);
        }
    }



}
