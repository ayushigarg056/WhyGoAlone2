package com.ayushigarg.whygoalone.Utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ayushigarg.whygoalone.R;


/**
 * Created by user on 10-Jan-18.
 */

public class MessageHolder extends RecyclerView.ViewHolder {
    TextView name,msgg;
    public MessageHolder(View itemView) {
        super(itemView);
        name=(TextView)itemView.findViewById(R.id.name);
        msgg=(TextView)itemView.findViewById(R.id.msg);


    }
}
