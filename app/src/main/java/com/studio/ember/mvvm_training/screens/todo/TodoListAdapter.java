package com.studio.ember.mvvm_training.screens.todo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.studio.ember.mvvm_training.R;
import com.studio.ember.mvvm_training.database.entities.ToDo;
import com.studio.ember.mvvm_training.utils.helper.ItemTouchHelperAdapter;
import com.studio.ember.mvvm_training.utils.helper.ItemTouchHelperViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnTextChanged;

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
        Log.d("moving ToDo", prev.getTask());
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
         * Called when a view is requesting a start of a drag.
         *
         * @param viewHolder The holder of the view to drag.
         */
        void onStartDrag(RecyclerView.ViewHolder viewHolder);

        void onItemDeleted(ToDo toDo);
    }

    /**
     * To-do ViewHolder
     */
    class ToDoViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        public final View view;

        @BindView(R.id.cb_todo)
        CheckBox cb_todo;

        @BindView(R.id.et_todo_task)
        EditText et_todo_task;

        @BindView(R.id.iv_handle)
        ImageView iv_handle;


        private ToDoViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            ButterKnife.bind(this,view);
        }

        void setItem(ToDo todo){
            this.cb_todo.setChecked(todo.isDone());
            this.et_todo_task.setText(todo.getTask());
        }

        @OnCheckedChanged(R.id.cb_todo)
        void onCheckedChange(CompoundButton button, boolean checked){
            ToDo todo = toDos.get(getAdapterPosition());
            todo.setDone(checked);
            this.et_todo_task.setEnabled(!todo.isDone());
            paintFlag(et_todo_task, checked);
        }

        @OnTextChanged(value = R.id.et_todo_task)
        void setTaskText(Editable editable){
            toDos.get(getAdapterPosition()).setTask(editable.toString());
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
            itemView.setBackgroundTintMode(PorterDuff.Mode.DARKEN);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }

    /**
     * Method to change the text decorator
     * It will strike through the text if the check is marked,
     * otherwise it will leave the text as it is.
     * @param button the button checked inside the list
     * @param checked the state of the button
     */
    private void paintFlag(EditText button, boolean checked) {
        if (checked) {
            button.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            button.setPaintFlags(Paint.LINEAR_TEXT_FLAG);
        }
    }


}
