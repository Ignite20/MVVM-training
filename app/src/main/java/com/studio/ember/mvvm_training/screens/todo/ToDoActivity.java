package com.studio.ember.mvvm_training.screens.todo;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.studio.ember.mvvm_training.R;
import com.studio.ember.mvvm_training.database.entities.ToDo;
import com.studio.ember.mvvm_training.utils.helper.SimpleItemTouchHelperCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ToDoActivity extends AppCompatActivity implements TodoListAdapter.TaskListener{

    // region Views
    @BindView(R.id.rv_todo_list)
    RecyclerView rv_todo_list;

    @BindView(R.id.btn_add_todo)
    ImageButton btn_add_todo;

    @BindView(R.id.et_add_todo)
    EditText et_add_todo;
    // endregion

    private TodoListAdapter adapter;
    private TodoViewModel todoViewModel;
    private LinearLayoutManager linearLayoutManager;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind the view
        ButterKnife.bind(this);
        et_add_todo.clearFocus();
        // Set the recyclerview
        adapter = new TodoListAdapter(this, this);
        this.linearLayoutManager = new LinearLayoutManager(this);
        rv_todo_list.setAdapter(adapter);
        rv_todo_list.setLayoutManager(this.linearLayoutManager);


        // Set ItemTouchHelper
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(rv_todo_list);

        // Setup the viewmodel
        todoViewModel = ViewModelProviders.of(this).get(TodoViewModel.class);

        todoViewModel.getmToDos().observe(this, new Observer<List<ToDo>>() {
            @Override
            public void onChanged(@Nullable List<ToDo> toDos) {
                adapter.setToDos(toDos);
            }
        });

        btn_add_todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String todoTask = et_add_todo.getText().toString().trim();
                if(!todoTask.isEmpty()) {
                    et_add_todo.setText("");
                    ToDo newTodo = new ToDo(todoTask);
                    newTodo.setDone(false);
                    newTodo.setTaskOrder(todoViewModel.getmToDos().getValue().size());
                    todoViewModel.insert(newTodo);
                }
            }
        });
        // Hide Keyboard when resuming
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }


    @Override
    public void onItemDeleted(ToDo toDo) {
        todoViewModel.delete(toDo);
    }



    @Override
    protected void onStop() {
        hideKeyboard(rv_todo_list);
        todoViewModel.update(adapter.getToDos());
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            clearFocus(event);
        }
        else if(event.getAction() == MotionEvent.ACTION_SCROLL){
            clearFocus(event);
        }


        return super.dispatchTouchEvent(event);
    }

    private void clearFocus(MotionEvent event) {
        View v = getCurrentFocus();
        if ( v instanceof EditText) {
            if (!isPointInsideView(event.getRawX(), event.getRawY(), v)) {
                Log.i("ET", "!isPointInsideView");

                Log.i("ET", "dispatchTouchEvent clearFocus");
                v.clearFocus();
                hideKeyboard(v);
            }
        }else if(v instanceof RecyclerView){
            if (isPointInsideView(event.getRawX(), event.getRawY(), v)) {
                Log.i("RV", "!isPointInsideView");

                Log.i("RV", "dispatchTouchEvent clearFocus");

                hideKeyboard(v);
            }
        }

    }

    private void hideKeyboard(View v) {
        if(v != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.RESULT_SHOWN);

        }
    }

    private boolean isPointInsideView(float rawX, float rawY, View v) {
        if(v.getX() == rawX && v.getY() == rawY)
            return true;
        else
            return false;
    }
}
