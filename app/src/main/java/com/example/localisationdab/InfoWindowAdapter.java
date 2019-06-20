package com.example.localisationdab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

public class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mcontext;
    private Dab dab;

    public InfoWindowAdapter(Context context) {
        this.mcontext = context;
        mWindow= LayoutInflater.from(context).inflate(R.layout.infowindow,null);
        this.dab=dab;
    }
    private void windowText(Marker marker, View view ){

        String title= marker.getTitle();
        TextView tvTitle=view.findViewById(R.id.title);
        tvTitle.setText(title);

        if(title.equals("")){
            marker.setTitle(title);
        }

        String snippet=marker.getSnippet();
        TextView tvsnippet =view.findViewById(R.id.adresse);
        tvsnippet.setText(snippet);
        if(snippet.equals("")){
            marker.setSnippet(snippet);
        }

    }

    @Override
    public View getInfoWindow(Marker marker) {
        windowText(marker,mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        windowText(marker,mWindow);
        return mWindow;
    }
}
