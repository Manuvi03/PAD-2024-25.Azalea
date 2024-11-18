package es.ucm.fdi.azalea.presentation.parent;

import android.os.Bundle;
import android.widget.CalendarView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import es.ucm.fdi.azalea.R;

public class ParentActivity extends AppCompatActivity {

    private ParentViewModel parentViewModel;
    private RecyclerView recyclerView;
    private EventsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.parent_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.parentActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CalendarView calendarView = findViewById(R.id.parent_calendarView);
        long today = System.currentTimeMillis();
        calendarView.setDate(today, true, true);

        parentViewModel =new ParentViewModel();
        recyclerView = findViewById(R.id.parent_recyclerView);
        calendarView.setOnDateChangeListener((calendar1, year, month, day) -> {



        });
    }
}
