package com.winlator.cmod.psp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.*;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import com.winlator.cmod.R;

public class PspLibraryFragment extends Fragment {
    private LinearLayout list;
    private ActivityResultLauncher<Intent> picker;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        picker = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), r -> {
            if (r.getResultCode() == Activity.RESULT_OK && r.getData() != null && r.getData().getData() != null) {
                Uri uri = r.getData().getData();
                requireContext().getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                addGame(uri);
            }
        });
    }

    @Override public View onCreateView(LayoutInflater i, ViewGroup c, Bundle b) {
        LinearLayout root=new LinearLayout(requireContext()); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(24,20,24,20);
        TextView title=new TextView(requireContext()); title.setText("PSP · PPSSPP"); title.setTextSize(26); title.setTextColor(0xff27313b); root.addView(title);
        TextView gpu=new TextView(requireContext()); SharedGpuDriver.DriverInfo d=SharedGpuDriver.resolve(requireContext());
        gpu.setText("Vulkan compartilhado: "+d.id); gpu.setTextColor(0xff66717c); gpu.setPadding(0,8,0,16); root.addView(gpu);
        Button add=new Button(requireContext()); add.setText("＋ Adicionar ISO / CSO / PBP / CHD"); add.setOnClickListener(v -> pick()); root.addView(add);
        list=new LinearLayout(requireContext()); list.setOrientation(LinearLayout.VERTICAL); root.addView(list);
        return root;
    }

    private void pick() {
        Intent in=new Intent(Intent.ACTION_OPEN_DOCUMENT).setType("*/*").addCategory(Intent.CATEGORY_OPENABLE);
        in.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION); picker.launch(in);
    }
    private void addGame(Uri uri) {
        String name=uri.getLastPathSegment();
        try (android.database.Cursor c=requireContext().getContentResolver().query(uri,null,null,null,null)) {
            if(c!=null && c.moveToFirst()){ int x=c.getColumnIndex(OpenableColumns.DISPLAY_NAME); if(x>=0) name=c.getString(x); }
        }
        if(!PspGame.supports(name)){ Toast.makeText(requireContext(),"Formato PSP não suportado",Toast.LENGTH_SHORT).show(); return; }
        Button game=new Button(requireContext()); game.setText("▶  "+name+"   · PSP"); game.setOnClickListener(v -> PspRuntime.launch(requireContext(),uri)); list.addView(game);
    }
}
