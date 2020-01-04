package iv.nakonechnyi.gituser.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import iv.nakonechnyi.gituser.R
import iv.nakonechnyi.gituser.repository.db.GitUserWithRepos
import kotlinx.android.synthetic.main.recycler_user_main.view.*


class GitUsersAdapter(
    private var items: MutableList<GitUserWithRepos>
) : RecyclerView.Adapter<GitUsersAdapter.GitUserHolder>(){

    private val TAG: String get() = javaClass.simpleName

    private lateinit var listener: OnClickListener

    fun setClickListener(callback: OnClickListener) {
        listener = callback
    }

    fun setItems(list: MutableList<GitUserWithRepos>) {
        items = list
        notifyDataSetChanged()
    }

    inner class GitUserHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            try {
                listener.onClick(adapterPosition)
            } catch (e: UninitializedPropertyAccessException) {
                Log.e(TAG, "Need to initialize a field 'listener'", e)
            }
        }

    }

    override fun onBindViewHolder(holder: GitUserHolder, position: Int) {
        val user = items[position]
        with(holder.itemView){
            Glide.with(context)
                .load(user.user.avatarUrl)
                .into(avatar)
            login.text = user.user.login
            name.text = user.user.name
            location.text = user.user.location
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LayoutInflater.from(parent.context).inflate(R.layout.recycler_user_main, parent, false)
            .run(::GitUserHolder)

    override fun getItemCount() = items.size

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    interface OnClickListener {
        fun onClick(position: Int)
    }

}