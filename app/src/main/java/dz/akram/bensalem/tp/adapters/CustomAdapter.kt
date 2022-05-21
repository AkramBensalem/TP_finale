package dz.akram.bensalem.tp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dz.akram.bensalem.tp.R
import dz.akram.bensalem.tp.database.Song

class CustomAdapter(
    private var items: List<Song>,
    private val onClick: (song : Song,position : Int) -> Unit = {_,_ ->}
) : RecyclerView.Adapter<CustomAdapter.FavouriteViewHolder>() {

    fun setData(data: List<Song>) {
        items = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_playlist, parent, false)

        return FavouriteViewHolder(view)
    }

    override fun getItemCount(): Int =  items.size

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
       holder.bind(items[position], position,onClick)
    }

    class FavouriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val cardItem: CardView = itemView.findViewById(R.id.card_view_song_item)
        private val title : TextView by lazy {itemView.findViewById(R.id.song_item_name)  }
        private val image : ImageView by lazy {itemView.findViewById(R.id.song_image_cover)  }
        private val description : TextView by lazy {itemView.findViewById(R.id.song_item_description)  }


        fun bind(
            item: Song,
            position: Int,
            onClick: (Song, Int) -> Unit
        ) {
            Glide.with(itemView.context)
                .load(R.drawable.background)
                .centerCrop()
                .into(image)

            title.text = item.name
            description.text = item.description ?: "Unknown"

            cardItem.setOnClickListener { onClick(item, position) }
        }

    }
}