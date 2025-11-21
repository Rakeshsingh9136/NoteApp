package com.knowledge.newapp.ui.framgment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.knowledge.newapp.R;
import com.knowledge.newapp.models.Entities.NoteEntity;
import com.knowledge.newapp.viewmodels.NoteViewModel;

public class AddNoteFragment extends Fragment {
    private NoteViewModel viewModel;
    private static final int PICK_IMAGE_REQUEST = 101;
    private EditText titleEdt, descEdt;
    private ImageButton uploadImage;


    public AddNoteFragment() { super(R.layout.addtext_layout); }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        titleEdt = v.findViewById(R.id.titleId);
        descEdt = v.findViewById(R.id.descriptionId);
        ImageButton save = v.findViewById(R.id.btnSaveId);
        uploadImage = v.findViewById(R.id.btnUploadImage);

        viewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);

        // Receive note if editing
        Bundle bundle = getArguments();
        final int[] noteId = {-1};
        if (bundle != null) {
            noteId[0] = bundle.getInt("id");
            titleEdt.setText(bundle.getString("title"));
            descEdt.setText(bundle.getString("description"));
        }

        // Upload image click
        uploadImage.setOnClickListener(view -> openGallery());

        // Save note
        save.setOnClickListener(view -> {
            String t = titleEdt.getText().toString().trim();
            String d = descEdt.getText().toString().trim();

            if (!t.isEmpty()) {
                NoteEntity note = new NoteEntity(t, d, System.currentTimeMillis());

                if (noteId[0] != -1) {
                    note.setId(noteId[0]);
                    viewModel.update(note);
                } else {
                    viewModel.insert(note);
                }

                Navigation.findNavController(view).navigateUp();
            }
        });
    }




    // ============================
    //   IMAGE PICKER
    // ============================
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    // ============================
    //   ON RESULT - OCR PROCESS
    // ============================
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                Bitmap bitmap = uriToBitmap(imageUri);
                extractText(bitmap);
            }
        }
    }


    // Convert URI → Bitmap
    private Bitmap uriToBitmap(Uri uri) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.Source source = ImageDecoder.createSource(requireActivity().getContentResolver(), uri);
                return ImageDecoder.decodeBitmap(source);
            } else {
                return MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // ============================
    //     ML KIT OCR
    // ============================
    private void extractText(Bitmap bitmap) {
        if (bitmap == null) return;

        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        InputImage image = InputImage.fromBitmap(bitmap, 0);

        recognizer.process(image)
                .addOnSuccessListener(result -> {
                    String fullText = result.getText().trim();

                    if (!fullText.isEmpty()) {
                        String[] lines = fullText.split("\n", 2);

                        // First line = Title
                        titleEdt.setText(lines[0]);

                        // Remaining text = Description
                        if (lines.length > 1)
                            descEdt.setText(lines[1]);
                    }
                })
                .addOnFailureListener(e -> {
                    descEdt.setText("Error reading text");
                });
    }
}
