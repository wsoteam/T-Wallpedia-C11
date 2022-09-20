package com.walls.walllivappo.Adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.walls.walllivappo.Catagory
import com.walls.walllivappo.R


//Adapter adapt the view from viewholder and show to the user

class CatagoryRecyclerViewAdapter(private val context: Context, private val Catagory:List<Catagory>):RecyclerView.Adapter<CatagoryRecyclerViewAdapter.CatagoryRecyclerViewViewHolder>() {

    //onCreateViewHolder create the view And store the View in View Holder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CatagoryRecyclerViewViewHolder {
        //layout inflater use to convert the xml layout to the java file
        val inflater=LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.catagory_recylerview_row,parent,false)
        //store the view in view Holder
        return CatagoryRecyclerViewViewHolder(view)
    }
    //What you want to show
    override fun onBindViewHolder(holder: CatagoryRecyclerViewViewHolder, position: Int) {
        holder.Title.text=Catagory[position].Title
        holder.bacground.setImageResource(Catagory[position].Background)
    }
    //return how much data have to store
    override fun getItemCount(): Int {
        return Catagory.size
    }
    //ViewHolder use to hold the view
    class CatagoryRecyclerViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var Title = itemView.findViewById<TextView>(R.id.TVTitle)
        var bacground = itemView.findViewById<ImageView>(R.id.IVBackground)
    }
}
