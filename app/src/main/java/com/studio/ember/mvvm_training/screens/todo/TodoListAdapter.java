package com.studio.ember.mvvm_training.screens.todo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.studio.ember.mvvm_training.R;
import com.studio.ember.mvvm_training.database.entities.ToDo;
import com.studio.ember.mvvm_training.utils.helper.ItemTouchHelperAdapter;
import com.studio.ember.mvvm_training.utils.helper.ItemTouchHelperViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ToDoViewHolder> implements ItemTouchHelperAdapter {

    private final LayoutInflater mInflater;
    private List<ToDo> toDos; // Cached copy of words

    private TaskListener listener;

    TodoListAdapter(Context context, TaskListener listener) {
        this.mInflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.todo_item, parent, false);
        return new ToDoViewHolder(itemView);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final ToDoViewHolder holder, int position) {
        if (toDos != null) {
            holder.setItem(toDos.get(position));
            holder.iv_handle.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                        listener.onStartDrag(holder);
                    }
                    return true;
                }
            });


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

    List<ToDo> getToDos(){
        return this.toDos;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        ToDo prev = toDos.remove(fromPosition);
        prev.setTaskOrder(toPosition);

        notifyItemMoved(fromPosition, toPosition);
        toDos.add(toPosition, prev);

    }

    @Override
    public void onItemDismiss(int position) {
        listener.onItemDeleted(toDos.get(position));
    }

    interface TaskListener{
        /**
         * Called when a view is checked bt the user
         * @param todo the to-do to be updated
         */
        void onCheckedListener(ToDo todo);
        /**
         * Called when a view is requesting a start of a drag.
         *
         * @param viewHolder The holder of the view to drag.
         */
        void onStartDrag(RecyclerView.ViewHolder viewHolder);

        void onDragFinished(List<ToDo> toDos);

        void onItemDeleted(ToDo toDo);
    }

    /**
     * To-do ViewHolder
     */
    class ToDoViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        public final View view;

        @BindView(R.id.cb_todo)
        CheckBox cb_todo;

        @BindView(R.id.iv_handle)
        ImageView iv_handle;


        private ToDoViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            ButterKnife.bind(this,view);
        }

        void setItem(ToDo todo){
            this.cb_todo.setText(todo.getTask());
            this.cb_todo.setChecked(todo.isDone());
            if (todo.isDone()) {
                cb_todo.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                cb_todo.setPaintFlags(Paint.LINEAR_TEXT_FLAG);
            }


        }

        @OnCheckedChanged(R.id.cb_todo)
        void onCheckedChange(CompoundButton button, boolean checked){
            ToDo todo = toDos.get(getAdapterPosition());
            todo.setDone(checked);
            listener.onCheckedListener(toDos.get(getAdapterPosition()));
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
            listener.onDragFinished(toDos);
        }


    }



}
