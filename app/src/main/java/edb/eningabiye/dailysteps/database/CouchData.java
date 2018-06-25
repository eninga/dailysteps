package edb.eningabiye.dailysteps.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseConfiguration;
import com.couchbase.lite.Document;
import com.couchbase.lite.Expression;
import com.couchbase.lite.Function;
import com.couchbase.lite.MutableDocument;
import com.couchbase.lite.Ordering;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryBuilder;
import com.couchbase.lite.ResultSet;
import com.couchbase.lite.SelectResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import edb.eningabiye.dailysteps.model.Message;
import edb.eningabiye.dailysteps.model.User;

public class CouchData {
    public static final String DATABASE_NAME = "pedometre";
    public static final String DATABASE_MSG = "messages";

    private DatabaseConfiguration config;
    private Database database;
    private Database database_message;
    private  String nom;
    public CouchData(Context context) throws CouchbaseLiteException {
        config = new DatabaseConfiguration(context);
        database = new Database(DATABASE_NAME, config);
        database_message = new Database(DATABASE_MSG, config);
        SharedPreferences sharedPref = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        nom = sharedPref.getString("username", null);
    }

    public void saveStep(int step) throws CouchbaseLiteException {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.FRENCH);
        String formattedDate = df.format(c);
        String id = formattedDate+nom;
        String[] data = formattedDate.split(" ");
        MutableDocument mutableDoc = new MutableDocument(id);
        Document document = database.getDocument(mutableDoc.getId());

        if(document != null && document.getString("user").equals(nom)) {
            mutableDoc = database.getDocument(mutableDoc.getId()).toMutable();
            int steps = mutableDoc.getInt("steps");
            mutableDoc.setInt("steps", step+steps)
                    .setString("date", formattedDate)
                    .setString("month", data[1])
                    .setString("year", data[2])
                    .setString("user", nom);
        }else{
            mutableDoc.setString("date", formattedDate)
                    .setInt("steps", step)
                    .setString("month", data[1])
                    .setString("year", data[2])
                    .setString("user", nom);
        }
        database.save(mutableDoc);
    }
    public ResultSet getSteps() throws CouchbaseLiteException {
        Query query = QueryBuilder.select(SelectResult.all())
                .from(DataSource.database(database))
                .where(Expression.property("user").equalTo(Expression.string(nom)));
        return query.execute();
    }
    public void saveMessages(String nom, String steps, String msg){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy H:m:s", Locale.FRENCH);
        String formattedDate = df.format(c);

        MutableDocument mutableDoc = new MutableDocument(formattedDate);
        mutableDoc.setString("nom", nom)
                .setString("steps", steps)
                .setString("date", formattedDate)
                .setString("message", msg);
        try {
            database_message.save(mutableDoc);
        } catch (CouchbaseLiteException e) {
            Log.e("Saving error",e.getMessage());
        }
    }
    public ResultSet getMessages() {
        Query query = QueryBuilder.select(SelectResult.all())
                .from(DataSource.database(database_message))
                .orderBy(Ordering.property("date").descending());
        try {
            return query.execute();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        return  null;
    }
    public ResultSet getTotalSteps() throws CouchbaseLiteException {
        Query query = QueryBuilder.select(
                SelectResult.expression(Function.sum(Expression.property("steps"))),
                SelectResult.property("year"),
                SelectResult.property("month"))
                .from(DataSource.database(database))
                .where(Expression.property("user").equalTo(Expression.string(nom)))
                .groupBy(Expression.property("month"),Expression.property("year"))
                .orderBy(Ordering.property("date").descending());
        return query.execute();
    }
    public ResultSet getMaxSteps() throws CouchbaseLiteException {
        Query query = QueryBuilder.select(
                SelectResult.expression(Function.max(Expression.property("steps"))),
                SelectResult.property("month"),
                SelectResult.property("year"))
                .from(DataSource.database(database))
                .where(Expression.property("user").equalTo(Expression.string(nom)))
                .groupBy(Expression.property("month"),Expression.property("year"))
                .orderBy(Ordering.property("date").descending());
        return query.execute();
    }
    public ResultSet getMinSteps() throws CouchbaseLiteException {
        Query query = QueryBuilder.select(
                SelectResult.expression(Function.min(Expression.property("steps"))),
                SelectResult.property("month"),
                SelectResult.property("year"))
                .from(DataSource.database(database))
                .where(Expression.property("user").equalTo(Expression.string(nom)))
                .groupBy(Expression.property("month"),Expression.property("year"))
                .orderBy(Ordering.property("date").descending());
        return query.execute();
    }
    public ResultSet getMoySteps() throws CouchbaseLiteException {
        Query query = QueryBuilder.select(
                SelectResult.expression(Function.avg(Expression.property("steps"))),
                SelectResult.property("month"),
                SelectResult.property("year"))
                .from(DataSource.database(database))
                .where(Expression.property("user").equalTo(Expression.string(nom)))
                .groupBy(Expression.property("month"),Expression.property("year"))
                .orderBy(Ordering.property("date").descending());
        return query.execute();
    }
    public ResultSet getLastSteps() throws CouchbaseLiteException {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.FRENCH);
        String formattedDate = df.format(c);
        Query query = QueryBuilder.select(SelectResult.all())
                .from(DataSource.database(database))
                .where(Expression.property("user").equalTo(Expression.string("Eric")))
                .orderBy(Ordering.property("date").descending());
        return query.execute();
    }
    public void getStepsi() throws CouchbaseLiteException {
        Query query = QueryBuilder.select(SelectResult.all())
                .from(DataSource.database(database))
                .where(Expression.property("user").equalTo(Expression.string("Eric")));
        ResultSet results = query.execute();
    }
}
