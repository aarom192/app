package com.example.tony.consoleapplication;

/**
 * Created by Tony on 2017/12/19.
 */

public class ListItem {
    private String mName = null;
    private String mCalories = null;
    private String mStore = null;

    /**
     * 空のコンストラクタ
     */
    public ListItem() {};

    /**
     * コンストラクタ
     * @param name 品物の名
     * @param calories カロリー
     */
    public ListItem(String name, String calories, String store) {
        mName = name;
        mCalories = calories;
        mStore = store;
    }

    /**
     * サムネイル画像を設定
     * @param name サムネイル画像
     */
    public void setmName(String name) {
        mName = name;
    }

    /**
     * タイトルを設定
     * @param calories タイトル
     */
    public void setmCalories(String calories) {
        mCalories = calories;
    }

    /**
     * タイトルを設定
     * @param store タイトル
     */
    public void setmStore(String store) {
        mStore = store;
    }

    /**
     * サムネイル画像を取得
     * @return サムネイル画像
     */
    public String getmName() {
        return mName;
    }

    /**
     * タイトルを取得
     * @return タイトル
     */
    public String getmCalories() {
        return mCalories;
    }

    /**
     * タイトルを取得
     * @return タイトル
     */
    public String getmStore() {
        return mStore;
    }
}
