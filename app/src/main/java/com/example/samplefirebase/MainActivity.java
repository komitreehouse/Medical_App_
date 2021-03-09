//package com.example.samplefirebase;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//
//
//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }
//
//   /* var database = firebase.database();
//    // Read from the database
//    myRef.addValueEventListener(new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//            // This method is called once with the initial value and again
//            // whenever data at this location is updated.
//            String value = dataSnapshot.getValue(String.class);
//            Log.d(TAG, "Value is: " + value);
//        }

//
//        @Override
//        public void onCancelled(DatabaseError error) {
//            // Failed to read value
//            Log.w(TAG, "Failed to read value.", error.toException());
//        }
//    }); */
//
//}

package com.example.samplefirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

// get user input and then pass it into functions in different classes
// put symptoms in an array and pass array into function

// output


public class MainActivity extends AppCompatActivity {
    DatabaseReference reff;
    String input_symptoms;
    EditText input_symptoms_text;
    TextView diagnosis_text;
    String final_diagnosis;

    Button submit_symptoms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input_symptoms_text = (EditText)findViewById(R.id.input_symptoms_text);
        submit_symptoms = (Button) findViewById(R.id.submit_symptoms);
        diagnosis_text = (TextView) findViewById(R.id.possible_diagnosis);


        submit_symptoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    reff = FirebaseDatabase.getInstance().getReference().child("Diagnosis");

                    reff.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            /** retrieves the first symptom
                             // loop through all symptoms of one diagnosis, then calculate percentage.
                             // do this for all diagnosis

                             // Find which diagnosis has highest percentage.
                             // then display that diagnosis
                             */

                            ArrayList<Double> percentages = new ArrayList<>();

                            input_symptoms = input_symptoms_text.getText().toString();
                            final_diagnosis = diagnosis_text.getText().toString();
                            // would have to convert input_symptoms to an array of strings
                            ArrayList<String> input_array = convertToArrayOfStrings(input_symptoms);

                            for (int diagnosis_index = 0; diagnosis_index <= 2; diagnosis_index++) {
                                String diagnosis = snapshot.child(String.valueOf(diagnosis_index)).child("name").getValue().toString();


                                  double count = 0;
                                  // loop through symptoms in each diagnosis
                                  for (int symptoms_index = 0; symptoms_index < 6; symptoms_index++) {

                                    String symptom = snapshot.child(String.valueOf(diagnosis_index)).child("Symptoms").child(String.valueOf(symptoms_index)).getValue().toString();

                                    // loop through inputs and check if each symptom matches any inputs
                                   /* for (int inputs = 0; inputs < input_array.size(); inputs++) {
                                        if (symptom.equals(input_array.get(inputs))) {
                                            count++;
                                        }
                                    }*/

                                   if (symptom.equals(input_array.get(0))) {
                                       count++;
                                   }

                                 }

                                  double percent = (count / 6.0) * 100;
                                  percentages.add(percent);
                            }

                           /* String value_first_percentage = percentages.get(0).toString();
                            String value_second_percentage = percentages.get(1).toString();
                            String value_third_percentage = percentages.get(2).toString();
                            //final_diagnosis = value_first_percentage;
                            //display(final_diagnosis);
                            //final_diagnosis = value_second_percentage;
                           // display(final_diagnosis);
                            //final_diagnosis = value_third_percentage;
                           // display(final_diagnosis);*/

                            int highest_percentage_index  = calculateHighestPercentage(percentages);

                            String diagnosis_name = snapshot.child(String.valueOf(highest_percentage_index)).child("name").getValue().toString();

                            final_diagnosis = diagnosis_name;
                            display(final_diagnosis);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            }
        });

    }

    /**
     * returns index of highest percentage
     * @param percentages
     * @return
     */
    private int calculateHighestPercentage(ArrayList<Double> percentages) {

        double max = 0;
        int max_index = 0;
        for (int i = 0; i < percentages.size(); i++) {
            if (percentages.get(i) > max) {
                max = percentages.get(i);
                max_index = i;
            }
        }

        return max_index;
    }

    private ArrayList<String> convertToArrayOfStrings(String input_symptoms) {

        /**
         * change so that it is each symptom comma seperated
         */
        ArrayList<String> array = new ArrayList<>();
        array.add(input_symptoms);
        return array;

    }

    private void display(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
    }
}
