package com.heinrichreimer.inquiry.demo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.heinrichreimer.inquiry.demo.model.Person;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    public MainAdapter() {
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
        notifyDataSetChanged();
    }

    private Person[] persons;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Person person = persons[position];
        holder.text.setText(person.toString());
    }

    @Override
    public int getItemCount() {
        return persons != null ? persons.length : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }
}