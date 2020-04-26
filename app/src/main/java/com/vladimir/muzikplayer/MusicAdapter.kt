package com.vladimir.muzikplayer

import android.content.Context
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_layout.view.*


class MusicAdapter(
    private val context: Context,
    private val musicModel: ArrayList<MusicModel>,
    private val recyclerView: RecyclerView
) :
    RecyclerView.Adapter<MusicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        return MusicViewHolder(view)
    }

    override fun getItemCount(): Int {
        return musicModel.size
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val model = musicModel[position]
        holder.itemView.title.text = model.title
        holder.itemView.artist.text = model.artist
        holder.itemView.duration.text = model.getMinutes()

        holder.itemView.setOnClickListener(View.OnClickListener {
            val pos = recyclerView.getChildAdapterPosition(it)
            // Log.d("CLICKED ITEM", pos.toString())
            if (MainActivity.mPlayer == null) {
                MainActivity.mPlayer = MediaPlayer()
                MainActivity.mPlayer?.setDataSource(context, musicModel[pos].getUri())
                MainActivity.mPlayer?.prepare()
                MainActivity.mPlayer?.start()
            }
            MainActivity.mPlayer!!.reset()
            MainActivity.mPlayer!!.setDataSource(context, musicModel[pos].getUri())
            MainActivity.mPlayer!!.prepare()
            MainActivity.mPlayer!!.start()
        })
    }

}