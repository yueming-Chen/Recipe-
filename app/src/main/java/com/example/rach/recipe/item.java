package com.example.rach.recipe;

/**
 * Created by Rach on 2016/9/6.
 */
public class Item implements java.io.Serializable {

    // 編號、日期時間、顏色、標題、內容、照相檔案名稱、錄音檔案名稱、經度、緯度、修改、已選擇
    private String id;
    private String author;
    private String name;

    public Item() {
        author = "";
        name = "";
    }

    public Item(String id,String author,String name) {
        this.id = id;
        this.author = author;
        this.name = name;
    }

    public String  getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String title) {
        this.author = author;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

}