package com.itintegration.orderapp.ui.assortment;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.itintegration.orderapp.R;
import com.itintegration.orderapp.data.model.ArticleSwe;

import java.util.List;

public class AssortmentAdapter extends RecyclerView.Adapter<AssortmentAdapter.ArticleViewHolder>{

    private List<ArticleSwe> articleSweList;
    private Context mContext;

    private static int currentPosition = 0;

    public AssortmentAdapter(List<ArticleSwe> articleSweList, Context mContext) {
        this.articleSweList = articleSweList;
        this.mContext = mContext;
    }

    @Override
    public AssortmentAdapter.ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_list_item_layout_assortment, parent, false);
        return new ArticleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AssortmentAdapter.ArticleViewHolder holder, final int position) {
        ArticleSwe articleSwe = articleSweList.get(position);

        //FIX
        //holder.articleNameHeader.setText(articleSwe.getProductName());
        //holder.articleNumberHeader.setText(String.valueOf(articleSwe.getArticleNumber()));
        holder.header1.setText("Beskrivning");
        holder.content1.setText(articleSwe.getDescription());
        holder.header2.setText("Kvantitet");
        //holder.content2.setText(String.valueOf(articleSwe.getStock()));

        holder.secondInnerConstraintLayout.setVisibility(View.GONE);

        //if the position is equals to the item position which is to be expanded
        if (currentPosition == position) {
            //creating an animation
            Animation slideDown = AnimationUtils.loadAnimation(mContext, R.anim.slide_down);

            //toggling visibility
            holder.secondInnerConstraintLayout.setVisibility(View.VISIBLE);

            //adding sliding effect
            holder.secondInnerConstraintLayout.startAnimation(slideDown);
        }

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getting the position of the item to expand it
                currentPosition = position;

                //reloding the list
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleSweList.size();
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView articleNameHeader, articleNumberHeader,
                header1, content1,
                header2, content2;
        ImageButton imageButton;
        ConstraintLayout secondInnerConstraintLayout;

        ArticleViewHolder(View itemView) {
            super(itemView);

            articleNameHeader = itemView.findViewById(R.id.ArticleNameHeader);
            articleNumberHeader = itemView.findViewById(R.id.ArticleNumberHeader);

            header1 = itemView.findViewById(R.id.header1);
            content1 =  itemView.findViewById(R.id.content1);

            header2 = itemView.findViewById(R.id.header2);
            content2 =  itemView.findViewById(R.id.content2);

            imageButton = itemView.findViewById(R.id.imageButton);

            secondInnerConstraintLayout = itemView.findViewById(R.id.SecondInnerConstraintLayout);
        }
    }
}
