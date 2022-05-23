package aleksandar.petrovski.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Random;

public class StatisticsActivity extends AppCompatActivity {
    private Button refreshButton;

    private void addRandomScore(User user, int howMany) {
        Random random = new Random();
        for (int i = 1; i <= howMany ; ++i) {
            user.addScore(random.nextInt(i * 9) - 120);
        }
        user.updateBestWorstScore();
    }

    private void addLinearScore(User user, int howMany) {
        for (int i = 0; i < howMany; ++i) {
            user.addScore(i);
        }
        user.updateBestWorstScore();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        refreshButton = findViewById(R.id.refreshstatistics);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // tood refresh
            }
        });


        UserAdapter userAdapter = new UserAdapter(this);
        Intent intent = getIntent();
        //mDB = (PlayerDBHelper) intent.getSerializableExtra("Data");
        String mSQLiteName = "memory_game.db";
        PlayerDBHelper mDB = new PlayerDBHelper(this, mSQLiteName, null, 1);
        mDB.setId();
        ArrayList<User> users = new ArrayList<>();

        /* fetch all distinct users */
        ArrayList<User> temp = mDB.getDistinctUsers();
        if (temp == null) {
            Log.i("moje", "au brate");
        } else {
            if (temp.isEmpty()) {
                Log.i("moje", "empty temp arraylist wtf");
            } else {
                for (User user : temp) {
                    Log.i("moje", "mesaga " + user.getmUserName());
                }
            }
            /* get their scores */
            for (User user : temp) {
                users.add(mDB.readUser(user.mUserName));
            }
            /* put them in adapter */
            for (User user : users) {
                Log.i("moje", "user: " + user.getmUserName());
                for (int i : user.getmScore()) {
                    Log.i("moje", "\t\t" + Integer.toString(i));
                }
                userAdapter.addUser(user);
            }
        }


        ListView listView = findViewById(R.id.listvju);
        listView.setAdapter(userAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = (User) userAdapter.getItem(i);
                Intent intent = new Intent(StatisticsActivity.this, DetailsActivity.class);
                Bundle arguments = new Bundle();
                arguments.putSerializable("arraylist", user.getNBestResults(10));
                for (int res : user.getNBestResults(10)) {
                    Log.i("moje", "hop: " + Integer.toString(res));
                }
                intent.putExtra("nBestResults", arguments);
                intent.putExtra("userName", user.getmUserName());
                startActivity(intent);
            }
        });
    }
}
