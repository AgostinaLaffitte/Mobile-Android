package ar.edu.unicen.seminario.ui

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ar.edu.unicen.seminario.R
import ar.edu.unicen.seminario.ddl.models.Game
import com.bumptech.glide.Glide

class GameAdapter(private var items: List<Game>) : RecyclerView.Adapter<GameAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.gameImage)
        val name: TextView = view.findViewById(R.id.gameName)
        val platforms: TextView = view.findViewById(R.id.gamePlatforms)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val game = items[position]
        Log.d("GameAdapter", "bind pos=$position name=${game.name}")
        holder.name.text = game.name
        holder.platforms.text = game.platforms.joinToString(", ")
        game.imageUrl?.let { url ->
            Glide.with(holder.itemView.context)
                .load(url)
                .into(holder.image)
        }
    }

    override fun getItemCount(): Int = items.size


    fun updateData(newList: List<Game>) {
        this.items = newList
        notifyDataSetChanged()
    }
}
