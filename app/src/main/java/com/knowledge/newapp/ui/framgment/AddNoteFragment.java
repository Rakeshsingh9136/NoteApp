package com.knowledge.newapp.ui.framgment;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.knowledge.newapp.R;
import com.knowledge.newapp.models.Entities.NoteEntity;
import com.knowledge.newapp.viewmodels.NoteViewModel;

public class AddNoteFragment extends Fragment {
    private NoteViewModel viewModel;

    public AddNoteFragment() { super(R.layout.addtext_layout); }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        EditText title = v.findViewById(R.id.titleId);
        EditText desc = v.findViewById(R.id.descriptionId);
        ImageButton save = v.findViewById(R.id.btnSaveId);

        viewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);

        Bundle bundle = getArguments();
        final int[] noteId = {-1};
        if (bundle != null) {
            noteId[0] = bundle.getInt("id");
            title.setText(bundle.getString("title"));
            desc.setText(bundle.getString("description"));
        }

        save.setOnClickListener(view -> {
            String t = title.getText().toString().trim();
            String d = desc.getText().toString().trim();

            if (!t.isEmpty()) {
                NoteEntity note = new NoteEntity(t, d, System.currentTimeMillis());
                if (noteId[0] != -1) note.setId(noteId[0]);
                if (noteId[0] != -1) viewModel.update(note);
                else viewModel.insert(note);

                Navigation.findNavController(view).navigateUp();
            }
        });
    }
}
