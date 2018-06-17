package edb.eningabiye.dailysteps.database;

import android.content.Context;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseConfiguration;
import com.couchbase.lite.Document;
import com.couchbase.lite.Expression;
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

import edb.eningabiye.dailysteps.model.User;

public class CouchData {
    public static final String DATABASE_NAME = "pedometre";
    private DatabaseConfiguration config;
    private Database database;
    public CouchData(Context context) throws CouchbaseLiteException {
        config = new DatabaseConfiguration(context);
        database = new Database(DATABASE_NAME, config);
    }

    public void saveStep(int step, User user) throws CouchbaseLiteException {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.FRENCH);
        String formattedDate = df.format(c);

        MutableDocument mutableDoc = new MutableDocument(formattedDate);
        Document document = database.getDocument(mutableDoc.getId());

        if(document != null) {
            mutableDoc = database.getDocument(mutableDoc.getId()).toMutable();
            int steps = mutableDoc.getInt("steps");
            mutableDoc.setInt("steps", step+steps)
            .setString("date", formattedDate)
                    .setString("user", user.getName());
        }else{
            mutableDoc.setString("date", formattedDate)
                    .setInt("steps", step)
                    .setString("user", user.getName());
        }
        database.save(mutableDoc);
    }
    public ResultSet getSteps() throws CouchbaseLiteException {
        Query query = QueryBuilder.select(SelectResult.all())
                .from(DataSource.database(database));
        //.orderBy(Ordering.property("date").descending());
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
