package com.ddkcommunity.model.projects;

import android.graphics.drawable.Drawable;

public class PoolingDoc{
    public String description="";
    public Drawable drawable;
    public String docName="";
    public PoolingDoc(String description,Drawable drawable,String docName){
        this.description=description;
        this.drawable=drawable;
        this.docName=docName;
    }
}
