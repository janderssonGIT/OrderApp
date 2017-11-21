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
import com.itintegration.orderapp.data.model.Article;

import java.util.List;

public class AssortmentAdapter extends RecyclerView.Adapter<AssortmentAdapter.ArticleViewHolder>{

    private List<Article> articleList;
    private Context mContext;

    private static int currentPosition = 0;

    public AssortmentAdapter(List<Article> articleList, Context mContext) {
        this.articleList = articleList;
        this.mContext = mContext;
    }

    @Override
    public AssortmentAdapter.ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_list_item_layout_assortment, parent, false);
        return new ArticleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AssortmentAdapter.ArticleViewHolder holder, final int position) {
        Article article = articleList.get(position);

        //FIX
        holder.header1.setText(article.getProductName());
        holder.content1.setText(article.getProductName());
        holder.header2.setText(article.getProductName());
        holder.content2.setText(article.getProductName());

        holder.SecondInnerConstraintLayout.setVisibility(View.GONE);

        //if the position is equals to the item position which is to be expanded
        if (currentPosition == position) {
            //creating an animation
            Animation slideDown = AnimationUtils.loadAnimation(mContext, R.anim.slide_down);

            //toggling visibility
            holder.SecondInnerConstraintLayout.setVisibility(View.VISIBLE);

            //adding sliding effect
            holder.SecondInnerConstraintLayout.startAnimation(slideDown);
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
        return articleList.size();
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView header1, content1,
            header2, content2;

        ImageButton imageButton;

        ConstraintLayout SecondInnerConstraintLayout;

        ArticleViewHolder(View itemView) {
            super(itemView);

            header1 = itemView.findViewById(R.id.header1);
            content1 =  itemView.findViewById(R.id.content1);

            header2 = itemView.findViewById(R.id.header2);
            content2 =  itemView.findViewById(R.id.content2);

            imageButton = itemView.findViewById(R.id.imageButton);

            SecondInnerConstraintLayout = itemView.findViewById(R.id.SecondInnerConstraintLayout);
        }
    }
}
