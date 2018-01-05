package com.example.tony.consoleapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.AppLaunchChecker;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import java.io.IOException;
import java.util.List;
import android.database.Cursor;


public class MainActivity extends AppCompatActivity implements AddFragment.OnFragmentInteractionListener{
    ListView listView;
    String ServerURL_getdata = "http://192.168.100.95/Android/getdata.php" ;
    String ServerURL_insertdata = "http://192.168.100.95/Android/insertdata.php" ;
    DBAdapter dbAdapter;
    private List<ListItem> listItems;
    protected ListItem myListItem;
    ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbAdapter = new DBAdapter(this);
        listItems = new ArrayList<>();

        listView = (ListView) findViewById(R.id.listView);
        if(AppLaunchChecker.hasStartedFromLauncher(this)){
            Log.d("AppLaunchChecker","2回目以降");
            getSqliteData();
        } else {
            getJSON(ServerURL_getdata);
            Log.d("AppLaunchChecker","はじめてアプリを起動した");
        }

        AppLaunchChecker.onActivityCreate(this);

    }

//    @Override
//    public void onResume() {
//        super.onResume();  // Always call the superclass method first
//        loadMyList();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_settings) {
            AddFragment addFragment= new AddFragment();

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();// begin  FragmentTransaction
            ft.add(R.id.container, addFragment);    // add    Fragment
            ft.addToBackStack("fragB");
            ft.commit();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getJSON(final String urlWebService) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                 //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    loadIntoListView(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void loadIntoListView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
       ArrayList<ListItem> listItems = new ArrayList<>();

       dbAdapter.openDB();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject obj = jsonArray.getJSONObject(i);

//                ListItem item = new ListItem(obj.getString("id"),obj.getString("name"),obj.getString("calorie"), obj.getString("store"));
//                listItems.add(item);
               dbAdapter.saveDB(obj.getString("name"),obj.getString("calorie"), obj.getString("store"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        dbAdapter.closeDB();
        loadMyList();
//        ListAdapter adapter = new ListAdapter (this, R.layout.list_item, listItems);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(onItemClickListener);  // タップ時のイベントを追加

    }

    /**
     * リストビューのタップイベント
     */
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,final int position, long id) {
            // タップしたアイテムの取得
            ListView listView = (ListView)parent;
            ListItem item = (ListItem)listView.getItemAtPosition(position);  // SampleListItemにキャスト
            // IDを取得する
            myListItem = listItems.get(position);
            final String listId = myListItem.getId();

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Tap No. " + listId);
            builder.setMessage(item.getmName());
            builder.setNegativeButton("Delete",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub

                    dbAdapter.openDB();     // DBの読み込み(読み書きの方)
                    dbAdapter.selectDelete(listId);     // DBから取得したIDが入っているデータを削除する
                    dbAdapter.closeDB();    // DBを閉じる
                    loadMyList();
                    Toast.makeText(MainActivity.this, "No." + listId + "  is deleted",Toast.LENGTH_SHORT).show();
                }
            });
            //builder.setMessage(item.getmCalories());
            builder.show();
        }
    };

    @Override
    public void onFragmentInteraction(String name , String calorie, String store){
        //you can leave it empty
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        InsertData(name, calorie, store);
        Toast.makeText(getApplicationContext(), name+" "+calorie+"kcal", Toast.LENGTH_SHORT).show();
        loadMyList();
    }

    public void InsertData(final String name, final String calorie, final String store){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String NameHolder = name;
                String CalorieHolder = calorie ;
                String StoreHolder = store;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("name", NameHolder));
                nameValuePairs.add(new BasicNameValuePair("calorie", CalorieHolder));
                nameValuePairs.add(new BasicNameValuePair("store", StoreHolder));

                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(ServerURL_insertdata);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"utf8"));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();


                } catch (ClientProtocolException e) {
                    return "ClientProtocolException";

                } catch (IOException e) {
                    return "IOException";
                }
                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);

                Toast.makeText(MainActivity.this, "Data Submit Successfully", Toast.LENGTH_LONG).show();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(name, calorie, store);
    }

    public void getSqliteData(){

        // DBの検索データを取得 入力した文字列を参照してDBの品名から検索
        dbAdapter.readDB();
        Cursor c = dbAdapter.getDB(null);

        if (c.moveToFirst()) {
            do {
                ListItem item = new ListItem(c.getString(0), c.getString(1), c.getString(2), c.getString(3));
                listItems.add(item);
            } while (c.moveToNext());
        } else {
            Toast.makeText(this, "検索結果 0件", Toast.LENGTH_SHORT).show();
        }
        c.close();
        dbAdapter.closeDB();        // DBを閉じる
        adapter = new ListAdapter (this, R.layout.list_item, listItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onItemClickListener);  // タップ時のイベントを追加
    }

    /**
     * DBを読み込む＆更新する処理
     * loadMyList()
     */
    public void loadMyList() {

        //ArrayAdapterに対してListViewのリスト(items)の更新
        listItems.clear();
        //adapter.clear();
        dbAdapter.openDB();     // DBの読み込み(読み書きの方)

        // DBのデータを取得
        Cursor c = dbAdapter.getDB(null);

        if (c.moveToFirst()) {
            do {
                // MyListItemのコンストラクタ呼び出し(myListItemのオブジェクト生成)
                myListItem = new ListItem(c.getString(0), c.getString(1), c.getString(2), c.getString(3));
                listItems.add(myListItem);          // 取得した要素をitemsに追加

            } while (c.moveToNext());
        }
        c.close();
        dbAdapter.closeDB();                    // DBを閉じる
        ListAdapter adapter = new ListAdapter (this, R.layout.list_item, listItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onItemClickListener);  // タップ時のイベントを追加
    }
}
