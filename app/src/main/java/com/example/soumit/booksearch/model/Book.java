package com.example.soumit.booksearch.model;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Soumit on 2/16/2018.
 */

public class Book implements Serializable {

    private String openLibraryId;
    private String author;
    private String title;

    // Get medium sized book cover from covers API
    public String getCoverUrl() {
        return "http://covers.openlibrary.org/b/olid/" + openLibraryId + "-M.jpg?default=false";
    }

    // Get large sized book cover from covers API
    public String getLargeCoverUrl() {
        return "http://covers.openlibrary.org/b/olid/" + openLibraryId + "-L.jpg?default=false";
    }


    public static Book fromJson(JSONObject jsonObject){
        Book book = new Book();

        try {
            if(jsonObject.has("cover_edition_key")){
                book.openLibraryId = jsonObject.getString("cover_edition_key");
            }else if(jsonObject.has("edition_key")){
                final JSONArray ids = jsonObject.getJSONArray("edition_key");
                book.openLibraryId = ids.getString(0);
            }
            book.title = jsonObject.has("title_suggest")?jsonObject.getString("title_suggest") : "";
            book.author = getAuthor(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return book;
    }

    private static String getAuthor(JSONObject jsonObject) {

        try {
            JSONArray authors = jsonObject.getJSONArray("author_name");
            int numAuthors = authors.length();
            String[] authorStrings = new String[numAuthors];
            for(int i=0;i<numAuthors;i++){
                authorStrings[i] = authors.getString(i);
            }

            return TextUtils.join(", ", authorStrings);
        } catch (JSONException e) {
            return "";
        }

    }


    public static ArrayList<Book> fromJson(JSONArray jsonArray) {

        ArrayList<Book> books = new ArrayList<>(jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject bookJson = null;

            try {
                bookJson = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }

            Book book = Book.fromJson(bookJson);
            if(book!=null){
                books.add(book);
            }
        }

        return books;
    }





    public String getOpenLibraryId() {
        return openLibraryId;
    }

    public void setOpenLibraryId(String openLibraryId) {
        this.openLibraryId = openLibraryId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
