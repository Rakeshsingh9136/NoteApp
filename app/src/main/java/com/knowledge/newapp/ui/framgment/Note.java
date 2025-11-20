package com.knowledge.newapp.ui.framgment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.knowledge.newapp.R;
import com.knowledge.newapp.adapter.NoteAdapter;
import com.knowledge.newapp.databinding.FragmentNoteBinding;
import com.knowledge.newapp.models.Entities.NoteEntity;
import com.knowledge.newapp.viewmodels.NoteViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Note#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Note extends Fragment {
    private NoteViewModel viewModel;
    private NoteAdapter adapter;
    private FragmentNoteBinding binding;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Note() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Note.
     */
    // TODO: Rename and change types and number of parameters
    public static Note newInstance(String param1, String param2) {
        Note fragment = new Note();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_note, container, false);
        binding=FragmentNoteBinding.inflate(inflater,container,false);

        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView rv = view.findViewById(R.id.noteRecyclerView);
        FloatingActionButton addBtn = view.findViewById(R.id.addNoteBtn);

        adapter = new NoteAdapter();
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);

        viewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);
        viewModel.getAllNotes().observe(getViewLifecycleOwner(), notes -> adapter.setNotes(notes));

        // Click to Edit
        adapter.setOnItemClickListener(note -> {
            Bundle bundle = new Bundle();
            bundle.putInt("id", note.getId());
            bundle.putString("title", note.getTitle());
            bundle.putString("description", note.getDescription());
            Navigation.findNavController(view)
                    .navigate(R.id.Notes_to_AddNote, bundle);
        });

        // Swipe to delete
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) { return false; }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                NoteEntity note = adapter.getNotes().get(pos);
                viewModel.delete(note);
                Toast.makeText(getContext(), "Note deleted", Toast.LENGTH_SHORT).show();
            }
        };
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(rv);

        // Add Note
        addBtn.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.Notes_to_AddNote)
        );
    }
}