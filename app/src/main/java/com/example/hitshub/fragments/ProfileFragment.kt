package com.example.hitshub.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.hitshub.R
import com.example.hitshub.activities.BaseActivity
import com.example.hitshub.activities.RegisterActivity
import com.example.hitshub.adapter.AlbumRecyclerViewAdapter
import com.example.hitshub.models.IAlbum
import com.example.hitshub.models.ITrack
import com.example.hitshub.models.User
import com.example.hitshub.utils.InjectorUtils
import com.example.hitshub.viewmodels.MediaViewModel
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseFragment() {
    private val user by lazy { activity!!.intent.getParcelableExtra<User>(BaseActivity.USER) }
    private val factory by lazy { InjectorUtils.provideViewModelFactory(activity!!.applicationContext) }
    private val mediaViewModel by lazy {
        ViewModelProvider(this, factory).get(MediaViewModel::class.java)
    }
    override val adapter by lazy {
        AlbumRecyclerViewAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        liked_tracks_recycler_view.adapter = adapter
        mediaViewModel.getTracks()
        mediaViewModel.tracksLiveData.observe(viewLifecycleOwner, Observer {
            adapter.clear()
            it.forEach { track ->
                adapter.addItem(track)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = toolbar as Toolbar
        toolbar.title = "Profile"
        (activity!! as AppCompatActivity).setSupportActionBar(toolbar as Toolbar?)

        if (!user!!.isAnonymous) {
            Picasso.get().load(user!!.avatarUrl).fit().into(avatar_image_view)
            name_textView.text = user!!.name
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout_view_btn) {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(activity!!, RegisterActivity::class.java))
            activity!!.finishAffinity()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClickItem(response: ITrack) {
        callMediaPlayer(response, adapter.playlist)
    }

    override fun onClickItem(response: IAlbum) {}
}
