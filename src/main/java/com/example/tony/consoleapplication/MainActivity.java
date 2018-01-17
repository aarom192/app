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
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
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
import java.util.Map;

import android.database.Cursor;


public class MainActivity extends AppCompatActivity implements AddFragment.OnFragmentInteractionListener, TestDialogFragment.TestDialogFragmentListener{
    ListView listView;
    String ServerURL_getdata = "http://192.168.100.95/Android/getdata.php" ;
    String ServerURL_insertdata = "http://192.168.100.95/Android/insertdata.php" ;
    String ServerURL_deletedata = "http://192.168.100.95/Android/deletedata.php" ;
    String ServerURL_updatedata = "http://192.168.100.95/Android/updatedata.php" ;
    public final static int COL_ID = 0;                 // id
    public final static int COL_NAME = 1;             // 品名
    public final static int COL_CALORIE = 2;          // カロリー
    public final static int COL_STORE = 3;             // ストア
    DBAdapter dbAdapter;
    private List<ListItem> listItems;
    protected ListItem myListItem;
    ListAdapter adapter;
    int LastID;
    ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbAdapter = new DBAdapter(this);
        listItems = new ArrayList<>();
        expandableListView = (ExpandableListView)findViewById(R.id.sample_list);
        int[] rowId = {0,1,2};

        //listView = (ListView) findViewById(R.id.listView);
        if(AppLaunchChecker.hasStartedFromLauncher(this)){
            Log.d("AppLaunchChecker","2回目以降");
            // getSqliteData();
            expandableListView.setAdapter(new CostmizeExpandableListAdapter(this, rowId, createGroupItemList(), createChildrenItemList()));
        } else {
            Log.d("AppLaunchChecker","はじめてアプリを起動した");
            getJSON(ServerURL_getdata);
        }
        AppLaunchChecker.onActivityCreate(this);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

               // Adapterをもらってくる
               ExpandableListAdapter adapter = parent.getExpandableListAdapter();
               // メンバー表示用データ作成時に作ったブツがもらえます
                ListItem item = (ListItem)adapter.getChild(groupPosition, childPosition);
                Toast.makeText(getApplicationContext(), "name clicked:" + item.getmName().toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "calories:" + item.getmCalories().toString(), Toast.LENGTH_SHORT).show();

                return true;
            }
        });
    }

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
            Bundle bundle = new Bundle();

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

              // ListItem item = new ListItem(obj.getString("id"),obj.getString("name"),obj.getString("calorie"), obj.getString("store"));
//                listItems.add(item);
               dbAdapter.saveDB( obj.getString("name"),obj.getString("calorie"), obj.getString("store"));
               if (i < jsonArray.length()) {
                   LastID = Integer.parseInt(obj.getString("id"));
               }

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
            final ListItem item = (ListItem)listView.getItemAtPosition(position);  // SampleListItemにキャスト
            // IDを取得する
            myListItem = listItems.get(position);
            final String listId = myListItem.getId();
            final String listName = myListItem.getmName();

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("No. " + listId+" "+item.getmName());
            builder.setMessage("カロリー: " + item.getmCalories() + "kcal\n" + "ストア: " + item.getmStore());
            // データを渡す為のBundleを生成し、渡すデータを内包させる

            builder.setPositiveButton("更新",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // ダイアログを表示する
                    Bundle bundle = new Bundle();
                    bundle.putString("id", listId);
                    bundle.putString("name", item.getmName());
                    bundle.putString("calorie", item.getmCalories());
                    bundle.putString("store", item.getmStore());
                    TestDialogFragment dialogFragment = new TestDialogFragment();
                    dialogFragment.setArguments(bundle);
                    dialogFragment.show(getFragmentManager(), "test");

                }
            });
            builder.setNegativeButton("Delete",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    AlertDialog.Builder negativebuilder = new AlertDialog.Builder(MainActivity.this);
                    negativebuilder.setTitle("本当に削除しますか?");
                    negativebuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            //Toast.makeText(MainActivity.this, "No." + listId,Toast.LENGTH_SHORT).show();
                            dbAdapter.openDB();     // DBの読み込み(読み書きの方)
                            dbAdapter.selectDelete(listId);     // DBから取得したIDが入っているデータを削除する
                            dbAdapter.closeDB();    // DBを閉じる
                            loadMyList();
                            deleteData(listName);
                            Toast.makeText(MainActivity.this, "No." + listId + "  is deleted",Toast.LENGTH_SHORT).show();
                        }
                    });
                    negativebuilder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });
                    negativebuilder.show();
                }
            });
            //builder.setMessage(item.getmCalories());
            builder.show();
        }
    };

    @Override
    public void TestDialogFragmentInteraction(String id, String originName, String name , String calorie, String store){
        //you can leave it empty
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        dbAdapter.openDB();     // DBの読み込み(読み書きの方)
        dbAdapter.updateDB(id, name, calorie);     // DBから取得したIDが入っているデータを削除する
        dbAdapter.closeDB();    // DBを閉じる
        UpdateData(originName, name, calorie, store);
        Toast.makeText(this, "Original Name:" +originName+"Name:" +name+ "  and Calories:" + calorie
                + "kcal" + " and Store:" + store,Toast.LENGTH_SHORT).show();
        loadMyList();
    }

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
    public void deleteData(final String name){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String NameHolder = name;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("name", NameHolder));

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(ServerURL_deletedata);
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

        sendPostReqAsyncTask.execute(name);
    }

    public void UpdateData(final String originalname,final String name, final String calorie, final String store){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String OriginalNameHolder = originalname;
                String NameHolder = name;
                String CalorieHolder = calorie ;
                String StoreHolder = store;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("originalname", OriginalNameHolder));
                nameValuePairs.add(new BasicNameValuePair("name", NameHolder));
                nameValuePairs.add(new BasicNameValuePair("calorie", CalorieHolder));
                nameValuePairs.add(new BasicNameValuePair("store", StoreHolder));

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(ServerURL_updatedata);
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
        int[] rowId = {0,1,2};
        expandableListView.setAdapter(new CostmizeExpandableListAdapter(this, rowId, createGroupItemList(), createChildrenItemList()));
    }

    /**
     *
     * @return
     */
    private List<List<ListItem>> createChildrenItemList() {
        //ArrayAdapterに対してListViewのリスト(items)の更新
        listItems.clear();
        //adapter.clear();
        dbAdapter.openDB();     // DBの読み込み(読み書きの方)

        // DBのデータを取得
        Cursor c = dbAdapter.getDB(null);
        List<ListItem> SEVENELEVEN = new ArrayList<>();
        List<ListItem> FamilyMart = new ArrayList<>();
        List<ListItem> DaYung = new ArrayList<>();
        List<ListItem> Others = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                if (c.getString(COL_STORE).equals("7-11")) {
                    SEVENELEVEN.add(new ListItem(c.getString(COL_ID), c.getString(COL_NAME), c.getString(COL_CALORIE), c.getString(COL_STORE)));
                } else if (c.getString(COL_STORE).equals("大苑子")) {
                    DaYung.add(new ListItem(c.getString(COL_ID), c.getString(COL_NAME), c.getString(COL_CALORIE), c.getString(COL_STORE)));
                } else if (c.getString(COL_STORE).equals("全家")) {
                    FamilyMart.add(new ListItem(c.getString(COL_ID), c.getString(COL_NAME), c.getString(COL_CALORIE), c.getString(COL_STORE)));
                } else {
                    Others.add(new ListItem(c.getString(COL_ID), c.getString(COL_NAME), c.getString(COL_CALORIE), c.getString(COL_STORE)));
                }
            } while (c.moveToNext());
        }
        c.close();
        dbAdapter.closeDB();
        List<List<ListItem>> result = new ArrayList<List<ListItem>>();
        result.add(SEVENELEVEN);
        result.add(FamilyMart);
        result.add(DaYung);
        result.add(Others);
        return result;
    }

    /**
     *
     * @return
     */
    private List<String> createGroupItemList() {
        List<String> groups = new ArrayList<String>();
        groups.add("7-11");
        groups.add("全家");
        groups.add("大苑子");
        groups.add("其它");
        return groups;
    }
}

